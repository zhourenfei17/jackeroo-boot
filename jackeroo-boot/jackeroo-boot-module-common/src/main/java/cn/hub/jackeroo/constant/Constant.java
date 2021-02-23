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
     * 排序-正序
     */
    String SORT_ASC = "ascend";
    /**
     * 排序-倒序
     */
    String SORT_DESC = "descend";
    /**
     * 分隔符-逗号
     */
    String SPLIT_COMMA = ",";
    /**
     * 分隔符-斜杠
     */
    String SPLIT_SLASH = "/";
    /**
     * 分隔符-点
     */
    String SPLIT_DOT = ".";
    /**
     * 分隔符-下划线
     */
    String SPLIT_UNDER_LINE = "_";
    /**
     * 分隔符-中横线
     */
    String SPLIT_MIDDLE_LINE = "-";
    /**
     * 权限标识中的分隔符
     */
    String SPLIT_SECURITY = ":";
}
