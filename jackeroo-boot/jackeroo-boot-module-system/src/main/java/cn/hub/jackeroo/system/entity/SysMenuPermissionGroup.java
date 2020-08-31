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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * <p>
 * 菜单权限配置组别
 * </p>
 *
 * @author jackeroo
 * @since 2020-08-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysMenuPermissionGroup extends BaseEntity<SysMenuPermissionGroup> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    @NotNull(groups = Update.class)
    @Null(groups = Insert.class)
    private Long id;

    /**
     * 权限组名
     */
    @NotBlank
    @Length(max = 20)
    private String groupName;

    /**
     * 是否为默认组
     */
    private Integer isDefault;

    /**
     * 是否禁用
     */
    private Integer disabled;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
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

    public static final int DISABLED_FLAG_ENABLE = 0;
    public static final int DISABLED_FLAG_DISABLE = 1;

}
