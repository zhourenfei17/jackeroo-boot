package cn.hub.jackeroo.online.controller;

import cn.hub.jackeroo.online.entity.OnlineTable;
import cn.hub.jackeroo.online.service.OnlineTableService;
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
 * 业务表信息controller
 * @author jackeroo
 * @date 2020/09/18
 */
@ApiModule(moduleName = "在线开发")
@Api(tags = "业务表信息")
@RestController
@RequestMapping("/online/table")
public class OnlineTableController extends BaseController {

    @Autowired
    private OnlineTableService service;

    /**
     * 业务表列表
     * @param onlineTable
     * @param pageParam
     * @return
     */
    @GetMapping("list")
    @ApiOperation("业务表列表")
    public Result<IPage<OnlineTable>> list(OnlineTable onlineTable, @Validated PageParam pageParam){
        return ok(service.findPage(onlineTable, pageParam));
    }
}