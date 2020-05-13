package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.system.entity.Role;
import cn.hub.jackeroo.system.entity.RoleMenu;
import cn.hub.jackeroo.persistence.CrudService;
import cn.hub.jackeroo.system.dao.RoleDao;
import cn.hub.jackeroo.exception.JackerooException;
import cn.hub.jackeroo.utils.IDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统角色Service
 * @author
 * @version 2018-11-14
 */
@Service
public class RoleService extends CrudService<RoleDao, Role, Long> {
	@Resource
	private RoleDao dao;
	@Resource
	private MenuService menuService;

	public List<Role> findByUserid(Long userId){
		return dao.findByUserId(userId);
	}

	public void editRoleMenu(Long roleId, String addMenus, String delMenus){
		if(StringUtils.isNotBlank(delMenus)){
			dao.delRoleMenu(roleId, delMenus);
		}
		if(StringUtils.isNotBlank(addMenus)){
			List<RoleMenu> roleMenus = new ArrayList<RoleMenu>();
			for(String addMenu : addMenus.split(",")){
				if(StringUtils.isNotBlank(addMenu)){
					RoleMenu roleMenu = new RoleMenu();
					roleMenu.setRoleId(roleId);
					roleMenu.setMenuId(Integer.parseInt(addMenu));
					roleMenus.add(roleMenu);
				}
			}
			dao.addRoleMenu(roleMenus);
		}
		// 清除菜单缓存
		menuService.updateUserMenuTree(roleId);
	}

	@Override
	public void save(Role role){
		if(dao.checkRoleName(role) > 0){
			throw new JackerooException("该角色已存在");
		}
		if(role.getId() == null){
			role.setId(IDUtil.nextId());
			dao.insert(role);
		}else {
			dao.update(role);
		}
	}
}