package cn.hub.jackeroo.online.entity;

import cn.hub.jackeroo.persistence.BaseEntity;
import cn.hub.jackeroo.utils.validator.groups.Insert;
import cn.hub.jackeroo.utils.validator.groups.Update;
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
 * 业务表字段信息
 * </p>
 *
 * @author jackeroo
 * @since 2020-09-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OnlineTableField extends BaseEntity<OnlineTableField> {

    private static final long serialVersionUID = 1L;

    @TableId
    @NotNull(groups = Update.class)
    @Null(groups = Insert.class)
    private Long id;

    /**
     * 业务表id
     */
    @NotNull(groups = Update.class)
    @Null(groups = Insert.class)
    private Long tableId;

    /**
     * 表字段名
     */
    @NotBlank
    @Length(max = 50)
    private String dbFieldName;

    /**
     * 表字段描述
     */
    @NotBlank
    @Length(max = 50)
    private String dbFieldDesc;

    /**
     * 表字段类型
     */
    @NotBlank
    @Length(max = 32)
    private String dbFieldType;

    /**
     * 表字段长度
     */
    @Min(0)
    @Max(128)
    private Integer dbFieldLength;

    /**
     * 表字段小数点长度
     */
    @Min(0)
    @Max(128)
    private Integer dbFieldDecimal;

    /**
     * 是否为主键
     */
    @Min(0)
    @Max(1)
    private Integer primaryKey;
    /**
     * 是否可空
     */
    @Min(0)
    @Max(1)
    private Integer enableNull;

    /**
     * 实体类字段名
     */
    @NotBlank
    @Length(max = 50)
    private String entityFieldName;

    /**
     * 实体类字段类型
     */
    @NotBlank
    @Length(max = 20)
    private String entityFieldType;

    /**
     * 是否为查询条件
     */
    @Min(0)
    @Max(1)
    private Integer enableQuery;

    /**
     * 是否为列表项
     */
    @Min(0)
    @Max(1)
    private Integer enableList;

    /**
     * 是否为表单项
     */
    @Min(0)
    @Max(1)
    private Integer enableForm;

    /**
     * 是否支持排序
     */
    @Min(0)
    @Max(1)
    private Integer enableSort;

    /**
     * 乐观锁
     */
    @Min(0)
    @Max(1)
    private Integer locker;

    /**
     * 填充策略
     */
    @Length(max = 20)
    private String fillStrategy;

    /**
     * 查询方式
     */
    @NotBlank
    @Length(max = 10)
    private String queryType;

    /**
     * 表单控件类型
     */
    @NotBlank
    @Length(max = 20)
    private String formType;

    /**
     * 表单是否必填
     */
    @Min(0)
    @Max(1)
    private Integer formRequired;

    /**
     * 表单校验规则
     */
    @Length(max = 200)
    private String formValidator;

    /**
     * 表单字典code
     */
    @Length(max = 50)
    private String formDictCode;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 是否占整行
     */
    @Min(0)
    @Max(1)
    private Integer singleLine;

    /**
     * 是否可编辑
     */
    @TableField(exist = false)
    private Boolean disabled = false;

}
