package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.enums.ResultStatusCode;
import cn.hub.jackeroo.exception.JackerooException;
import cn.hub.jackeroo.system.entity.SysUser;
import cn.hub.jackeroo.system.mapper.SysUserMapper;
import cn.hub.jackeroo.utils.PasswordUtil;
import cn.hub.jackeroo.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


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

    /**
     * 通过登录账号获取用户信息
     * @param account
     * @return
     */
    public SysUser findByAccount(String account){
        LambdaQueryWrapper<SysUser> query = new LambdaQueryWrapper<>();
        query.eq(SysUser::getAccount, account);

        SysUser user = super.getOne(query);

        return user;
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
    }

}
