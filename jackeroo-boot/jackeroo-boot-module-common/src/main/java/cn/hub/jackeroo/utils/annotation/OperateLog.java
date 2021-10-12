package cn.hub.jackeroo.utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志用注解
 * @author alex
 * @date 2021/10/11
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperateLog {
    /**
     * 操作主体内容
     * @return
     */
    String value();

    /**
     * 系统操作日志操作类型
     * @return
     */
    String type();
}
