package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.system.entity.SysMenu;
import cn.hub.jackeroo.system.mapper.SysMenuMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
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
}
