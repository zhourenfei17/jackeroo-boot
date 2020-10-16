package cn.hub.jackeroo.system.entity;

import cn.hub.jackeroo.persistence.BaseEntity;
import cn.hub.jackeroo.utils.validator.annotation.Unique;
import cn.hub.jackeroo.utils.validator.groups.First;
import cn.hub.jackeroo.utils.validator.groups.Insert;
import cn.hub.jackeroo.utils.validator.groups.Second;
import cn.hub.jackeroo.utils.validator.groups.Update;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

/**
* <p>
* 数据字典
* </p>
*
* @author jackeroo
* @since 2020-10-14
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDict extends BaseEntity<SysDict> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    @NotNull(groups = Update.class)
    @Null(groups = Insert.class)
    private Long id;

    /**
     * 数据值
     */
    @Length(max = 50, groups = Second.class)
    @NotBlank(groups = Second.class)
    @Unique(name = "字典项值", groups = First.class)
    private String value;

    /**
     * 标签名
     */
    @Length(max = 30, groups = Second.class)
    @NotBlank(groups = Second.class)
    private String label;

    /**
     * 字典编码
     */
    @NotBlank
    @Length(max = 30)
    @Unique(name = "字典编码", groups = Second.class)
    private String dictCode;

    /**
     * 字典名称
     */
    @NotBlank(groups = First.class)
    @Length(max = 30, groups = First.class)
    private String dictName;

    /**
     * 类型，0:字典,1:字典项
     */
    @Min(0)
    @Max(1)
    private Integer type;

    /**
     * 排序
     */
    @Min(0)
    @Max(99999)
    private Integer sort;

    /**
     * 备注
     */
    @Length(max = 100)
    private String remark;

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


    public static final int TYPE_DICT = 0;
    public static final int TYPE_DICT_ITEM = 1;
}
