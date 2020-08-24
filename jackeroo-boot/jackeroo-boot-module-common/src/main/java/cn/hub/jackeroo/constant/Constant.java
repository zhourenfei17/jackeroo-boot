package cn.hub.jackeroo.constant;


/**
 * 全局常量
 * @author alex
 * @date 2020/05/25
 */
public interface Constant {
    /**
     * 删除标识 - 正常
     */
    int DEL_FLAG_NORMAL = 0;
    /**
     * 删除标识 - 已删除
     */
    int DEL_FLAG_DELETE = 1;
    /**
     * 用户状态 - 正常
     */
    int USER_STATUS_NORMAL = 0;
    /**
     * 用户状态 - 冻结
     */
    int USER_STATUS_FROZEN = 1;
    /**
     * 自定义系统异常代码
     */
    int SYSTEM_ERROR_CODE = 5000;
    /**
     * 是
     */
    int BOOLEAN_YES = 1;
    /**
     * 否
     */
    int BOOLEAN_NO = 0;
    /**
     * 分隔符-逗号
     */
    String SPLIT_COMMA = ",";
    /**
     * 分隔符-斜杠
     */
    String SPLIT_SLASH = "/";
}
