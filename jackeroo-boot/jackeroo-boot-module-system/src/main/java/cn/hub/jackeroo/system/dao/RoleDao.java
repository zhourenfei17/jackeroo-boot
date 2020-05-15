package cn.hub.jackeroo.system.dao;

import cn.hub.jackeroo.system.entity.Role;
import cn.hub.jackeroo.system.entity.RoleMenu;
import cn.hub.jackeroo.persistence.CrudDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统角色DAO接口
 * @author
 * @version 2018-11-14
 */
public interface RoleDao extends CrudDao<Role, Long> {
    /**
     * 通过用户id获取用户拥有的角色
     * @param userId
     * @return
     */
    List<Role> findByUserId(Long userId);

    /**
     * @Description: 根据角色Id和菜单Id删除角色菜单关系
     * @param roleId
     * @param delMenus
     * @return
     * @author
     * @date 2016年9月21日
     */
    int delRoleMenu(@Param("roleId") Long roleId, @Param("delMenus") String delMenus);

    /**
     * @Description: 批量新增角色菜单关系
     * @param list
     * @return
     * @author
     * @date 2016年9月21日
     */
    int addRoleMenu(List<RoleMenu> list);

    /**
     * 检测角色名是否重复
     * @param role
     * @return
     */
    int checkRoleName(Role role);
}