package cn.hub.jackeroo.online.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import cn.hub.jackeroo.persistence.BaseEntity;
import cn.hub.jackeroo.utils.validator.annotation.Unique;
import cn.hub.jackeroo.utils.validator.groups.Insert;
import cn.hub.jackeroo.utils.validator.groups.Update;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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

/**
* <p>
* 生成代码默认配置项
* </p>
*
* @author jackeroo
* @since 2020-10-26
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OnlineDefaultConfig extends BaseEntity<OnlineDefaultConfig> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    @NotNull(groups = Update.class)
    @Null(groups = Insert.class)
    private Long id;

    /**
     * 排序字段
     */
    @Length(max = 30)
    private String sortColumn;

    /**
     * 排序方式
     */
    @Length(max = 5)
    private String sortType;

    /**
     * 主键策略
     */
    @Length(max = 20)
    private String idStrategy;

    /**
     * 逻辑删字段
     */
    @Length(max = 20)
    private String logicColumn;

    /**
     * 生成包名
     */
    @Length(max = 50)
    private String packageName;

    /**
     * 表单风格
     */
    @Min(0)
    @Max(128)
    private Integer formStyle;

    /**
     * 显示复选框
     */
    @Min(0)
    @Max(128)
    private Integer showCheckbox;

    /**
     * 是否分页
     */
    @Min(0)
    @Max(128)
    private Integer enablePagination;

    /**
     * 生成swagger文档
     */
    @Min(0)
    @Max(128)
    private Integer enableSwagger;

    /**
     * 服务器端校验
     */
    @Min(0)
    @Max(128)
    private Integer enableServerValid;

    /**
     * 列配置
     */
    private String columnConfig;

}
