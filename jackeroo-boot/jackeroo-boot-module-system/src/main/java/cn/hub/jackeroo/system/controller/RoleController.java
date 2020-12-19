package cn.hub.jackeroo.system.controller;

import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.system.entity.SysRole;
import cn.hub.jackeroo.system.entity.SysRoleMenu;
import cn.hub.jackeroo.system.query.RolePermission;
import cn.hub.jackeroo.system.service.SysRoleMenuService;
import cn.hub.jackeroo.system.service.SysRoleService;
import cn.hub.jackeroo.system.service.ValidService;
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
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 系统角色相关接口
 * @author alex
 * @date 2020/06/28
 */
@ApiModule(moduleName = "系统管理")
@Api(tags = "角色管理")
@RestController
@RequestMapping("/system/role")
public class RoleController extends BaseController {
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysRoleMenuService roleMenuService;
    /**
     * 角色列表
     * @return
     */
    @GetMapping("list")
    @ApiOperation(value = "角色列表", response = Result.class)
    @RequiresPermissions("system:role:view")
    public Result<IPage<SysRole>> list(SysRole sysRole, @Validated PageParam pageParam){
        return ok(roleService.findPage(sysRole, pageParam));
    }

    /**
     * 所有角色列表
     * @return
     */
    @GetMapping("findAll")
    @ApiOperation(value = "所有角色", response = Result.class)
    public Result<SysRole> findAll(){
        return ok(roleService.list());
    }

    /**
     * 获取角色所拥有的菜单和权限列表
     * @param roleId
     * @return
     */
    @GetMapping("findRoleMenuAndPermission")
    @ApiOperation(value = "获取角色所拥有的菜单和权限列表")
    public Result<List<Long>> findRoleMenuAndPermission(@RequestParam String roleId){
        return ok(roleMenuService.findRoleMenuByRoleId(Long.parseLong(roleId)));
    }

    /**
     * 根据id查询角色详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "角色详情", response = Result.class)
    @RequiresPermissions("system:role:view")
    public Result<SysRole> getById(@PathVariable String id){
        return ok(roleService.getById(id));
    }

    /**
     * 保存角色权限配置
     * @param rolePermission
     * @return
     */
    @PostMapping("saveRolePermission")
    @ApiOperation(value = "保存角色权限配置")
    @RequiresPermissions("system:role:setAuth")
    public Result saveRolePermission(@Validated @RequestBody RolePermission rolePermission){
        SysRole role = roleService.getById(rolePermission.getRoleId());
        if(role != null){
            roleMenuService.saveRolePermission(rolePermission, role);
        }
        return ok();
    }

    /**
     * 保存角色
     * @param role
     * @return
     */
    @PostMapping("save")
    @ApiOperation(value = "保存角色", response = Result.class)
    @ValidatedUnique(clazz = SysRole.class)
    @RequiresPermissions("system:role:add")
    public Result save(@Validated(Insert.class) @RequestBody SysRole role){
        // validService.validEntityUniqueField(role);
        roleService.save(role);
        return ok();
    }

    /**
     * 更新角色
     * @param role
     * @return
     */
    @PutMapping("update")
    @ApiOperation(value = "更新角色", response = Result.class)
    @ValidatedUnique(clazz = SysRole.class)
    @RequiresPermissions("system:role:update")
    public Result update(@Validated(Update.class) @RequestBody SysRole role){
        // validService.validEntityUniqueField(role);
        roleService.updateById(role);
        return ok();
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @DeleteMapping("delete")
    @ApiOperation(value = "删除角色", response = Result.class)
    @RequiresPermissions("system:role:delete")
    public Result delete(@Validated Id id){
        roleService.delete(id.getId());

        return ok();
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("deleteBatch")
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("system:role:delete")
    public Result deleteBatch(@Validated @RequestBody IdList ids){
        roleService.delete(ids.getIds().toArray(new String[]{}));
        return ok();
    }
}
