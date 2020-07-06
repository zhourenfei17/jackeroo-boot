package cn.hub.jackeroo.system.entity;

import cn.hub.jackeroo.constant.CodeType;
import cn.hub.jackeroo.persistence.BaseEntity;
import cn.hub.jackeroo.utils.validator.annotation.CodeNum;
import cn.hub.jackeroo.utils.validator.annotation.Unique;
import cn.hub.jackeroo.utils.validator.groups.Insert;
import cn.hub.jackeroo.utils.validator.groups.Update;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
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
public class SysUser extends BaseEntity<SysUser> {

    private static final long serialVersionUID = 1L;

    @NotNull(groups = Update.class)
    @Null(groups = Insert.class)
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
    @Unique(name = "登录账号", groups = {Insert.class})
    @NotBlank
    @Length(min = 5, max = 30)
    private String account;

    /**
     * 登录密码
     */
    @NotBlank(groups = Insert.class)
    @Null(groups = Update.class)
    private String password;

    /**
     * 动态盐
     */
    @Null
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
    @Unique(name = "手机号", groups = {Insert.class, Update.class})
    @CodeNum(type = CodeType.MOBILE)
    private String phone;

    /**
     * 座机
     */
    @CodeNum(type = CodeType.TELEPHONE)
    private String telephone;

    /**
     * 冻结状态(1-正常，2-冻结)
     */
    @Null
    private Integer status;

    /**
     * 删除标识
     */
    @Null
    private Integer delFlag;
    /**
     * 创建人
     */
    @Null
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    /**
     * 创建时间
     */
    @Null
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 更新人
     */
    @Null
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    /**
     * 更新时间
     */
    @Null
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
