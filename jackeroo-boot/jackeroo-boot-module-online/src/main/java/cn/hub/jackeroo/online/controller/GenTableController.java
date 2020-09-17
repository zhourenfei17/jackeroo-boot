package cn.hub.jackeroo.online.controller;

import cn.hub.jackeroo.persistence.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/gen/genTable")
@Controller
public class GenTableController extends BaseController {
    /*@Autowired
    private GenTableService service;

    @RequestMapping("")
    public String index(){
        return "/gen/genTableList";
    }

    @GetMapping("/list")
    @ResponseBody
    public Grid list(GridParam gridParam, GenTable genTable){
        return service.find(gridParam, genTable);
    }

    @RequestMapping("/form")
    public String form(GenTable genTable, ModelMap map){
        if(genTable.getId() != null){
            genTable = service.get(genTable.getId());
        }
        // 获取物理表列表
        //List<GenTable> tableList = service.findTableListFormDb(new GenTable());
        // 验证表是否存在
        if (genTable.getId() == null && !service.checkTableName(genTable.getName())){
            map.addAttribute("error", "下一步失败！" + genTable.getName() + " 表已经添加！");
            genTable.setName("");
        }else if(StringUtils.isNotEmpty(genTable.getName())){
            map.addAttribute("config", GenUtils.getConfig());
            // 获取物理表字段
            genTable = service.getTableFormDb(genTable);
        }

        map.addAttribute("genTable", genTable);

        return "/gen/genTableForm";
    }

    @RequestMapping(value = "save")
    public String save(GenTable genTable, ModelMap map) {
        // 验证表是否已经存在
        if (genTable.getId()==null && !service.checkTableName(genTable.getName())){
            map.addAttribute("error", "保存失败！" + genTable.getName() + " 表已经存在！");
            genTable.setName("");
            return form(genTable, map);
        }
        service.save(genTable);
        return successPath;
    }

    *//**
     * 获取数据库所有物理表
     * @return
     *//*
    @RequestMapping("/getAllTable")
    @ResponseBody
    public List<GenTable> getAllTable(){
        // 获取物理表列表
        return service.findTableListFormDb(new GenTable());
    }

    *//**
     * 删除业务表配置
     * @param genTable
     * @param redirectAttributes
     * @return
     *//*
    @RequestMapping(value = "delete")
    @ResponseBody
    public Json delete(GenTable genTable, RedirectAttributes redirectAttributes) {
        Json json = new Json();
        try {
            service.delete(genTable);

            json.setSuccess(true);
            json.setMsg("删除成功");
        }catch (Exception e){
            log.error(e.getMessage());
            json.setSuccess(false);
            json.setMsg("删除失败");
        }
        return json;
    }*/
}
