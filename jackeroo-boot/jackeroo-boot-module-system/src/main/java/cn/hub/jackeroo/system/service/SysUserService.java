package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.enums.ResultStatusCode;
import cn.hub.jackeroo.exception.JackerooException;
import cn.hub.jackeroo.system.entity.SysUser;
import cn.hub.jackeroo.system.entity.SysUserRole;
import cn.hub.jackeroo.system.mapper.SysUserMapper;
import cn.hub.jackeroo.utils.Assert;
import cn.hub.jackeroo.utils.PasswordUtil;
import cn.hub.jackeroo.utils.StringUtils;
import cn.hub.jackeroo.vo.PageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author jackeroo
 * @since 2020-05-18
 */
@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {

    @Resource
    private SysUserMapper mapper;
    @Autowired
    private SysUserRoleService userRoleService;

    /**
     * 查询数据列表-带分页
     * @param sysUser
     * @return
     */
    public IPage<SysUser> findPage(SysUser sysUser, PageParam pageParam){
        sysUser.setDelFlag(Constant.DEL_FLAG_NORMAL);

        Page<SysUser> page = sysUser.initPage(pageParam);
        page.setRecords(mapper.findList(sysUser));

        return page;
    }
    /**
     * 通过登录账号获取用户信息
     * @param account
     * @return
     */
    public SysUser findByAccount(String account){
        SysUser user = mapper.findByAccount(account);

        return user;
    }

    /**
     * 通过用户id获取用户信息
     * @param userId
     * @return
     */
    public SysUser findById(Long userId){
        return mapper.findById(userId);
    }

    /**
     *  保存用户
     * @param user
     */
    @Transactional
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
    @Transactional
    public void updateUser(SysUser user){
        // 编辑用户无法修改密码和账号
        user.setPassword(null);
        user.setAccount(null);

        super.updateById(user);

        // 保存用户角色关系
        SysUserRole userRole = new SysUserRole();
        userRole.setRoleId(user.getRoleId());
        userRole.setUserId(user.getId());
        userRoleService.saveOrUpdate(userRole);
    }

    /**
     * 冻结用户
     * @param id
     */
    public void frozenUser(String id){
        SysUser sysUser = getById(id);
        if(sysUser != null){
            sysUser.setStatus(Constant.USER_STATUS_FROZEN);

            super.updateById(sysUser);

            // TODO 冻结用户需要清除用户的redis缓存，避免用户仍然在登录状态
        }
    }

    /**
     * 解冻用户
     * @param id
     */
    public void unfrozenUser(String id){
        SysUser sysUser = getById(id);
        if(sysUser != null){
            sysUser.setStatus(Constant.USER_STATUS_NORMAL);

            super.updateById(sysUser);
        }
    }

    /**
     * 重置密码，默认密码为手机号后6位
     * @param id
     */
    public void resetPassword(String id){
        SysUser sysUser = getById(id);
        if(sysUser != null){
            Assert.isEmpty(sysUser.getPhone(), "重置密码失败");

            String passwordEncode = PasswordUtil.encrypt(sysUser.getAccount(),
                    sysUser.getPhone().substring(sysUser.getPhone().length() - 6), sysUser.getSalt());
            sysUser.setPassword(passwordEncode);

            super.updateById(sysUser);
        }
    }
}
