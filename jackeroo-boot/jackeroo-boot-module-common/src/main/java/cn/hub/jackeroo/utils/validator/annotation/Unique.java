package cn.hub.jackeroo.utils.validator.annotation;

import cn.hub.jackeroo.utils.validator.annotation.validator.CodeNumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

/**
 * 唯一校验注解
 * @author alex
 * @date 2020/06/30
 */
@Target({METHOD, FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Unique {
    String value() default "";

    String name();

    String message() default "不能重复";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
