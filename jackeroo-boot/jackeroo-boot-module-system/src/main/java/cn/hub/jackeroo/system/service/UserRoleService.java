package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.system.entity.UserRole;
import cn.hub.jackeroo.persistence.CrudService;
import cn.hub.jackeroo.system.dao.UserRoleDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户角色信息Service
 * @author
 * @version 2018-11-14
 */
@Service
public class UserRoleService extends CrudService<UserRoleDao, UserRole, Long> {
	@Resource
	private UserRoleDao dao;

}