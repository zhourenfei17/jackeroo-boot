package cn.hub.jackeroo.system.mapper;

import cn.hub.jackeroo.system.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 角色信息 Mapper 接口
 * </p>
 *
 * @author jackeroo
 * @since 2020-05-18
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> findList(SysRole sysRole);
}
