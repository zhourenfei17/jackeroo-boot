package cn.hub.jackeroo.enums;

/**
 * 自定义验证规则的验证类型
 * @author alex
 * @date 2020/11/25
 */
public enum ValidRuleType {
    /**
     * 验证手机号
     */
    VALID_MOBILE,
    /**
     * 验证电话号码
     */
    VALID_TELEPHONE,
    /**
     * 验证邮编
     */
    VALID_POSTCDOE,
    /**
     * 验证身份证号
     */
    VALID_ID_CARD,
    /**
     * 验证字母和下划线
     */
    VALID_LETTER_AND_UNDERLINE
}
