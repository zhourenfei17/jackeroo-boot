package cn.hub.jackeroo.system.entity;

import cn.hub.jackeroo.persistence.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 角色菜单
 * </p>
 *
 * @author jackeroo
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysRoleMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;
    /**
     * 角色id
     */
    @NotNull
    private Long roleId;

    /**
     * 菜单id
     */
    @NotNull
    private Long menuId;
}
