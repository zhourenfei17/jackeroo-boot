package cn.hub.jackeroo.system.mapper;

import cn.hub.jackeroo.system.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 菜单信息 Mapper 接口
 * </p>
 *
 * @author jackeroo
 * @since 2020-05-18
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> findPermissionList(SysMenu sysMenu);

    /**
     * 通过角色获取菜单、权限列表
     * @param roleId
     * @return
     */
    List<SysMenu> findMenuByRoleId(Long roleId);
}
