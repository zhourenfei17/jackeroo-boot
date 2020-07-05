package cn.hub.jackeroo.utils.validator.annotation;

import cn.hub.jackeroo.utils.validator.annotation.validator.CodeNumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 号码/编码验证注解，手机号、电话号、邮编号、身份证号
 * @author alex
 * @date 2020/06/29
 */
@Constraint(validatedBy = {CodeNumValidator.class})
@Target({METHOD, FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CodeNum {
    int type();

    String message() default "号码不正确";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
