package cn.hub.jackeroo.utils.validator.annotation;

import cn.hub.jackeroo.utils.validator.annotation.validator.ForEachPatternValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

/**
 * 用于验证数组、集合等数据，必须每个元素都要符合条件
 * 集合或者数组必须是基本数据类型或者其封装类
 * @author alex
 * @date 2020/10/23
 */
@Constraint(validatedBy = {ForEachPatternValidator.class})
@Target({METHOD, FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ForEachPattern {
    /**
     * 正则表达式
     * @return
     */
    String regexp();

    String message() default "不正确";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
