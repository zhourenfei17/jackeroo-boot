package cn.hub.jackeroo.system.mapper;

import cn.hub.jackeroo.system.entity.SysMenuPermissionConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 菜单权限配置 Mapper 接口
 * </p>
 *
 * @author jackeroo
 * @since 2020-08-27
 */
public interface SysMenuPermissionConfigMapper extends BaseMapper<SysMenuPermissionConfig> {

    List<SysMenuPermissionConfig> findList(SysMenuPermissionConfig sysMenuPermissionConfig);
}
