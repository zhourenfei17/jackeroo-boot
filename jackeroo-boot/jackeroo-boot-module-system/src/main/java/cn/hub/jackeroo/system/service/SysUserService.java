package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.system.entity.SysUser;
import cn.hub.jackeroo.system.mapper.SysUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


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
}
