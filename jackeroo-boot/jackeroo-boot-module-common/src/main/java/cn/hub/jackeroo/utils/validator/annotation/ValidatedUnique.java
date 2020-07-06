package cn.hub.jackeroo.utils.validator.annotation;

import javax.validation.Payload;
import javax.validation.constraints.Null;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * controller层方法注解，用于校验提交数据中的唯一字段
 * @author alex
 * @date 2020/07/06
 */
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatedUnique {

    String value() default "";

    /**
     * 指定DataEntity校验类（只对包含@Unique注解的类起作用）
     * @return
     */
    Class<?> clazz();

    Class<?> groups() default Null.class;
}
