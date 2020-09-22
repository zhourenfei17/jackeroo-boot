package cn.hub.jackeroo.online.controller;

import cn.hub.jackeroo.online.entity.OnlineTable;
import cn.hub.jackeroo.online.service.OnlineGenerateService;
import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.utils.annotation.ApiModule;
import cn.hub.jackeroo.vo.PageParam;
import cn.hub.jackeroo.vo.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 代码生成controller
 * @author jackeroo
 * @date 2020/09/18
 */
@ApiModule(moduleName = "在线开发")
@Api("代码生成")
@RestController
@RequestMapping("/online/generate")
public class OnlineGenerateController extends BaseController {

    @Autowired
    private OnlineGenerateService service;

    /**
     * 获取数据库业务表列表
     * @param onlineTable
     * @param pageParam
     * @return
     */
    @GetMapping("findTableListFromDataSource")
    @ApiOperation("获取数据库业务表列表")
    public Result<IPage<OnlineTable>> findTableListFromDataSource(OnlineTable onlineTable, @Validated PageParam pageParam){
        return ok(service.findTableListFromDataSoure(onlineTable, pageParam));
    }

    /**
     * 获取数据库业务表字段列表
     * @param tableName
     * @return
     */
    @GetMapping("findTableColumnList")
    @ApiOperation("获取数据库业务表字段列表")
    public Result findTableColumnList(String tableName){
        return ok(service.findTableColumnList(tableName));
    }

    /**
     * 获取数据库业务表详细内容
     * @param tableName
     * @return
     */
    @GetMapping("findTableDetailInfo")
    @ApiOperation("获取数据库业务表详细内容")
    public Result findTableDetailInfo(String tableName){
        return ok(service.findTableDetailInfo(tableName));
    }
}
