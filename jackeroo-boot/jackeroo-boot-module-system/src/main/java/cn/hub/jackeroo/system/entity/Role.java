package cn.hub.jackeroo.system.entity;

import cn.hub.jackeroo.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/**
 * 系统角色Entity
 * @author
 * @version 2018-11-14
 */
public class Role extends DataEntity<Role, Long> {
	
	private static final long serialVersionUID = 1L;
	private Integer parentId;		// 父级id
	private String name;		// 岗位名称
	private String comment;		// 描述
	
	public Role() {
		super();
	}

	public Role(Long id){
		super(id);
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parent_id) {
		this.parentId = parent_id;
	}
	
	@Length(min=1, max=50, message="岗位名称长度必须介于 1 和 50 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=1, max=50, message="描述长度必须介于 1 和 50 之间")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}