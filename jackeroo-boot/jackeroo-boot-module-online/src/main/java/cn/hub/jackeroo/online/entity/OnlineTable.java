package cn.hub.jackeroo.online.entity;

import cn.hub.jackeroo.persistence.BaseEntity;
import cn.hub.jackeroo.utils.validator.groups.Insert;
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
 * 业务表信息
 * </p>
 *
 * @author jackeroo
 * @since 2020-09-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OnlineTable extends BaseEntity<OnlineTable> {

    private static final long serialVersionUID = 1L;

    @TableId
    @NotNull(groups = Update.class)
    @Null(groups = Insert.class)
    private Long id;
    /**
     * 表名
     */
    @NotBlank
    @Length(max = 100)
    private String tableName;

    /**
     * 类名
     */
    @NotBlank
    @Length(max = 100)
    private String className;

    /**
     * 表说明
     */
    @NotBlank
    @Length(max = 100)
    private String comment;

    /**
     * 主键策略
     */
    @NotBlank
    @Length(max = 20)
    private String idStrategy;

    /**
     * 删除策略
     */
    @NotNull
    @Min(0)
    @Max(1)
    private Integer delStrategy;

    /**
     * 逻辑删字段
     */
    @Length(max = 50)
    private String logicField;

    /**
     * 默认排序字段
     */
    @Length(max = 50)
    private String sortColumn;

    /**
     * 排序方式
     */
    @Length(max = 5)
    private String sortType;

    /**
     * 创建人
     */
    @Null
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @Null
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @Null
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @Null
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 数据源
     */
    @TableField(exist = false)
    private String dataSource;

    /**
     * 删除策略--物理删除
     */
    public static final int DEL_STRATEGY_DATABASE = 0;
    /**
     * 删除策略--逻辑删除
     */
    public static final int DEL_STRATEGY_LOGIC = 1;
}
