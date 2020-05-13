package cn.hub.jackeroo.system.controller;

import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.system.entity.Dict;
import cn.hub.jackeroo.system.service.DictService;
import cn.hub.jackeroo.vo.Grid;
import cn.hub.jackeroo.vo.Json;
import cn.hub.jackeroo.vo.GridParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 字典信息Controller
 * @author 
 * @version 2018-11-14
 */
@Slf4j
@Controller
@RequestMapping(value = "${adminPath}/system/dict")
public class DictController extends BaseController {

	@Autowired
	private DictService dictService;
	
	/**
	 * @Description：字典信息列表页面
	 * @author 
	 */
	@RequestMapping("index")
	public String index(){
		return "system/system/dictList";
	}
	
	/**
	 * @Description：字典信息列表数据
	 * @author 
	 */
	@RequestMapping("list")
	@ResponseBody
	public Grid list(Dict dict, GridParam param) {
		return dictService.findPage(dict, param);
	}
	/**
	 * @Description：字典信息表单页面--新增/编辑
	 * @author 
	 */
	@RequestMapping(value = "form")
	public String form(Dict dict, ModelMap model) {
		if(dict.getValue() == null && dict.getId() != null){
			dict = dictService.get(dict.getId());
		}
		model.addAttribute("dict", dict);
		return "system/system/dictForm";
	}
	/**
	 * @Description：字典信息新增/编辑 保存方法
	 * @ahutor
	 */
	@RequestMapping("saveDict")
	public String saveDict(Dict dict, ModelMap model){
		try{
			dictService.save(dict);
		}catch(Exception e){
			log.error("保存失败！ msg={}", e.getMessage(), e);

			model.addAttribute("error", "保存失败！");
			return form(dict, model);
		}
		return successPath;
	}
	/**
	 * @Description：字典信息数据删除方法
	 * @ahutor
	 */
	@RequestMapping("delDict")
	@ResponseBody
	public Json delDict(Dict dict){
		Json json = new Json();
		try{
			dictService.delete(dict);
			json.setSuccess(true);
		}catch(Exception e){
			log.error("删除失败！ msg={}", e.getMessage(), e);

			json.setSuccess(false);
			json.setMsg("删除失败！");
		}
		return json;
	}

	@ResponseBody
	@RequestMapping(value = "listData")
	public List<Dict> listData(@RequestParam(required=false) String type) {
		Dict dict = new Dict();
		dict.setType(type);
		return dictService.findList(dict);
	}
}