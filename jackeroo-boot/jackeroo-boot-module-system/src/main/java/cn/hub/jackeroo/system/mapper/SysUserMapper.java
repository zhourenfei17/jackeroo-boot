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

    /**
     * 通过账号获取用户详细信息
     * @param account
     * @return
     */
    SysUser findByAccount(String account);

    /**
     * 通过用户id获取用户详情
     * @param userId
     * @return
     */
    SysUser findById(Long userId);
}
