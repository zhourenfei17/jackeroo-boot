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
    /**
     * Shiro缓存key
     */
    String USER_CACHE = "SYS:CACHE:USER:";
    /**
     * Session key
     */
    String USER_SESSION = "SYS:SESSION:USER:";
    /**
     * 同一用户在线的sessionId集合
     */
    String SAME_USER_SESSION_LIST = "SYS:SESSION:ONLINE:";
}
