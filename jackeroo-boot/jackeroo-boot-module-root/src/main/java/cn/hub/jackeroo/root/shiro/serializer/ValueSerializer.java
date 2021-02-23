package cn.hub.jackeroo.root.shiro.serializer;

import org.crazycake.shiro.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * @author alex
 * @date 2020/11/12
 */
public class ValueSerializer<T> extends Jackson2JsonRedisSerializer<T> implements RedisSerializer<T> {

    public ValueSerializer(Class<T> clazz){
        super(clazz);
    }
}
