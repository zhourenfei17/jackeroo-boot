package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.exception.JackerooException;
import cn.hub.jackeroo.system.entity.SysMenu;
import cn.hub.jackeroo.system.mapper.SysMenuMapper;
import cn.hub.jackeroo.system.vo.AuthVo;
import cn.hub.jackeroo.system.vo.Tree;
import cn.hub.jackeroo.system.vo.TreeSelect;
import cn.hub.jackeroo.utils.StringUtils;
import cn.hub.jackeroo.vo.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单信息 服务实现类
 * </p>
 *
 * @author jackeroo
 * @since 2020-05-18
 */
@Service
public class SysMenuService extends ServiceImpl<SysMenuMapper, SysMenu> {

    @Resource
    private SysMenuMapper mapper;

    /**
     * 获取叶子菜单权限列表
     * @param parentId
     * @param pageParam
     * @return
     */
    public IPage<SysMenu> findPermissionPage(Long parentId, PageParam pageParam){
        SysMenu sysMenu = new SysMenu();
        sysMenu.setParentId(parentId);
        sysMenu.setType(SysMenu.TYPE_PERMISSION);
        sysMenu.setDelFlag(Constant.DEL_FLAG_NORMAL);

        Page<SysMenu> page = sysMenu.initPage(pageParam);
        page.setRecords(mapper.findPermissionList(sysMenu));

        return page;
    }

    /**
     * 获取菜单列表-树形结构
     * @return
     */
    @Cacheable("FullMenuTree")
    public List<SysMenu> getMenuFullTree(String name){
        List<SysMenu> menuList = super.list();

        List<SysMenu> rootTreeNode = menuList.stream().filter(node -> node.getParentId().equals(0L)).collect(Collectors.toList());
        buildMenuTree(rootTreeNode, menuList);

        if(StringUtils.isNotBlank(name)){
            List<SysMenu> newMenuList = new ArrayList<>();
            for (SysMenu menu : rootTreeNode) {
                if(menu.getName().contains(name) || filterMenuByName(menu.getChildren(), name)){
                    newMenuList.add(menu);
                }
            }
            return newMenuList;
        }

        return rootTreeNode;
    }

    /**
     * 获取角色权限配置的完整菜单、权限树
     * @return
     */
    public List<Tree> getRolePermissionTree(){
        List<SysMenu> menuList = super.list();
        List<Tree> rootTreeNode = menuList
                .stream()
                .filter(node -> node.getParentId().equals(0L))
                .sorted(Comparator.comparingInt(SysMenu::getSort))
                .map(node -> new Tree(node.getId(), node.getName()))
                .collect(Collectors.toList());

        buildFullTree(rootTreeNode, menuList);

        return rootTreeNode;
    }

    private void buildFullTree(List<Tree> rootTree, List<SysMenu> fullMenu){
        for (Tree tree : rootTree) {
            tree.setChildren(fullMenu
                    .stream()
                    .filter(node -> node.getParentId().equals(tree.getKey()))
                    .sorted(Comparator.comparingInt(SysMenu::getSort))
                    .map(node -> new Tree(node.getId(), node.getName()))
                    .collect(Collectors.toList()));

            if(CollectionUtils.isNotEmpty(tree.getChildren())){
                buildFullTree(tree.getChildren(), fullMenu);
            }
        }
    }

    private boolean filterMenuByName(List<SysMenu> menuList, String name){
        if(CollectionUtils.isEmpty(menuList)){
            return false;
        }
        for (SysMenu menu : menuList) {
            if(menu.getName().contains(name)){
                return true;
            }
            boolean flag = filterMenuByName(menu.getChildren(), name);
            if(flag){
                return true;
            }
        }
        return false;
    }

    /**
     * 构建树结构
     * @param rootMenu
     * @param fullMenu
     */
    private void buildMenuTree(List<SysMenu> rootMenu, List<SysMenu> fullMenu){
        for (SysMenu menu : rootMenu) {
            if(menu.getLeaf() == Constant.BOOLEAN_NO){
                menu.setChildren(fullMenu.stream().filter(node -> node.getParentId().equals(menu.getId()))
                        .sorted(Comparator.comparingInt(SysMenu::getSort))
                        .collect(Collectors.toList()));
                if(CollectionUtils.isNotEmpty(menu.getChildren())){
                    buildMenuTree(menu.getChildren(), fullMenu);
                }
            }else{
                menu.setAuth(fullMenu.stream().filter(node -> node.getParentId().equals(menu.getId()))
                        .map(node -> {
                            AuthVo auth = new AuthVo();
                            auth.setValue(node.getPermission());
                            auth.setLabel(node.getName());

                            return auth;
                        })
                        .collect(Collectors.toList()));
            }
        }
    }

    /**
     * 获取菜单下拉框列表
     * @return
     */
    public TreeSelect getTreeSelect(){
        TreeSelect treeSelect = new TreeSelect();
        treeSelect.setKey("0");
        treeSelect.setValue("0");
        treeSelect.setTitle("一级菜单");
        treeSelect.setChildren(initTreeSelect(getMenuFullTree(null)));
        return treeSelect;
    }

    private List<TreeSelect> initTreeSelect(List<SysMenu> menuList){
        List<TreeSelect> treeSelectList = new ArrayList<>();
        for (SysMenu menu : menuList) {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setKey(menu.getId().toString());
            treeSelect.setValue(treeSelect.getKey());
            treeSelect.setTitle(menu.getName());
            if(menu.getLeaf() == Constant.BOOLEAN_NO){
                treeSelect.setChildren(initTreeSelect(menu.getChildren()));
            }
            treeSelectList.add(treeSelect);
        }
        return treeSelectList;
    }

    @Transactional
    @Override
    public boolean save(SysMenu menu) {
        menu.setId(IdWorker.getId());
        if(menu.getParentId() == 0){
            // 根节点
            menu.setLevel(1);
            menu.setParentIds(buildParentIds(null, menu.getId()));
            super.save(menu);
        }else{
            SysMenu parentMenu = getById(menu.getParentId());
            if(parentMenu == null){
                throw new JackerooException("上级菜单不存在");
            }
            menu.setLevel(parentMenu.getLevel() + 1);
            menu.setParentIds(buildParentIds(parentMenu.getParentIds(), menu.getId()));
            super.save(menu);
        }

        if(CollectionUtils.isNotEmpty(menu.getAuth())){
            List<SysMenu> authList = getAuthMenuList(menu.getAuth(), menu);
            if(authList.size() > 0){
                super.saveBatch(authList);
            }
        }

        return true;
    }

    private String buildParentIds(String parentIds, Long parentId){
        return (parentIds == null ? "" : parentIds) + Constant.SPLIT_SLASH + parentId;
    }

    private List<SysMenu> getAuthMenuList(List<AuthVo> voList, SysMenu menu){
        List<SysMenu> authList = new ArrayList<>();
        for (int i = 0; i < voList.size(); i++) {
            AuthVo auth = voList.get(i);
            if(StringUtils.isNotBlank(auth.getValue())){
                SysMenu authMenu = new SysMenu();

                authMenu.setParentId(menu.getId());
                authMenu.setParentIds(buildParentIds(menu.getParentIds(), menu.getId()));
                authMenu.setLevel(menu.getLevel() + 1);
                authMenu.setName(auth.getLabel());
                authMenu.setSort((i+ 1) * 10);
                authMenu.setPermission(menu.getGroup() + ":" + auth.getValue());
                authMenu.setType(SysMenu.TYPE_PERMISSION);

                authList.add(authMenu);
            }
        }
        return authList;
    }

    /**
     * 获取菜单详情
     * @param id
     * @return
     */
    public SysMenu getMenuDetail(String id){
        SysMenu menu = super.getById(id);
        if(menu == null){
            return null;
        }
        if(menu.getLeaf() != null && menu.getLeaf() == Constant.BOOLEAN_YES){
            LambdaQueryWrapper<SysMenu> query = new LambdaQueryWrapper<>();
            query.eq(SysMenu::getParentId, menu.getId());
            query.eq(SysMenu::getDelFlag, Constant.DEL_FLAG_NORMAL);

            List<SysMenu> authList = super.list(query);
            if(CollectionUtils.isNotEmpty(authList)){
                menu.setAuth(authList.stream().map(item -> {
                    AuthVo auth = new AuthVo();
                    auth.setLabel(item.getName());
                    auth.setValue(item.getPermission());
                    return auth;
                }).collect(Collectors.toList()));
            }
        }

        return menu;
    }

    /**
     * 更新菜单
     * @param menu
     */
    public void update(SysMenu menu){
        if(CollectionUtils.isNotEmpty(menu.getAuth())){
            for (AuthVo authVo : menu.getAuth()) {
                authVo.setValue(menu.getGroup() + ":" + authVo.getValue());
            }

            SysMenu sysMenu = super.getById(menu.getId());
            if(sysMenu != null){
                menu.setLevel(sysMenu.getLevel());
                menu.setParentIds(sysMenu.getParentIds());
            }

            LambdaQueryWrapper<SysMenu> query = new LambdaQueryWrapper<>();
            query.eq(SysMenu::getParentId, menu.getId());
            query.eq(SysMenu::getDelFlag, Constant.DEL_FLAG_NORMAL);

            List<AuthVo> existAuthList = super.list(query)
                    .stream()
                    .map(item -> new AuthVo(item.getPermission(), item.getName()))
                    .collect(Collectors.toList());

            //新增的
            List<AuthVo> addAuthList = menu.getAuth().stream().filter(item -> !existAuthList.contains(item)).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(addAuthList)){
                List<SysMenu> authList = getAuthMenuList(addAuthList, menu);
                super.saveBatch(authList);
            }

            //删除的
            List<AuthVo> deleteAuthList = existAuthList.stream().filter(item -> !menu.getAuth().contains(item)).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(deleteAuthList)){
                LambdaQueryWrapper<SysMenu> remove = new LambdaQueryWrapper<>();
                remove.eq(SysMenu::getParentId, menu.getId());
                remove.in(SysMenu::getPermission, deleteAuthList.stream().map(item -> item.getValue()).collect(Collectors.toList()));

                super.remove(remove);
            }
        }

        super.updateById(menu);
    }
}
