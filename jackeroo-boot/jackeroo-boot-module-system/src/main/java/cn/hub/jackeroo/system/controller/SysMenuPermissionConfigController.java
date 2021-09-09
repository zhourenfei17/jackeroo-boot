package cn.hub.jackeroo.system.controller;


import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.system.entity.SysMenuPermissionConfig;
import cn.hub.jackeroo.system.service.SysMenuPermissionConfigService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 菜单权限配置 前端控制器
 * </p>
 *
 * @author jackeroo
 * @since 2020-08-27
 */
@ApiModule(moduleName = "系统管理")
@Api(tags = "菜单权限配置")
@RestController
@RequestMapping("/system/menu/permission/config")
@RequiredArgsConstructor
public class SysMenuPermissionConfigController extends BaseController {

    private final SysMenuPermissionConfigService permissionConfigService;
    /**
     * 菜单权限配置列表
     * @return
     */
    @GetMapping("list")
    @ApiOperation(value = "菜单权限配置列表", response = Result.class)
    @RequiresPermissions("system:permission:view")
    public Result<IPage<SysMenuPermissionConfig>> list(SysMenuPermissionConfig sysMenuPermissionConfig, @Validated PageParam pageParam){
        return ok(permissionConfigService.findPage(sysMenuPermissionConfig, pageParam));
    }

    /**
     * 获取默认组权限列表
     * @return
     */
    @GetMapping("findDefaultPermissionConfig")
    @ApiOperation(value = "获取默认组权限列表")
    public Result<List<SysMenuPermissionConfig>> findDefaultPermissionConfig(){
        return ok(permissionConfigService.findListByDefaultGroup());
    }

    /**
     * 根据分组id获取权限列表
     * @param groupId
     * @return
     */
    @GetMapping("findPermissionByGroupId")
    @ApiOperation(value = "根据分组id获取权限列表")
    public Result<List<SysMenuPermissionConfig>> findPermissionByGroupId(@RequestParam Long groupId){
        return ok(permissionConfigService.findListByGroupId(groupId));
    }

    /**
     * 根据id查询菜单权限配置详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "菜单权限配置详情", response = Result.class)
    @RequiresPermissions("system:permission:view")
    public Result<SysMenuPermissionConfig> getById(@PathVariable String id){
        return ok(permissionConfigService.getById(id));
    }

    /**
     * 保存菜单权限配置
     * @param sysMenuPermissionConfig
     * @return
     */
    @PostMapping("save")
    @ApiOperation(value = "保存菜单权限配置", response = Result.class)
    @ValidatedUnique(clazz = SysMenuPermissionConfig.class)
    @RequiresPermissions("system:permission:add")
    public Result save(@Validated(Insert.class) @RequestBody SysMenuPermissionConfig sysMenuPermissionConfig){
        permissionConfigService.save(sysMenuPermissionConfig);
        return ok();
    }

    /**
     * 更新菜单权限配置
     * @param sysMenuPermissionConfig
     * @return
     */
    @PutMapping("update")
    @ApiOperation(value = "更新菜单权限配置", response = Result.class)
    @ValidatedUnique(clazz = SysMenuPermissionConfig.class)
    @RequiresPermissions("system:permission:update")
    public Result update(@Validated(Update.class) @RequestBody SysMenuPermissionConfig sysMenuPermissionConfig){
        permissionConfigService.updateById(sysMenuPermissionConfig);
        return ok();
    }

    /**
     * 删除菜单权限配置
     * @param id
     * @return
     */
    @DeleteMapping("delete")
    @ApiOperation(value = "删除菜单权限配置", response = Result.class)
    @RequiresPermissions("system:permission:delete")
    public Result delete(@Validated @RequestBody Id id){
        permissionConfigService.removeById(id.getId());

        return ok();
    }
    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("deleteBatch")
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("system:permission:delete")
    public Result deleteBatch(@Validated @RequestBody IdList ids){
        permissionConfigService.removeByIds(ids.getIds());
        return ok();
    }

    /**
     * 获取最大排序号
     * @param groupId
     * @return
     */
    @GetMapping("getMaxSort")
    @ApiOperation("获取最大排序号")
    public Result getMaxSort(@RequestParam String groupId){
        return ok(permissionConfigService.getMaxSort(groupId));
    }
}
