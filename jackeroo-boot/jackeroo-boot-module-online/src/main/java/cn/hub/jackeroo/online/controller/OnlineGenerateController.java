package cn.hub.jackeroo.online.controller;

import cn.hub.jackeroo.online.entity.OnlineDatasource;
import cn.hub.jackeroo.online.entity.OnlineTable;
import cn.hub.jackeroo.online.param.GenerateCodeParam;
import cn.hub.jackeroo.online.param.GenerateTableDetail;
import cn.hub.jackeroo.online.service.OnlineDatasourceService;
import cn.hub.jackeroo.online.service.OnlineGenerateService;
import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.utils.annotation.ApiModule;
import cn.hub.jackeroo.vo.Id;
import cn.hub.jackeroo.vo.PageParam;
import cn.hub.jackeroo.vo.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 代码生成controller
 * @author jackeroo
 * @date 2020/09/18
 */
@Slf4j
@ApiModule(moduleName = "在线开发")
@Api("代码生成")
@RestController
@RequestMapping("/online/generate")
public class OnlineGenerateController extends BaseController {

    @Autowired
    private OnlineGenerateService service;
    @Autowired
    private OnlineDatasourceService datasourceService;

    /**
     * 获取数据库业务表列表
     * @param onlineTable
     * @param pageParam
     * @return
     */
    @GetMapping("findTableListFromDataSource")
    @ApiOperation("获取数据库业务表列表")
    public Result<IPage<OnlineTable>> findTableListFromDataSource(OnlineTable onlineTable, @Validated PageParam pageParam, @RequestParam String dataSourceName){
        String dataSource = datasourceService.getDataSourceName(dataSourceName);
        return ok(service.findTableListFromDataSource(onlineTable, pageParam, dataSource));
    }

    /**
     * 获取数据库业务表字段列表
     * @param tableName
     * @return
     */
    /*@GetMapping("findTableColumnList")
    @ApiOperation("获取数据库业务表字段列表")
    public Result findTableColumnList(String tableName){
        return ok(service.findTableColumnList(tableName, "0"));
    }*/

    /**
     * 获取数据库业务表详细内容
     * @param tableName
     * @return
     */
    @GetMapping("findTableDetailInfo")
    @ApiOperation("获取数据库业务表详细内容")
    public Result findTableDetailInfo(String tableName, String dataSource){
        return ok(service.findTableDetailInfo(tableName, dataSource));
    }

    /**
     * 保存代码生成信息
     * @param detail
     * @return
     */
    @PostMapping("save")
    @ApiOperation("保存代码生成信息")
    @RequiresPermissions("online:generate:add")
    public Result save(@RequestBody GenerateTableDetail detail){
        return ok(service.save(detail));
    }

    /**
     * 生成代码
     * @return
     */
    @PostMapping("generateCode")
    @ApiOperation("生成代码")
    @RequiresPermissions("online:generate:generate")
    public Result generateCode(@Validated @RequestBody GenerateCodeParam param){
        service.generateCode(param.getId(), param.getOutputDir(), param.getOverride(), param.getTemplateType());
        return ok();
    }

    /**
     * 根目录文件列表
     * @return
     */
    @GetMapping("rootFiles")
    @ApiOperation("根目录文件列表")
    public Result rootFiles(){
        return ok(service.getFileList(null));
    }

    /**
     * 指定目录下文件列表
     * @param path
     * @return
     */
    @GetMapping("listFiles")
    @ApiOperation("指定目录下文件列表")
    public Result listFiles(@RequestParam String path){
        return ok(service.getFileList(path));
    }
}
