package cn.hub.jackeroo.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 自定义配置的RedisTemplate工具类
 * 该RedisTemplate仅配置了String类型，但同时也提供了collection、map和object对象转化为json字符串作为value的存储和读取
 * 默认调用set、setList、setMap、setObject方法时，默认指定了redis的过期时间，过期时间通过application.yml配置
 * @author alex
 * @date 2020/11/20
 */
@Component
public class RedisService{

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 默认过期时长，单位：秒
     */
    @Value("${spring.redis-template.timeout}")
    private long DEFAULT_EXPIRE;
    /**
     * 默认过期时间单位，s
     */
    private TimeUnit DEFAULT_UNIT = TimeUnit.SECONDS;

    /**
     * 是否存在key
     * @param key
     * @return
     */
    public boolean existKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 重名名key，如果newKey已经存在，则newKey的原值被覆盖
     *
     * @param oldKey
     * @param newKey
     */
    public void renameKey(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * newKey不存在时才重命名
     *
     * @param oldKey
     * @param newKey
     * @return 修改成功返回true
     */
    public boolean renameKeyNotExist(String oldKey, String newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 删除key
     *
     * @param key
     */
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 删除多个key
     *
     * @param keys
     */
    public void deleteKey(String... keys) {
        redisTemplate.delete(Arrays.asList(keys));
    }

    /**
     * 删除Key的集合
     *
     * @param keys
     */
    public void deleteKey(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 设置key的生命周期
     *
     * @param key
     * @param time
     * @param timeUnit
     */
    public void expireKey(String key, long time, TimeUnit timeUnit) {
        redisTemplate.expire(key, time, timeUnit);
    }

    /**
     * 指定key在指定的日期过期
     *
     * @param key
     * @param date
     */
    public void expireKeyAt(String key, Date date) {
        redisTemplate.expireAt(key, date);
    }

    /**
     * 查询key的生命周期
     *
     * @param key
     * @param timeUnit
     * @return
     */
    public long getKeyExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 将key设置为永久有效
     *
     * @param key
     */
    public void persistKey(String key) {
        redisTemplate.persist(key);
    }

    /**
     * 设置key的值，并指定默认时长
     * @param key
     * @param value
     */
    public void set(String key, String value){
        redisTemplate.opsForValue().set(key, value, DEFAULT_EXPIRE, DEFAULT_UNIT);
    }

    /**
     * 设置key的值，并指定时长
     * @param key
     * @param value
     * @param timeout
     */
    public void set(String key, String value, long timeout){
        redisTemplate.opsForValue().set(key, value, timeout, DEFAULT_UNIT);
    }

    /**
     * 获取key的值
     * @param key
     * @return
     */
    public String get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置key的值为集合，并指定默认时长
     * @param key
     * @param value
     */
    public void setList(String key, List value){
        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(value), DEFAULT_EXPIRE, DEFAULT_UNIT);
    }

    /**
     * 获取key的值为集合
     * @param key
     * @param clazz
     * @param <E>
     * @return
     */
    public <E> List<E> getList(String key, Class<E> clazz){
        return JSONObject.parseArray(redisTemplate.opsForValue().get(key), clazz);
    }

    /**
     * 设置key的值为map，并指定默认时长
     * @param key
     * @param value
     */
    public void setMap(String key, Map value){
        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(value), DEFAULT_EXPIRE, DEFAULT_UNIT);
    }

    /**
     * 获取key的值为map
     * @param key
     * @param clazz
     * @param <E>
     * @return
     */
    public <E> Map<String, E> getMap(String key, Class<E> clazz){
        return (Map<String, E>) JSONObject.parseObject(redisTemplate.opsForValue().get(key), Map.class);
    }

    /**
     * 设置key的值为Object
     * @param key
     * @param value
     */
    public void setObject(String key, Object value){
        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(value), DEFAULT_EXPIRE, DEFAULT_UNIT);
    }

    /**
     * 获取key的值为Object
     * @param key
     * @param clazz
     * @param <E>
     * @return
     */
    public <E> E getObject(String key, Class<E> clazz){
        return JSONObject.parseObject(redisTemplate.opsForValue().get(key), clazz);
    }
}
