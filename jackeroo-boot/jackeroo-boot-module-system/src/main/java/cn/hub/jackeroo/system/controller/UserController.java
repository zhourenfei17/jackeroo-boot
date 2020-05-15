package cn.hub.jackeroo.system.controller;

import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.system.entity.User;
import cn.hub.jackeroo.system.service.UserService;
import cn.hub.jackeroo.vo.Grid;
import cn.hub.jackeroo.vo.IUser;
import cn.hub.jackeroo.vo.Json;
import cn.hub.jackeroo.vo.GridParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 系统用户信息Controller
 * @ahutor
 * @version 2018-11-14
 */
@Slf4j
@Controller
@RequestMapping(value = "${adminPath}/system/user")
public class UserController extends BaseController {

	/*@Autowired
	private UserService userService;
	
	*//**
	 * @Description：系统用户信息列表页面
	 * @ahutor
	 *//*
	@RequestMapping("index")
	public String index(){
		return "/system/userList";
	}
	
	*//**
	 * @Description：系统用户信息表单页面--新增/编辑
	 * @ahutor
	 *//*
	@RequestMapping(value = "form")
	public String form(IUser user, ModelMap model) {
		if(user.getName() == null && user.getId() != null){
			user = userService.get(user.getId());
		}
		model.addAttribute("user", user);
		return "/system/userForm";
	}
	*//**
	 * @Description：系统用户信息新增/编辑 保存方法
	 * @ahutor
	 *//*
	@RequestMapping("saveUser")
	public String saveUser(User user, ModelMap model){
		try{
			userService.save(user);
		}catch(Exception e){
			log.error("保存失败！ msg={}", e.getMessage(), e);

			model.addAttribute("error", "保存失败！");
			return form(user, model);
		}
		return successPath;
	}
	*//**
	 * @Description：系统用户信息数据删除方法
	 * @ahutor
	 *//*
	@RequestMapping("delUser")
	@ResponseBody
	public Json delUser(User user){
		Json json = new Json();
		try{
			userService.delete(user);
			json.setSuccess(true);
		}catch(Exception e){
			log.error("删除失败！ msg={}", e.getMessage(), e);

			json.setSuccess(false);
			json.setMsg("删除失败！");
		}
		return json;
	}

	@RequestMapping("getManagerList")
	@ResponseBody
	public List<IUser> getManagerList(){
		User user = new User();
		user.setNature(0);		//管理
		return userService.findList(user);
	}*/
}