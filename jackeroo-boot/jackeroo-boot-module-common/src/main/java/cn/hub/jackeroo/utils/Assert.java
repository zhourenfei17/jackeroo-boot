package cn.hub.jackeroo.utils;

import cn.hub.jackeroo.exception.JackerooException;

/**
 * @author alex
 * @date 2020/06/19
 */
public abstract class Assert {

    public static void isNull(Object obj, String message){
        if(obj == null){
            throw new JackerooException(message);
        }
    }

    public static void isEmpty(String obj, String message){
        if(obj == null || "".equals(obj)){
            throw new JackerooException(message);
        }
    }
}
