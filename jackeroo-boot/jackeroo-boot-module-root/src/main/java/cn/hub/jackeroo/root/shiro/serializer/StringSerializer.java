package cn.hub.jackeroo.root.shiro.serializer;

import org.crazycake.shiro.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author alex
 * @date 2020/11/12
 */
public class StringSerializer extends StringRedisSerializer implements RedisSerializer<String> {

    private String keyPrefix = "";

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    @Override
    public String deserialize(byte[] bytes) {
        return super.deserialize(bytes);
    }

    @Override
    public byte[] serialize(String string) {
        return super.serialize(keyPrefix + string);
    }
}
