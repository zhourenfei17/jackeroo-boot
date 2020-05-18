package cn.hub.jackeroo.system.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
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
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 姓名
     */
    @TableField("NAME")
    private String name;

    /**
     * 员工号
     */
    @TableField("CODE")
    private String code;

    /**
     * 登录帐号
     */
    private String account;

    /**
     * 登录密码
     */
    @TableField("PASSWORD")
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
    private LocalDate birthday;

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
    @TableField("STATUS")
    private Integer status;

    /**
     * 删除标识
     */
    private Integer delFlag;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
