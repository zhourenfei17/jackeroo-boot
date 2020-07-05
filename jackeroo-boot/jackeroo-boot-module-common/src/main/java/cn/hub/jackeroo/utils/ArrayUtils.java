package cn.hub.jackeroo.utils;

import org.apache.poi.ss.formula.functions.T;

/**
 * @author alex
 * @date 2020/07/05
 */
public class ArrayUtils {

    public static boolean contains(Object[] arrays, Object obj){
        if(arrays == null){
            return false;
        }
        for (Object array : arrays) {
            if(array.equals(obj)){
                return true;
            }
        }
        return false;
    }
}
