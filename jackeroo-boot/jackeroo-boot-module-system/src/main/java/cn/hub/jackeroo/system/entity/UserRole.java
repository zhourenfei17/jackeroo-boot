package cn.hub.jackeroo.system.entity;


import cn.hub.jackeroo.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 用户角色信息Entity
 * @author
 * @version 2018-11-14
 */
public class UserRole extends DataEntity<UserRole, Long> {
	
	private static final long serialVersionUID = 1L;
	private Long uid;		// 用户id
	private Long roleId;		// 角色id
	
	public UserRole() {
		super();
	}

	public UserRole(Long id){
		super(id);
	}

	@NotNull(message="用户id不能为空")
	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}
	
	@NotNull(message="角色id不能为空")
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
}