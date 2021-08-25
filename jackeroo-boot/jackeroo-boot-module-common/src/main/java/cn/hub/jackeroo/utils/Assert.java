package cn.hub.jackeroo.utils;

import cn.hub.jackeroo.exception.JackerooException;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @author alex
 * @date 2020/06/19
 */
public abstract class Assert {

    /**
     * 断言这个boolean必须为true
     * @param expression
     * @param message
     */
    public static void isTrue(boolean expression, String message){
        if(!expression){
            throw new JackerooException(message);
        }
    }

    /**
     * 断言这个boolean必须为false
     * @param expression
     * @param message
     */
    public static void isFalse(boolean expression, String message){
        isTrue(!expression, message);
    }

    /**
     * 断言这个对象必须为null
     * @param obj
     * @param message
     */
    public static void isNull(Object obj, String message){
        isTrue(obj == null, message);
    }

    /**
     * 断言这个对象必须不能为null
     * @param obj
     * @param message
     */
    public static void notNull(Object obj, String message){
        isTrue(obj != null, message);
    }

    /**
     * 断言这个字符串必须为空字符串
     * @param obj
     * @param message
     */
    public static void isEmpty(String obj, String message){
        isTrue(StringUtils.isBlank(obj), message);
    }

    /**
     * 断言这个字符串必须不能为空字符串
     * @param obj
     * @param message
     */
    public static void notEmpty(String obj, String message){
        isTrue(StringUtils.isNotBlank(obj), message);
    }

    /**
     * 断言数组不为空
     * @param array
     * @param message
     */
    public static void notEmpty(Object[] array, String message) {
        isTrue(array != null && array.length > 0, message);
    }

    /**
     * 断言集合不为空
     * @param collection
     * @param message
     */
    public static void notEmpty(Collection collection, String message) {
        isTrue(CollectionUtils.isNotEmpty(collection), message);
    }

    /**
     * 断言map不为空
     * @param map
     * @param message
     */
    public static void notEmpty(Map map, String message) {
        isTrue(map != null && !map.isEmpty(), message);
    }

    /**
     * 断言数组为空
     * @param array
     * @param message
     */
    public static void isEmpty(Object[] array, String message) {
        isFalse(array != null && array.length > 0, message);
    }

    /**
     * 断言集合为空
     * @param collection
     * @param message
     */
    public static void isEmpty(Collection collection, String message) {
        isFalse(CollectionUtils.isNotEmpty(collection), message);
    }

    /**
     * 断言map为空
     * @param map
     * @param message
     */
    public static void isEmpty(Map map, String message) {
        isFalse(map != null && !map.isEmpty(), message);
    }
}
