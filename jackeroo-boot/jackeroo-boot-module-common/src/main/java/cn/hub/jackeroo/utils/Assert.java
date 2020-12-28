package cn.hub.jackeroo.utils;

import cn.hub.jackeroo.exception.JackerooException;

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
}
