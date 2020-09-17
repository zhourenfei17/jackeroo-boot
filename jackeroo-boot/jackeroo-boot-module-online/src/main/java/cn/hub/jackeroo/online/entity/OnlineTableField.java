package cn.hub.jackeroo.online.entity;

import cn.hub.jackeroo.persistence.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
    private Long id;

    /**
     * 业务表id
     */
    private Long tableId;

    /**
     * 表字段名
     */
    private String dbFieldName;

    /**
     * 表字段描述
     */
    private String dbFieldDesc;

    /**
     * 表字段类型
     */
    private String dbFieldType;

    /**
     * 表字段长度
     */
    private Integer dbFieldLength;

    /**
     * 表字段小数点长度
     */
    private Integer dbFieldDecimal;

    /**
     * 是否为主键
     */
    private Integer primaryKey;
    /**
     * 是否可空
     */
    private Integer nullable;

    /**
     * 实体类字段名
     */
    private String entityFieldName;

    /**
     * 实体类字段类型
     */
    private String entityFieldType;

    /**
     * 是否为查询条件
     */
    private Integer enableQuery;

    /**
     * 是否为列表项
     */
    private Integer enableList;

    /**
     * 是否为表单项
     */
    private Integer enableForm;

    /**
     * 是否支持排序
     */
    private Integer enableSort;

    /**
     * 查询方式
     */
    private Integer queryType;

    /**
     * 表单控件类型
     */
    private Integer formType;

    /**
     * 表单是否必填
     */
    private Integer formRequired;

    /**
     * 表单校验规则
     */
    private String formValidator;

    /**
     * 表单字典code
     */
    private String formDictCode;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 是否占整行
     */
    private Integer singleLine;

}
