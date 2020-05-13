package cn.hub.jackeroo.system.controller;

import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.system.entity.Menu;
import cn.hub.jackeroo.system.service.MenuService;
import cn.hub.jackeroo.vo.Json;
import cn.hub.jackeroo.utils.StringUtils;
import cn.hub.jackeroo.vo.ZTree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单信息Controller
 * @ahutor
 * @version 2018-11-14
 */
@Slf4j
@Controller
@RequestMapping(value = "${adminPath}/system/menu")
public class MenuController extends BaseController {

	@Autowired
	private MenuService menuService;
	
	/**
	 * @Description：菜单信息列表页面
	 * @ahutor
	 */
	@RequestMapping("index")
	public String index(){
		return "/system/menuList";
	}
	
	/**
	 * @Description：菜单信息列表数据
	 * @ahutor
	 */
	@RequestMapping("list")
	@ResponseBody
	public List<Menu> list(Menu menu) {
		menu.setIsShow(null);
		return menuService.findAllList(menu);
	}
	/**
	 * @Description：菜单信息表单页面--新增/编辑
	 * @ahutor
	 */
	@RequestMapping(value = "form")
	public String form(Menu menu, ModelMap model) {
		if(menu.getName() == null && menu.getId() != null){
			menu = menuService.get(menu.getId());
		}else{
			if(menu.getParentId() == null){
				menu.setParent(new Menu(1));
			}
			menu.setParent(menuService.get(menu.getParentId()));
			if(menu.getId() == null){
				menu.setSort(menuService.getMaxSortByPid(menu.getParent().getId()) + 30);
			}
		}
		model.addAttribute("menu", menu);
		return "/system/menuForm";
	}
	/**
	 * @Description：菜单信息新增/编辑 保存方法
	 * @ahutor
	 */
	@RequestMapping("saveMenu")
	public String saveMenu(Menu menu, ModelMap model){
		try{
			menuService.save(menu);
		}catch(Exception e){
			log.error("保存失败！ msg={}", e.getMessage(), e);

			model.addAttribute("error", "保存失败！");
			return form(menu, model);
		}
		return successPath;
	}
	/**
	 * @Description：菜单信息数据删除方法
	 * @ahutor
	 */
	@RequestMapping("delMenu")
	@ResponseBody
	public Json delMenu(Menu menu){
		Json json = new Json();
		try{
			menuService.delete(menu);
			json.setSuccess(true);
		}catch(Exception e){
			log.error("删除失败！ msg={}", e.getMessage(), e);

			json.setSuccess(false);
			json.setMsg("删除失败！");
		}
		return json;
	}

	/**
	 * 获取所有的菜单列表
	 * @return
	 */
	@RequestMapping("getMenuList")
	@ResponseBody
	public List<ZTree> getMenuList(String menuId){
		Menu menu = new Menu();
		menu.setIsShow("1");

		List<Menu> list = menuService.findAllList(menu);

		List<ZTree> trees = new ArrayList<>();
		for (Menu m : list){
			ZTree tree = new ZTree();

			tree.setId(m.getId());
			tree.setpId(m.getParentId());
			tree.setName(m.getName());
			if(StringUtils.isNotEmpty(menuId) && m.getId() == Integer.parseInt(menuId)){
				tree.setChecked(true);
			}

			trees.add(tree);
		}
		return trees;
	}

	/**
	 * 上级菜单选择页
	 * @return
	 */
	@RequestMapping("menuSelect")
	public String menuSelect(String menuId, ModelMap model){
		model.addAttribute("menuId", menuId);
		return "/system/menuSelectForm";
	}

}