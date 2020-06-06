package cn.hub.jackeroo.system.entity;

import cn.hub.jackeroo.utils.groups.First;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

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

    @NotNull(groups = First.class)
    @TableId
    private Long id;
    /**
     * 姓名
     */
    @NotBlank
    @Length(max = 20)
    private String name;

    /**
     * 员工号
     */
    @Length(max = 60)
    private String code;

    /**
     * 登录帐号
     */
    @NotBlank
    @Length(max = 30)
    private String account;

    /**
     * 登录密码
     */
    @NotBlank
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
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String createBy;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
    /**
     * 更新人
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
