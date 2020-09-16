package cn.hub.jackeroo.system.controller;


import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.system.entity.SysModule;
import cn.hub.jackeroo.system.service.SysModuleService;
import cn.hub.jackeroo.utils.annotation.ApiModule;
import cn.hub.jackeroo.utils.validator.annotation.ValidatedUnique;
import cn.hub.jackeroo.utils.validator.groups.Insert;
import cn.hub.jackeroo.utils.validator.groups.Update;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统模块信息 前端控制器
 * </p>
 *
 * @author jackeroo
 * @since 2020-09-16
 */
@ApiModule(moduleName = "系统管理")
@Api(tags = "系统模块管理")
@RestController
@RequestMapping("/system/module")
public class SysModuleController extends BaseController {

    @Autowired
    private SysModuleService service;

    /**
     * 系统模块列表
     * @param module
     * @param pageParam
     * @return
     */
    @GetMapping("list")
    @ApiOperation("系统模块列表")
    public Result<IPage<SysModule>> list(SysModule module, @Validated PageParam pageParam){
        return ok(service.findPage(module, pageParam));
    }

    /**
     * 获取模块信息详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("获取模块信息详情")
    public Result<SysModule> get(@PathVariable String id){
        return ok(service.getById(id));
    }

    /**
     * 添加模块
     * @param module
     * @return
     */
    @PostMapping("add")
    @ApiOperation("添加模块")
    @ValidatedUnique(clazz = SysModule.class)
    public Result add(@Validated(Insert.class) @RequestBody SysModule module){
        service.save(module);
        return ok();
    }

    /**
     * 编辑模块
     * @param module
     * @return
     */
    @PutMapping("update")
    @ApiOperation("编辑模块")
    @ValidatedUnique(clazz = SysModule.class)
    public Result update(@Validated(Update.class) @RequestBody SysModule module){
        service.updateById(module);
        return ok();
    }

    /**
     * 删除模块
     * @param id
     * @return
     */
    @DeleteMapping("delete")
    @ApiOperation("删除模块")
    public Result delete(@Validated Id id){
        service.removeById(id.getId());
        return ok();
    }
}
