package cn.hub.jackeroo.online.controller;

import cn.hub.jackeroo.online.entity.GenScheme;
import cn.hub.jackeroo.online.service.GenSchemeService;
import cn.hub.jackeroo.online.service.GenTableService;
import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.vo.Grid;
import cn.hub.jackeroo.vo.GridParam;
import cn.hub.jackeroo.vo.Json;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequestMapping("/gen/genScheme")
@Controller
public class GenSchemeController extends BaseController {
    @Autowired
    private GenSchemeService genSchemeService;
    @Autowired
    private GenTableService genTableService;

    @RequestMapping("")
    public String index(){
        return "/gen/genSchemeList";
    }

    @RequestMapping("/list")
    @ResponseBody
    public Grid list(GridParam gridParam, GenScheme genScheme){
        return genSchemeService.find(gridParam, genScheme);
    }

    @RequestMapping(value = "form")
    public String form(GenScheme genScheme, ModelMap model) {
        if (StringUtils.isBlank(genScheme.getPackageName())){
            genScheme.setPackageName("com.alex.springboot");
        }
        if(genScheme.getId() != null && StringUtils.isEmpty(genScheme.getName())){
            genScheme = genSchemeService.get(genScheme.getId());
        }
//		if (StringUtils.isBlank(genScheme.getFunctionAuthor())){
//			genScheme.setFunctionAuthor(UserUtils.getUser().getName());
//		}
        model.addAttribute("genScheme", genScheme);
        //model.addAttribute("config", GenUtils.getConfig());
        model.addAttribute("tableList", genTableService.findAll());
        return "/gen/genSchemeForm";
    }

    @RequestMapping(value = "save")
    public String save(GenScheme genScheme, ModelMap model) {
        try {
            genSchemeService.saveScheme(genScheme);
        }catch (Exception e){
            model.addAttribute("error", "保存失败");
            log.error("保存失败 msg={}", e.getMessage(), e);
            return form(genScheme, model);
        }
        return successPath;
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    public Json delete(GenScheme genScheme) {
        Json json = new Json();
        try {
            genSchemeService.delete(genScheme);

            json.setSuccess(true);
            json.setMsg("删除生成方案成功");
        }catch (Exception e){
            log.error("删除失败 msg={}", e.getMessage(), e);

            json.setSuccess(false);
            json.setMsg("删除生成方案失败");
        }
        return json;
    }
}