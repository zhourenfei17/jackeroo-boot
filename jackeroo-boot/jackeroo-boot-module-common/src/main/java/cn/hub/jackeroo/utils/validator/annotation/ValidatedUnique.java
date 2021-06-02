package cn.hub.jackeroo.utils.validator.annotation;

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

    /**
     * 数据库表名
     * @return
     */
    String tableName() default "";

    /**
     * 指定校验的分组
     * @see Unique groups()
     * @return
     */
    Class<?> groups() default Null.class;

    /**
     * 额外的唯一性条件
     * @return
     */
    String condition() default "";

    /**
     * 主键
     * @return
     */
    String primaryKey() default "id";
}
