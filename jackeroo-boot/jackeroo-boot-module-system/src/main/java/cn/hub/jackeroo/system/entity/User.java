package cn.hub.jackeroo.system.entity;

import cn.hub.jackeroo.vo.IUser;


/**
 * 系统用户信息Entity
 * @author
 * @version 2018-11-14
 */
public class User extends IUser {
	
	private static final long serialVersionUID = 1L;
	private cn.hub.jackeroo.system.entity.Role role;

	private String managerName;
	
	public User() {
		super();
	}

	public User(Long id){
		super();
		this.id = id;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}


	public cn.hub.jackeroo.system.entity.Role getRole() {
		return role;
	}

	public void setRole(cn.hub.jackeroo.system.entity.Role role) {
		this.role = role;
	}
}