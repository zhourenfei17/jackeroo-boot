package cn.hub.jackeroo.system.controller;


import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.system.entity.SysModule;
import cn.hub.jackeroo.system.service.SysModuleService;
import cn.hub.jackeroo.utils.annotation.ApiModule;
import cn.hub.jackeroo.utils.validator.annotation.ValidatedUnique;
import cn.hub.jackeroo.utils.validator.groups.Insert;
import cn.hub.jackeroo.utils.validator.groups.Update;
import cn.hub.jackeroo.vo.Id;
import cn.hub.jackeroo.vo.IdList;
import cn.hub.jackeroo.vo.PageParam;
import cn.hub.jackeroo.vo.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
@RequiredArgsConstructor
public class SysModuleController extends BaseController {

    private final SysModuleService service;

    /**
     * 系统模块列表
     * @param module
     * @param pageParam
     * @return
     */
    @GetMapping("list")
    @ApiOperation("系统模块列表")
    @RequiresPermissions("system:module:view")
    public Result<IPage<SysModule>> list(SysModule module, @Validated PageParam pageParam){
        return ok(service.findPage(module, pageParam));
    }

    /**
     * 获取所有系统模块列表
     * @return
     */
    @GetMapping("allList")
    @ApiOperation("获取所有系统模块列表")
    public Result allList(){
        return ok(service.findAll());
    }

    /**
     * 获取当前排序号
     * @return
     */
    @GetMapping("getMaxSort")
    @ApiOperation("获取当前排序号")
    public Result<Integer> getMaxSort(){
        return ok(service.getMaxSort());
    }

    /**
     * 获取模块信息详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("获取模块信息详情")
    @RequiresPermissions("system:module:view")
    public Result<SysModule> get(@PathVariable String id){
        return ok(service.getById(id));
    }

    /**
     * 保存模块
     * @param module
     * @return
     */
    @PostMapping("save")
    @ApiOperation("保存模块")
    @ValidatedUnique(clazz = SysModule.class)
    @RequiresPermissions("system:module:add")
    public Result save(@Validated(Insert.class) @RequestBody SysModule module){
        service.save(module);
        return ok();
    }

    /**
     * 更新模块
     * @param module
     * @return
     */
    @PutMapping("update")
    @ApiOperation("更新模块")
    @ValidatedUnique(clazz = SysModule.class)
    @RequiresPermissions("system:module:update")
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
    @RequiresPermissions("system:module:delete")
    public Result delete(@Validated @RequestBody Id id){
        service.removeById(id.getId());
        return ok();
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("deleteBatch")
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("system:module:delete")
    public Result deleteBatch(@Validated @RequestBody IdList ids){
        service.removeByIds(ids.getIds());
        return ok();
    }
}
