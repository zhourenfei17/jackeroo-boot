package cn.hub.jackeroo.system.entity;

import cn.hub.jackeroo.persistence.BaseEntity;
import cn.hub.jackeroo.system.vo.AuthVo;
import cn.hub.jackeroo.utils.validator.groups.Insert;
import cn.hub.jackeroo.utils.validator.groups.Update;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 菜单信息
 * </p>
 *
 * @author jackeroo
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysMenu extends BaseEntity<SysMenu> {

    private static final long serialVersionUID = 1L;

    @TableId
    @NotNull(groups = Update.class)
    @Null(groups = Insert.class)
    private Long id;
    /**
     * 上级菜单id
     */
    private Long parentId;

    /**
     * 所有上级菜单id
     */
    private String parentIds;

    /**
     * 是否叶子菜单
     */
    private Integer leaf;
    /**
     * 菜单等级
     */
    private Integer level;

    /**
     * 菜单名称
     */
    @NotEmpty
    private String name;

    /**
     * 菜单类型(0：菜单，1：权限)
     */
    @NotNull
    private Integer type;

    /**
     * 访问url路径
     */
    private String href;

    /**
     * 组件地址
     */
    private String component;

    /**
     * 布局组件
     */
    private String layout;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 打开目标
     */
    private Integer target;

    /**
     * 是否隐藏菜单
     */
    private Integer hide;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private List<SysMenu> children;
    @TableField(exist = false)
    private String function;
    @TableField(exist = false)
    private String module;
    @TableField(exist = false)
    private List<AuthVo> auth;


    public static final int TYPE_MENU = 0;
    public static final int TYPE_PERMISSION = 1;

    public static final int TARGET_INSIDE = 1;
    public static final int TARGET_OUTSIDE = 2;
}
