package cn.hub.jackeroo.system.dao;


import cn.hub.jackeroo.system.entity.Menu;
import cn.hub.jackeroo.persistence.CrudDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单信息DAO接口
 * @author
 * @version 2018-11-14
 */
public interface MenuDao extends CrudDao<Menu, Integer> {
    /**
     * 通过角色id获取所有权限信息
     * @param roleId
     * @return
     */
    List<Menu> getAllMenuByRoleId(Long roleId);

    List<Menu> findByRoleId(Menu menu);

    List<Menu> findByParentIdsLike(Menu menu);

    int updateParentIds(Menu menu);

    /**
     * 根据pid获取子目录最大排序值
     * @param pid
     * @return
     */
    int getMaxSortByPid(Integer pid);

    /**
     * 根据角色查询菜单列表,查询所有的菜单，标记自己拥有的
     * @param menu
     * @return
     */
    List<Menu> findMenuByRole(Menu menu);
}