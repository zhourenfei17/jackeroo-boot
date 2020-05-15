package cn.hub.jackeroo.system.controller;

import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.system.entity.Role;
import cn.hub.jackeroo.exception.JackerooException;
import cn.hub.jackeroo.system.service.MenuService;
import cn.hub.jackeroo.system.service.RoleService;
import cn.hub.jackeroo.vo.Grid;
import cn.hub.jackeroo.vo.Json;
import cn.hub.jackeroo.vo.GridParam;
import cn.hub.jackeroo.vo.ZTree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 系统角色Controller
 * @ahutor
 * @version 2018-11-14
 */
@Slf4j
@Controller
@RequestMapping(value = "${adminPath}/system/role")
public class RoleController extends BaseController {

	/*@Autowired
	private RoleService roleService;
	@Autowired
	private MenuService menuService;
	
	*//**
	 * @Description：系统角色列表页面
	 * @ahutor
	 *//*
	@RequestMapping("index")
	public String index(){
		return "/system/roleList";
	}
	
	*//**
	 * @Description：系统角色表单页面--新增/编辑
	 * @ahutor
	 *//*
	@RequestMapping(value = "form")
	public String form(Role role, ModelMap model) {
		if(role.getName() == null && role.getId() != null){
			role = roleService.get(role.getId());
		}
		model.addAttribute("role", role);
		return "/system/roleForm";
	}
	*//**
	 * @Description：系统角色新增/编辑 保存方法
	 * @ahutor
	 *//*
	@RequestMapping("saveRole")
	public String saveRole(Role role, ModelMap model){
		try{
			roleService.save(role);
		}catch(Exception e){
			if(e instanceof JackerooException){
				model.addAttribute("error", "该角色已存在！");
			}else{
				model.addAttribute("error", "保存失败！");
			}
			log.error("保存失败！ msg={}", e.getMessage(), e);

			return form(role, model);
		}
		return successPath;
	}
	*//**
	 * @Description：系统角色数据删除方法
	 * @ahutor
	 *//*
	@RequestMapping("delRole")
	@ResponseBody
	public Json delRole(Role role){
		Json json = new Json();
		try{
			roleService.delete(role);
			json.setSuccess(true);
		}catch(Exception e){
			log.error("删除失败！ msg={}", e.getMessage(), e);

			json.setSuccess(false);
			json.setMsg("删除失败！");
		}
		return json;
	}

	*//**
	 * 权限设置页面
	 * @param model
	 * @return
	 *//*
	@RequestMapping("editPermission")
	public String editPermission(ModelMap model){
		return "/system/permissionSelectForm";
	}

	@RequestMapping("rolePermissionList")
	@ResponseBody
	public List<ZTree> rolePermissionList(String roleId){
		return menuService.findMenuByRole(Long.parseLong(roleId));
	}

	*//**
	 * @Description: 编辑保存角色菜单关系
	 * @param roleId
	 * @param addMenus
	 * @param delMenus
	 * @return
	 * @ahutor
	 * @date 2016年9月21日
	 *//*
	@RequestMapping("editRoleMenu")
	@ResponseBody
	public Json editRoleMenu(String roleId, String addMenus, String delMenus){
		Json json = new Json();
		try{
			roleService.editRoleMenu(Long.parseLong(roleId), addMenus, delMenus);
			json.setSuccess(true);
		}catch(Exception e){
			log.error("操作失败", e);

			json.setSuccess(false);
			json.setMsg("操作失败！");
		}
		return json;
	}*/
}