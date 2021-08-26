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
    VALID_POSTCODE,
    /**
     * 验证身份证号
     */
    VALID_ID_CARD,
    /**
     * 验证字母和下划线
     */
    VALID_LETTER_AND_UNDERLINE,
    /**
     * 验证ip地址
     */
    VALID_IP,
    /**
     * 验证ip和域名
     */
    VALID_IP_HOST,
    /**
     * 验证url网址
     */
    VALID_URL
}
