package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.system.entity.SysUserRole;
import cn.hub.jackeroo.system.mapper.SysUserRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * <p>
 * 用户角色 服务实现类
 * </p>
 *
 * @author jackeroo
 * @since 2020-05-18
 */
@Service
public class SysUserRoleService extends ServiceImpl<SysUserRoleMapper, SysUserRole> {

    /**
     * 判断角色是否正在使用
     * @param roleId
     * @return
     */
    public boolean existRole(String roleId){
        LambdaQueryWrapper<SysUserRole> query = new LambdaQueryWrapper<>();
        query.eq(SysUserRole::getRoleId, roleId);

        return super.count(query) > 0;
    }

    /**
     * 通过用户id获取用户角色关系
     * @param userId
     * @return
     */
    public SysUserRole getByUserId(Serializable userId){
        LambdaQueryWrapper<SysUserRole> query = new LambdaQueryWrapper<>();
        query.eq(SysUserRole::getUserId, userId);

        return super.getOne(query, false);
    }

    /**
     * 根据用户id删除
     * @param userId
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteByUserId(String userId){
        LambdaQueryWrapper<SysUserRole> query = new LambdaQueryWrapper<>();
        query.eq(SysUserRole::getUserId, userId);

        super.remove(query);
    }
}
