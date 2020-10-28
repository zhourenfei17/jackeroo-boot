package cn.hub.jackeroo.system.controller;

import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.system.entity.SysMenu;
import cn.hub.jackeroo.system.service.SysMenuService;
import cn.hub.jackeroo.system.vo.Tree;
import cn.hub.jackeroo.system.vo.TreeSelect;
import cn.hub.jackeroo.utils.UserUtils;
import cn.hub.jackeroo.utils.annotation.ApiModule;
import cn.hub.jackeroo.utils.validator.groups.Insert;
import cn.hub.jackeroo.utils.validator.groups.Update;
import cn.hub.jackeroo.vo.Id;
import cn.hub.jackeroo.vo.LoginUser;
import cn.hub.jackeroo.vo.PageParam;
import cn.hub.jackeroo.vo.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

import java.util.List;

/**
 * @author alex
 * @date 2020/07/16
 */
@ApiModule(moduleName = "系统管理")
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/system/menu")
public class MenuController extends BaseController {

    @Autowired
    private SysMenuService menuService;

    /**
     * 获取完整菜单树
     * @return
     */
    @GetMapping("list")
    @ApiOperation(value = "获取完整菜单树")
    public Result<List<SysMenu>> list(String name){
        return ok(menuService.getMenuFullTree(name));
    }

    /**
     * 获取叶子菜单的权限列表
     * @return
     */
    @GetMapping("permission")
    @ApiOperation(value = "获取叶子菜单的权限列表")
    public Result<Page<SysMenu>> permission(String parentId, @Validated PageParam pageParam){
        return ok(menuService.findPermissionPage(Long.parseLong(parentId), pageParam));
    }

    /**
     * 获取角色配置菜单、权限树
     * @return
     */
    @GetMapping("findRolePermissionTree")
    @ApiOperation(value = "获取角色配置菜单、权限树")
    public Result<List<Tree>> findRolePermissionTree(){
        return ok(menuService.getRolePermissionTree());
    }

    /**
     * 通过角色获取菜单、权限o
     * @return
     */
    @GetMapping("getMenuByRole")
    @ApiOperation(value = "通过角色获取菜单、权限")
    public Result getMenuByRole(){
        LoginUser user = UserUtils.getUser();

        return ok(menuService.getMenuByRole(user.getRoleId()));
    }

    /**
     * 根据id查询菜单详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "菜单详情")
    public Result<SysMenu> getById(@PathVariable String id){
        return ok(menuService.getMenuDetail(id));
    }

    /**
     * 保存菜单
     * @param menu
     * @return
     */
    @PostMapping("save")
    @ApiOperation(value = "保存菜单")
    public Result save(@Validated(Insert.class) @RequestBody SysMenu menu){
        menuService.save(menu);
        return ok();
    }

    /**
     * 更新菜单
     * @param menu
     * @return
     */
    @PutMapping("update")
    @ApiOperation(value = "更新菜单")
    public Result update(@Validated(Update.class) @RequestBody SysMenu menu){
        menuService.update(menu);
        return ok();
    }

    /**
     * 删除菜单
     * @param id
     * @return
     */
    @DeleteMapping("delete")
    @ApiOperation(value = "删除菜单")
    public Result delete(@Validated Id id){
        menuService.removeById(id.getId());

        return ok();
    }

    /**
     * 获取下拉框中的菜单树
     * @return
     */
    @GetMapping("getTreeSelect")
    @ApiOperation(value = "获取下拉框中的菜单树")
    public Result<TreeSelect> getTreeSelect(){
        return ok(menuService.getTreeSelect());
    }
}
