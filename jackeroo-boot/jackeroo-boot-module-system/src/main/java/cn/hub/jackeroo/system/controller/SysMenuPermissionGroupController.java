package cn.hub.jackeroo.system.controller;


import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.system.entity.SysMenuPermissionGroup;
import cn.hub.jackeroo.system.service.SysMenuPermissionGroupService;
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
 * 菜单权限配置组别 前端控制器
 * </p>
 *
 * @author jackeroo
 * @since 2020-08-27
 */
@ApiModule(moduleName = "系统管理")
@Api(tags = "菜单权限配置组别")
@RestController
@RequestMapping("/system/menu/permission/group")
public class SysMenuPermissionGroupController extends BaseController {

    @Autowired
    private SysMenuPermissionGroupService permissionGroupService;
    /**
     * 菜单权限配置组别列表
     * @return
     */
    @GetMapping("list")
    @ApiOperation(value = "菜单权限配置组别列表", response = Result.class)
    public Result<IPage<SysMenuPermissionGroup>> list(SysMenuPermissionGroup sysMenuPermissionGroup, @Validated PageParam pageParam){
        return ok(permissionGroupService.findPage(sysMenuPermissionGroup, pageParam));
    }


    /**
     * 根据id查询菜单权限配置组别详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "菜单权限配置组别详情", response = Result.class)
    public Result<SysMenuPermissionGroup> getById(@PathVariable String id){
        return ok(permissionGroupService.getById(id));
    }

    /**
     * 添加菜单权限配置组别
     * @param sysMenuPermissionGroup
     * @return
     */
    @PostMapping("add")
    @ApiOperation(value = "添加菜单权限配置组别", response = Result.class)
    @ValidatedUnique(clazz = SysMenuPermissionGroup.class)
    public Result add(@Validated(Insert.class) @RequestBody SysMenuPermissionGroup sysMenuPermissionGroup){
        permissionGroupService.save(sysMenuPermissionGroup);
        return ok();
    }

    /**
     * 编辑菜单权限配置组别
     * @param sysMenuPermissionGroup
     * @return
     */
    @PutMapping("update")
    @ApiOperation(value = "编辑菜单权限配置组别", response = Result.class)
    @ValidatedUnique(clazz = SysMenuPermissionGroup.class)
    public Result update(@Validated(Update.class) @RequestBody SysMenuPermissionGroup sysMenuPermissionGroup){
        permissionGroupService.updateById(sysMenuPermissionGroup);
        return ok();
    }

    /**
     * 删除菜单权限配置组别
     * @param id
     * @return
     */
    @DeleteMapping("delete")
    @ApiOperation(value = "删除菜单权限配置组别", response = Result.class)
    public Result delete(@Validated Id id){
        permissionGroupService.removeById(id.getId());

        return ok();
    }
}
