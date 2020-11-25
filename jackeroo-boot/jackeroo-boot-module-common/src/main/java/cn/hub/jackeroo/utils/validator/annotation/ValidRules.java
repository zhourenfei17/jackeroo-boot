package cn.hub.jackeroo.utils.validator.annotation;

import cn.hub.jackeroo.enums.ValidRuleType;
import cn.hub.jackeroo.utils.validator.annotation.validator.ValidRulesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

/**
 * String类型字段的自定义校验规则验证注解类
 * @author alex
 * @date 2020/11/25
 */
@Constraint(validatedBy = {ValidRulesValidator.class})
@Target({METHOD, FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRules {
    /**
     * 验证规则
     * @return
     */
    ValidRuleType[] type();

    String message() default "格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
