package cn.hub.jackeroo.online.entity;

import cn.hub.jackeroo.persistence.BaseEntity;
import cn.hub.jackeroo.utils.validator.annotation.Dict;
import cn.hub.jackeroo.utils.validator.annotation.Unique;
import cn.hub.jackeroo.utils.validator.groups.Insert;
import cn.hub.jackeroo.utils.validator.groups.Update;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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

/**
 * <p>
 * 数据源配置
 * </p>
 *
 * @author alex
 * @since 2021-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "数据源配置")
@TableName("online_datasource")
public class OnlineDatasource extends BaseEntity<OnlineDatasource> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    @NotNull(groups = Update.class)
    @Null(groups = Insert.class)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 数据源名称
     */
    @NotBlank
    @Length(max = 50)
    @ApiModelProperty(value = "数据源名称")
    @Unique(name = "数据源名称")
    private String name;

    /**
     * 驱动包
     */
    @NotBlank
    @Length(max = 50)
    @Dict(dictCode = "GEN_DATABASE_TYPE")
    @ApiModelProperty(value = "驱动包")
    private String driverClassName;

    /**
     * 连接地址
     */
    @NotBlank
    @Length(max = 500)
    @ApiModelProperty(value = "连接地址")
    private String url;

    /**
     * 用户名
     */
    @NotBlank
    @Length(max = 50)
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 密钥
     */
    @ApiModelProperty(value = "密钥")
    private String slat;

    /**
     * 密码
     */
    @NotBlank(groups = Insert.class)
    @Length(max = 100)
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 创建人
     */
    @Length(max = 30)
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @Length(max = 30)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
