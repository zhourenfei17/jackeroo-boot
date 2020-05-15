package cn.hub.jackeroo.system.entity;

import cn.hub.jackeroo.persistence.DataEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 字典信息Entity
 * @author
 * @version 2018-11-14
 */
@TableName(value = "sys_dict")
public class Dict extends DataEntity<Dict, Long> {
	
	private static final long serialVersionUID = 1L;
	@TableId
	private Long id;
	private String value;		// 数据值
	private String label;		// 标签名
	private String type;		// 类型
	private String description;		// 描述
	private Integer sort;		// 排序（升序）
	private Integer parent_id;		// 父级编号

	public Dict() {
		super();
	}

	public Dict(Long id){
		super(id);
	}

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Length(min=1, max=100, message="数据值长度必须介于 1 和 100 之间")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Length(min=1, max=100, message="标签名长度必须介于 1 和 100 之间")
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	@Length(min=1, max=100, message="类型长度必须介于 1 和 100 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=1, max=100, message="描述长度必须介于 1 和 100 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@NotNull(message="排序（升序）不能为空")
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public Integer getParent_id() {
		return parent_id;
	}

	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}
	
}