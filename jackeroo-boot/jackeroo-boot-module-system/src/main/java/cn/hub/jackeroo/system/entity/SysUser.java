package cn.hub.jackeroo.system.entity;

import cn.hub.jackeroo.enums.ValidRuleType;
import cn.hub.jackeroo.persistence.BaseEntity;
import cn.hub.jackeroo.system.vo.AuthVo;
import cn.hub.jackeroo.utils.easyexcel.annotation.ExcelField;
import cn.hub.jackeroo.utils.validator.annotation.Dict;
import cn.hub.jackeroo.utils.validator.annotation.Unique;
import cn.hub.jackeroo.utils.validator.annotation.ValidRules;
import cn.hub.jackeroo.utils.validator.groups.Insert;
import cn.hub.jackeroo.utils.validator.groups.Update;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.List;

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
@ApiModel(value = "用户信息")
public class SysUser extends BaseEntity<SysUser> {

    private static final long serialVersionUID = 1L;

    @NotNull(groups = Update.class)
    @Null(groups = Insert.class)
    @TableId
    @ApiModelProperty(value = "用户id")
    private Long id;
    /**
     * 头像
     */
    @Length(max = 255)
    @ApiModelProperty(value = "头像")
    private String avatar;
    /**
     * 姓名
     */
    @NotBlank
    @Length(max = 20)
    @ApiModelProperty(value = "姓名")
    @ExcelField(title = "姓名", sort = 0, width = 50)
    private String name;

    /**
     * 员工号
     */
    @Length(max = 60)
    @ApiModelProperty(value = "员工号")
    @ExcelField(title = "员工号", sort = 1)
    private String code;

    /**
     * 登录帐号
     */
    @Unique(name = "登录账号", groups = {Insert.class})
    @NotBlank
    @Length(min = 1, max = 30)
    @ApiModelProperty(value = "登录账号")
    @ExcelField(title = "登录账号", sort = 2)
    private String account;

    /**
     * 登录密码
     */
    @NotBlank(groups = Insert.class)
    @Null(groups = Update.class)
    @ApiModelProperty(hidden = true)
    private String password;

    /**
     * 动态盐
     */
    @JsonIgnore
    @Null
    @ApiModelProperty(hidden = true)
    private String salt;

    /**
     * 性别
     */
    @Dict(dictCode = "COMMON.SEX")
    @ApiModelProperty(value = "性别")
    @ExcelField(title = "性别", sort = 3, dictType = "COMMON.SEX")
    private Integer gender;

    /**
     * 生日
     */
    @ApiModelProperty(value = "生日")
    @ExcelField(title = "生日", sort = 4, width = 30)
    private String birthday;

    /**
     * 手机
     */
    @Unique(name = "手机号", groups = {Insert.class, Update.class})
    @ValidRules(type = ValidRuleType.VALID_MOBILE)
    @NotBlank
    @ApiModelProperty(value = "手机号")
    @ExcelField(title = "手机", sort = 5, width = 40)
    private String phone;

    /**
     * 座机
     */
    @ValidRules(type = ValidRuleType.VALID_TELEPHONE)
    @ApiModelProperty(value = "座机")
    private String telephone;

    /**
     * 冻结状态(1-正常，2-冻结)
     */
    @Null
    @ApiModelProperty(value = "冻结状态")
    private Integer status;

    /**
     * 删除标识
     */
    @Null
    @ApiModelProperty(hidden = true)
    private Integer delFlag;
    /**
     * 创建人
     */
    @Null
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建时间
     */
    @Null
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    @ExcelField(title = "创建时间", sort = 6)
    private LocalDateTime createTime;
    /**
     * 更新人
     */
    @Null
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 更新时间
     */
    @Null
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    @ExcelField(title = "创建时间", sort = 7)
    private LocalDateTime updateTime;

    /**
     * 角色id
     */
    @NotNull
    @TableField(exist = false)
    private Long roleId;
    @TableField(exist = false)
    private String roleName;
    /**
     * 角色编码
     */
    @TableField(exist = false)
    private String roleCode;
    /**
     * 拥有的权限
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private List<AuthVo> permissionList;
}
