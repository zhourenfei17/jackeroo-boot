package cn.hub.jackeroo.online.entity;

import cn.hub.jackeroo.persistence.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 代码生成方案
 * </p>
 *
 * @author jackeroo
 * @date 2020/09/17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OnlineScheme extends BaseEntity<OnlineScheme> {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;
    /**
     * 业务表id
     */
    private Long tableId;
    /**
     * 生成包路径
     */
    private String packageName;

    /**
     * 生成模块名
     */
    private String moduleName;

    /**
     * 显示复选框
     */
    private Integer showCheckbox;

    /**
     * 表单风格
     */
    private Integer formStyle;

    /**
     * 生成作者名
     */
    private String author;

    /**
     * 生成路径
     */
    private String outputDir;

    /**
     * 生成模版
     */
    private String template;

    /**
     * 是否分页
     */
    private Integer enablePagination;

    /**
     * 是否生成swagger文档
     */
    private Integer enableSwagger;

    /**
     * 是否服务器端校验
     */
    private Integer enableServerValid;
}
