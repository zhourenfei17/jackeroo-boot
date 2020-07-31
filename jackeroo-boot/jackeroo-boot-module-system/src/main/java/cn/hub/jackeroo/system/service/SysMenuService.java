package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.system.entity.SysMenu;
import cn.hub.jackeroo.system.mapper.SysMenuMapper;
import cn.hub.jackeroo.system.vo.TreeSelect;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
    public List<SysMenu> getMenuFullTree(){
        List<SysMenu> menuList = super.list();

        List<SysMenu> rootTreeNode = menuList.stream().filter(node -> node.getParentId().equals(0L)).collect(Collectors.toList());
        buildMenuTree(rootTreeNode, menuList);

        return rootTreeNode;
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
                menu.setIsLeaf(Constant.BOOLEAN_NO);
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
        treeSelect.setChildren(initTreeSelect(getMenuFullTree()));
        return treeSelect;
    }

    private List<TreeSelect> initTreeSelect(List<SysMenu> menuList){
        List<TreeSelect> treeSelectList = new ArrayList<>();
        for (SysMenu menu : menuList) {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setKey(menu.getId().toString());
            treeSelect.setValue(treeSelect.getKey());
            treeSelect.setTitle(menu.getName());
            if(menu.getIsLeaf() == Constant.BOOLEAN_NO){
                treeSelect.setChildren(initTreeSelect(menu.getChildren()));
            }
            treeSelectList.add(treeSelect);
        }
        return treeSelectList;
    }

}
