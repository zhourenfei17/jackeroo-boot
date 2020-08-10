package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.exception.JackerooException;
import cn.hub.jackeroo.system.entity.SysMenu;
import cn.hub.jackeroo.system.mapper.SysMenuMapper;
import cn.hub.jackeroo.system.vo.TreeSelect;
import cn.hub.jackeroo.utils.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * 地柜构建树结构
     * @param rootMenu
     * @param fullMenu
     */
    private void buildMenuTree(List<SysMenu> rootMenu, List<SysMenu> fullMenu){
        for (SysMenu menu : rootMenu) {
            menu.setChildren(fullMenu.stream().filter(node -> node.getParentId().equals(menu.getId()))
                    .sorted(Comparator.comparingInt(SysMenu::getSort))
                    .collect(Collectors.toList()));
            if(CollectionUtils.isNotEmpty(menu.getChildren())){
                buildMenuTree(menu.getChildren(), fullMenu);
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
        SysMenu parentMenu = getById(menu.getParentId());
        if(parentMenu == null){
            throw new JackerooException("上级菜单不存在");
        }

        menu.setLevel(parentMenu.getLevel() + 1);
        menu.setParentIds(parentMenu.getParentIds() + "," + parentMenu.getParentId());
        super.save(menu);

        return true;
    }
}
