package cn.hub.jackeroo.utils;

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
