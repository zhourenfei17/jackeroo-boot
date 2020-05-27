package cn.hub.jackeroo.persistence;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Date;
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
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;

	/**
	 * 自定义SQL（SQL标识，SQL内容）
	 */
	@TableField(exist = false)
	protected Map<String, String> sqlMap;
    @TableField(exist = false)
	private String dbName = "mysql"; // 当前数据库类型


	public BaseEntity() {

	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
