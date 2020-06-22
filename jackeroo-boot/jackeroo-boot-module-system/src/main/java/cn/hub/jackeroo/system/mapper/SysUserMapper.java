package cn.hub.jackeroo.system.mapper;

import cn.hub.jackeroo.system.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 用户信息 Mapper 接口
 * </p>
 *
 * @author jackeroo
 * @since 2020-05-18
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 查询用户列表
     * @param sysUser
     * @return
     */
    List<SysUser> findList(SysUser sysUser);
}
