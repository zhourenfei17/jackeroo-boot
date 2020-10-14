package cn.hub.jackeroo.online.controller;

import cn.hub.jackeroo.online.entity.OnlineTable;
import cn.hub.jackeroo.online.service.OnlineTableService;
import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.utils.annotation.ApiModule;
import cn.hub.jackeroo.vo.Id;
import cn.hub.jackeroo.vo.PageParam;
import cn.hub.jackeroo.vo.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    /**
     * 获取业务表生成代码详细信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("获取业务表生成代码详细信息")
    public Result get(@PathVariable String id){
        return ok(service.get(id));
    }

    /**
     * 删除业务表配置
     * @return
     */
    @DeleteMapping("delete")
    @ApiOperation("删除业务表配置")
    public Result delete(@Validated Id id){
        service.delete(id.getId());

        return ok();
    }
}
