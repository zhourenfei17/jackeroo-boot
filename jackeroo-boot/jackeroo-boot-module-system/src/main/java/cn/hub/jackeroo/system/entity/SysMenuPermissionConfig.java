package cn.hub.jackeroo.system.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import cn.hub.jackeroo.persistence.BaseEntity;
import cn.hub.jackeroo.utils.validator.groups.Insert;
import cn.hub.jackeroo.utils.validator.groups.Update;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * <p>
 * 菜单权限配置
 * </p>
 *
 * @author jackeroo
 * @since 2020-08-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysMenuPermissionConfig extends BaseEntity<SysMenuPermissionConfig> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    @NotNull(groups = Update.class)
    @Null(groups = Insert.class)
    private Long id;

    /**
     * 所在分组
     */
    @NotNull
    private Long groupId;

    /**
     * 权限名称
     */
    @NotBlank
    @Length(max = 20)
    private String label;

    /**
     * 权限标识
     */
    @NotBlank
    @Length(max = 30)
    private String value;

    /**
     * 是否选中
     */
    @NotNull
    @Min(0)
    @Max(1)
    private Integer checked;

    /**
     * 排序号
     */
    @NotNull
    @Min(0)
    @Max(99999)
    private Integer sort;

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
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
