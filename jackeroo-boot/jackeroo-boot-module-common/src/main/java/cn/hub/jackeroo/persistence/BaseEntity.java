package cn.hub.jackeroo.persistence;

import cn.hub.jackeroo.utils.StringUtils;
import cn.hub.jackeroo.vo.PageParam;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * Entity支持类
 * 
 * @NotNull 和 @NotEmpty 和@NotBlank 区别
 * @NotEmpty 用在集合类上面
 * @NotBlank 用在String上面
 * @NotNull 用在基本类型上
 * @author
 * @version 2014-05-16
 */
@Data
public abstract class BaseEntity<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;


	/**
	 * 自定义SQL（SQL标识，SQL内容）
	 */
	@TableField(exist = false)
    @JsonIgnore
    @ApiModelProperty(hidden = true)
	protected Map<String, String> sqlMap;
    @TableField(exist = false)
    @JsonIgnore
    @ApiModelProperty(hidden = true)
	private String dbName = "mysql"; // 当前数据库类型
    /**
     * 排序字段
     */
    @TableField(exist = false)
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String orderBy;
    @TableField(exist = false)
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @ApiParam(hidden = true)
    private Page<T> page;

    public Page<T> initPage(PageParam pageParam){
        page = new Page();
        page.setCurrent(pageParam.getPageNo());
        page.setSize(pageParam.getPageSize());
        if(StringUtils.isNotBlank(pageParam.getSortField())){
            if(StringUtils.isNotBlank(pageParam.getSortOrder())){
                this.orderBy = StringUtils.toUnderScoreCase(pageParam.getSortField()) + " "
                        + pageParam.getSortOrder().replace("end", "");
            }else{
                this.orderBy = StringUtils.toUnderScoreCase(pageParam.getSortField()) + " asc";
            }
        }

        return page;
    }
}
