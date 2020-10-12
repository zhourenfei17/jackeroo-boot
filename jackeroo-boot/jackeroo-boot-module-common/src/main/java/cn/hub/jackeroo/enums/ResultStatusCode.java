package cn.hub.jackeroo.enums;

/**
 * @param
 * @author
 * @description 错误代码
 * 1.错误码为0表示陈宫，其他都表示错误
 * 2.错误码长度为3位的是通用错误码，常规的如400、401、404、500
 * 3.错误码长度为5位的是系统自定义错误码；建议统一格式：ABBCC
 *      A:错误级别，如1代表系统级别错误，4代表参数校验错误，5代表业务级别错误
 *      B:项目或者模块名称对应的编码
 *      C:具体的错误编号，可从01开始自增，如果不够用可以扩展为3位
 * @date 2018/1/5
 * @return
 */
public enum ResultStatusCode {
    OK(0, "OK"),
    BAD_REQUEST(400, "参数解析失败"),
    INVALID_TOKEN(401, "无效的Access-Token"),
    METHOD_NOT_ALLOWED(405, "不支持当前请求方法"),
    SYSTEM_ERR(500, "服务器运行异常"),
    UNAUTHO_ERROR(10001, "您没有该权限"),
    UPLOAD_ERROR(10002, "上传文件异常"),
    CAPTCHA_ERROR(10003, "验证码错误"),
    BUSSINESS_ERROR(50000, "业务异常"),
    NOT_EXIST_USER_OR_ERROR_PWD(50101, "该用户不存在或密码错误"),
    LOGINED_IN(50102, "该用户已登录"),
    USER_FROZEN(50103, "该用户已被冻结"),
    SHIRO_ERROR(50104, "登录异常"),
    INVALID_CAPTCHA(50105, "无效的验证码"),
    EXIST_SAME_ACCOUNT(50106, "该账号已存在");

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private ResultStatusCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
