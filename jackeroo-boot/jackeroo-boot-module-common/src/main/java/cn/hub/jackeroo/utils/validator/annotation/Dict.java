package cn.hub.jackeroo.utils.validator.annotation;

import cn.hub.jackeroo.utils.validator.annotation.validator.DictValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

/**
 * 数据字典注解
 * @author alex
 * @date 2020/11/13
 */
@Constraint(validatedBy = {DictValidator.class})
@Target({METHOD, FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Dict {

    String dictCode();

    String message() default "值不在范围内";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
