package cn.hub.jackeroo.system.entity;

import cn.hub.jackeroo.persistence.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author jackeroo
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

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
