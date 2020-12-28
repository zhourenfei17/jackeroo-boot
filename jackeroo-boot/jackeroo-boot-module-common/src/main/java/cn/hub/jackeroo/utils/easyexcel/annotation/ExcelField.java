package cn.hub.jackeroo.utils.easyexcel.annotation;

import cn.hub.jackeroo.utils.easyexcel.annotation.enums.ExcelType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * EasyExcel自定义注解
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {

	/**
	 * 导出字段标题（需要添加批注请用“**”分隔，标题**批注，仅对导出模板有效）
	 */
	String title();
	
	/**
	 * 字段类型，默认为导入、导出
	 */
	ExcelType type() default ExcelType.ALL;

	/**
	 * 水平对齐方式，默认对齐方式为：文本类型居左，数字、日期、时间类型居右，布尔类型居中
	 */
    HorizontalAlignment align() default HorizontalAlignment.GENERAL;
	
	/**
	 * 导出字段字段排序（升序）
	 */
	int sort();

    /**
     * 列宽，以字符宽度的1/256为单位
     * @see org.apache.poi.ss.usermodel.Sheet setColumnWidth()
     * @return
     */
	int width() default 20;

	/**
	 * 如果是字典类型，请设置字典的type值
	 */
	String dictType() default "";

	String dateFormat() default "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 反射类型
	 */
	Class<?> fieldType() default Class.class;
	
}
