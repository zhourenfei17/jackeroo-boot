package cn.hub.jackeroo.online.entity;

import cn.hub.jackeroo.persistence.BaseEntity;
import cn.hub.jackeroo.utils.validator.groups.Insert;
import cn.hub.jackeroo.utils.validator.groups.Update;
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
     * 生成包路径
     */
    @NotBlank
    @Length(max = 100)
    private String packageName;

    /**
     * 生成模块名
     */
    @NotNull
    @Length(max = 20)
    private Long moduleId;

    /**
     * 显示复选框
     */
    @NotNull
    @Min(0)
    @Max(1)
    private Integer showCheckbox;

    /**
     * 表单风格
     */
    @NotNull
    @Min(0)
    @Max(1)
    private Integer formStyle;

    /**
     * 生成作者名
     */
    @NotBlank
    @Length(max = 50)
    private String author;

    /**
     * 生成路径
     */
    @NotBlank
    @Length(max = 500)
    private String outputDir;

    /**
     * 生成模版
     */
    @NotBlank
    @Length(max = 50)
    private String template;

    /**
     * 是否分页
     */
    @NotNull
    @Min(0)
    @Max(1)
    private Integer enablePagination;

    /**
     * 是否生成swagger文档
     */
    @NotNull
    @Min(0)
    @Max(1)
    private Integer enableSwagger;

    /**
     * 是否服务器端校验
     */
    @NotNull
    @Min(0)
    @Max(1)
    private Integer enableServerValid;
}
