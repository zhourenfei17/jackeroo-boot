package cn.hub.jackeroo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author alex
 * @date 2020/05/13
 */
@Data
public class LoginUser extends IUser implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 姓名
     */
    private String name;

    /**
     * 员工号
     */
    private String code;

    /**
     * 登录帐号
     */
    private String account;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 动态盐
     */
    private String salt;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 手机
     */
    private String phone;

    /**
     * 座机
     */
    private String telephone;

    /**
     * 冻结状态(1-正常，2-冻结)
     */
    private Integer status;

    /**
     * 删除标识
     */
    private Integer delFlag;
}
