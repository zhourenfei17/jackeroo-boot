package cn.hub.jackeroo.online.entity;

import cn.hub.jackeroo.persistence.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
    private Long id;
    /**
     * 表名
     */
    private String tableName;

    /**
     * 类名
     */
    private String className;

    /**
     * 表说明
     */
    private String comment;

    /**
     * 主键策略
     */
    private String idStrategy;

    /**
     * 删除策略
     */
    private Integer delStrategy;

    /**
     * 逻辑删字段
     */
    private String logicField;

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
