package cn.hub.jackeroo.system.mapper;

import cn.hub.jackeroo.system.entity.SysMenuPermissionGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 菜单权限配置组别 Mapper 接口
 * </p>
 *
 * @author jackeroo
 * @since 2020-08-27
 */
public interface SysMenuPermissionGroupMapper extends BaseMapper<SysMenuPermissionGroup> {

    List<SysMenuPermissionGroup> findList(SysMenuPermissionGroup sysMenuPermissionGroup);
}
