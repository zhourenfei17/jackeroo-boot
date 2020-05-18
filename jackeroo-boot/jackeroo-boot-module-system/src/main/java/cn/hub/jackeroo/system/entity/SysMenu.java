package cn.hub.jackeroo.system.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 上级菜单id
     */
    private String parentId;

    /**
     * 所有上级菜单id
     */
    private String parentIds;

    /**
     * 菜单等级
     */
    @TableField("LEVEL")
    private Integer level;

    /**
     * 菜单名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 菜单类型(0：菜单，1：权限)
     */
    @TableField("TYPE")
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
    private String target;

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
