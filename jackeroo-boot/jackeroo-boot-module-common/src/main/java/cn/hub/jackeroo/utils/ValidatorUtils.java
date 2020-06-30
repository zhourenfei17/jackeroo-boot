package cn.hub.jackeroo.utils;

import java.util.regex.Pattern;

/**
 * 验证工具类
 * @author alex
 * @date 2020/06/30
 */
public class ValidatorUtils {

    /**
     * 正则表达式：验证手机号码
     */
    public static final String REGEX_MOBILE = "^1[3456789]\\d{9}$";
    /**
     * 正则表达式：验证电话号码
     */
    public static final String REGEX_TELEPHONE = "^((0[0-9]{2,3}\\-)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)$";

    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 正则表达式：验证汉字
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";

    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x)$";

    /**
     * 正则表达式：验证URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    /**
     * 正则表达式：验证IP地址
     */
    public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
    /**
     * 正则表达式：验证邮政编码
     */
    public static final String REGEX_POST_CODE = "^[1-9]\\\\d{5}$";

    /**
     * 校验手机号
     *
     * @param num
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String num) {
        return Pattern.matches(REGEX_MOBILE, num);
    }

    /**
     * 校验电话号码
     * @param num
     * @return
     */
    public static boolean isTelephone(String num){
        return Pattern.matches(REGEX_TELEPHONE, num);
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 校验汉字
     *
     * @param chinese
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }

    /**
     * 校验身份证
     *
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }

    /**
     * 校验URL
     *
     * @param url
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUrl(String url) {
        return Pattern.matches(REGEX_URL, url);
    }

    /**
     * 校验IP地址
     *
     * @param ipAddr
     * @return
     */
    public static boolean isIPAddr(String ipAddr) {
        return Pattern.matches(REGEX_IP_ADDR, ipAddr);
    }

    /**
     * 校验邮政编码
     * @param num
     * @return
     */
    public static boolean isPostCode(String num){
        return Pattern.matches(REGEX_POST_CODE, num);
    }

}
