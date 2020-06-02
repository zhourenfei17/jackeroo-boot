package cn.hub.jackeroo.root.shiro;

import cn.hub.jackeroo.system.entity.SysUser;
import org.crazycake.shiro.IRedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author alex
 * @date 2020/05/29
 */
// @Component
public class JackerooRedisManager implements IRedisManager {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public byte[] get(byte[] key) {
        if(key == null){
            return null;
        }
        Object user = redisTemplate.opsForValue().get(new String(key));
        if(user == null){
            return null;
        }else{
            return user.toString().getBytes();
        }
    }

    @Override
    public byte[] set(byte[] key, byte[] value, int expire) {
        redisTemplate.opsForValue().set(new String(key), new String(value), expire);
        return value;
    }

    @Override
    public void del(byte[] key) {

    }

    @Override
    public Long dbSize(byte[] pattern) {
        return null;
    }

    @Override
    public Set<byte[]> keys(byte[] pattern) {
        return null;
    }
}
