package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.api.ISystemApi;
import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.enums.ResultStatusCode;
import cn.hub.jackeroo.exception.JackerooException;
import cn.hub.jackeroo.system.entity.SysUser;
import cn.hub.jackeroo.system.entity.SysUserRole;
import cn.hub.jackeroo.system.mapper.SysUserMapper;
import cn.hub.jackeroo.utils.Assert;
import cn.hub.jackeroo.utils.UserUtils;
import cn.hub.jackeroo.utils.encrypt.MD5Util;
import cn.hub.jackeroo.utils.encrypt.PasswordUtil;
import cn.hub.jackeroo.utils.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author jackeroo
 * @since 2020-05-18
 */
@Service
@RequiredArgsConstructor
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {

    private final SysUserRoleService userRoleService;
    private final ISystemApi systemApi;

    /**
     * 查询数据列表
     * @param sysUser
     * @return
     */
    public List<SysUser> findList(SysUser sysUser){
        sysUser.setDelFlag(Constant.DEL_FLAG_NORMAL);
        return getBaseMapper().findList(sysUser);
    }
    /**
     * 通过登录账号获取用户信息
     * @param account
     * @return
     */
    public SysUser findByAccount(String account){
        SysUser user = getBaseMapper().findByAccount(account);

        return user;
    }

    /**
     * 通过用户id获取用户信息
     * @param userId
     * @return
     */
    public SysUser findById(Serializable userId){
        return getBaseMapper().findById(userId);
    }

    /**
     *  保存用户
     * @param user
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void insertUser(SysUser user){
        if(findByAccount(user.getAccount()) != null){
            throw new JackerooException(ResultStatusCode.EXIST_SAME_ACCOUNT);
        }
        String salt = StringUtils.randomGen(8);
        user.setSalt(salt);
        String passwordEncode = PasswordUtil.encrypt(user.getAccount(), user.getPassword(), salt);
        user.setPassword(passwordEncode);
        user.setDelFlag(Constant.DEL_FLAG_NORMAL);
        user.setStatus(0);
        super.save(user);

        // 保存用户角色关系
        SysUserRole userRole = new SysUserRole();
        userRole.setRoleId(user.getRoleId());
        userRole.setUserId(user.getId());
        userRoleService.save(userRole);
    }

    /**
     * 更新用户信息
     * @param user
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateUser(SysUser user){
        // 编辑用户无法修改密码和账号
        user.setPassword(null);
        user.setAccount(null);

        super.updateById(user);

        // 保存用户角色关系
        SysUserRole userRole = userRoleService.getByUserId(user.getId());
        if(userRole == null){
            userRole = new SysUserRole();
        }
        userRole.setRoleId(user.getRoleId());
        userRole.setUserId(user.getId());
        userRoleService.saveOrUpdate(userRole);
    }

    /**
     * 冻结用户
     * @param id
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void frozenUser(String ...id){
        for (String userId : id) {
            SysUser sysUser = getById(userId);
            if(sysUser != null && sysUser.getStatus() == Constant.USER_STATUS_NORMAL){
                sysUser.setStatus(Constant.USER_STATUS_FROZEN);

                super.updateById(sysUser);

                systemApi.kickOutUser(userId);
            }
        }
    }

    /**
     * 解冻用户
     * @param id
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void unfrozenUser(String ...id){
        for (String userId : id) {
            SysUser sysUser = getById(userId);
            if(sysUser != null && sysUser.getStatus() == Constant.USER_STATUS_FROZEN){
                sysUser.setStatus(Constant.USER_STATUS_NORMAL);

                super.updateById(sysUser);
            }
        }
    }

    /**
     * 重置密码，默认密码为手机号后6位
     * @param id
     */
    public void resetPassword(String id){
        SysUser sysUser = getById(id);
        if(sysUser != null){
            Assert.notEmpty(sysUser.getPhone(), "重置密码失败");

            String passwordEncode = PasswordUtil.encrypt(sysUser.getAccount(),
                    MD5Util.encrypt(sysUser.getPhone().substring(sysUser.getPhone().length() - 6)), sysUser.getSalt());
            sysUser.setPassword(passwordEncode);

            super.updateById(sysUser);
        }
    }

    /**
     * 修改当前登录用户密码
     * @param pwd
     */
    public void changeLoginUserPwd(String pwd){
        SysUser user = getById(UserUtils.getUser().getId());
        if(user != null){
            String passwordEncode = PasswordUtil.encrypt(user.getAccount(), pwd, user.getSalt());
            user.setPassword(passwordEncode);

            super.updateById(user);
        }
    }
}
