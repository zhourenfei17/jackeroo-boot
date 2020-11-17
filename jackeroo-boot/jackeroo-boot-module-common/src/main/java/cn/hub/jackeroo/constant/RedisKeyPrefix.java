package cn.hub.jackeroo.constant;

/**
 *
 * 存储至redis中的key前缀
 *
 * @author alex
 * @date 2020/11/16
 */
public interface RedisKeyPrefix {
    /**
     * 菜单缓存前缀
     */
    String CACHE_MENU = "CACHE:MENU";
    /**
     * 字典项缓存前缀
     */
    String CACHE_DICT = "CACHE:DICT";
}
