package cn.hub.jackeroo.system.controller;

import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.system.entity.SysRole;
import cn.hub.jackeroo.system.service.SysRoleService;
import cn.hub.jackeroo.system.service.ValidService;
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
    private ValidService validService;
    /**
     * 角色列表
     * @return
     */
    @GetMapping("list")
    @ApiOperation(value = "角色列表", response = Result.class)
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
     * 根据id查询角色详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "角色详情", response = Result.class)
    public Result<SysRole> getById(@PathVariable String id){
        return ok(roleService.getById(id));
    }

    /**
     * 添加角色
     * @param role
     * @return
     */
    @PostMapping("add")
    @ApiOperation(value = "添加角色", response = Result.class)
    @ValidatedUnique(clazz = SysRole.class)
    public Result add(@Validated(Insert.class) @RequestBody SysRole role){
        // validService.validEntityUniqueField(role);
        roleService.save(role);
        return ok();
    }

    /**
     * 编辑角色
     * @param role
     * @return
     */
    @PutMapping("update")
    @ApiOperation(value = "编辑角色", response = Result.class)
    @ValidatedUnique(clazz = SysRole.class)
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
    public Result delete(@Validated Id id){
        roleService.removeById(id.getId());

        return ok();
    }
}
