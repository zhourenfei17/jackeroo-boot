package cn.hub.jackeroo.system.controller;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.system.entity.SysMenu;
import cn.hub.jackeroo.system.service.SysMenuService;
import cn.hub.jackeroo.system.vo.Tree;
import cn.hub.jackeroo.system.vo.TreeSelect;
import cn.hub.jackeroo.utils.StringUtils;
import cn.hub.jackeroo.utils.UserUtils;
import cn.hub.jackeroo.utils.annotation.ApiModule;
import cn.hub.jackeroo.utils.validator.groups.Insert;
import cn.hub.jackeroo.utils.validator.groups.Update;
import cn.hub.jackeroo.vo.Id;
import cn.hub.jackeroo.vo.LoginUser;
import cn.hub.jackeroo.vo.PageParam;
import cn.hub.jackeroo.vo.Result;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

import java.util.List;

/**
 * @author alex
 * @date 2020/07/16
 */
@ApiModule(moduleName = "系统管理")
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/system/menu")
@RequiredArgsConstructor
public class MenuController extends BaseController {

    private final SysMenuService menuService;

    /**
     * 获取完整菜单树
     * @return
     */
    @GetMapping("list")
    @ApiOperation(value = "获取完整菜单树")
    @RequiresPermissions("system:menu:view")
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

        return ok(menuService.getMenuByRole(user.getRoleCode()));
    }

    /**
     * 根据id查询菜单详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "菜单详情")
    @RequiresPermissions("system:menu:view")
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
    @RequiresPermissions("system:menu:add")
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
    @RequiresPermissions("system:menu:update")
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
    @RequiresPermissions("system:menu:delete")
    public Result delete(@Validated @RequestBody Id id){
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

    /**
     * 获取叶子菜单的权限标识前缀
     * @param pid
     * @return
     */
    @GetMapping("getPermissionPrefix")
    @ApiOperation(value = "获取叶子菜单的权限标识前缀")
    public Result getPermissionPrefix(String pid){
        LambdaQueryWrapper<SysMenu> query = new LambdaQueryWrapper<>();
        query.eq(SysMenu::getParentId, pid);
        query.eq(SysMenu::getType, SysMenu.TYPE_PERMISSION);
        query.last("limit 1");

        SysMenu menu = menuService.getOne(query, false);

        JSONObject json = new JSONObject();
        if(menu != null && StringUtils.isNotBlank(menu.getPermission())){
            json.put("module", menu.getPermission().split(Constant.SPLIT_SECURITY)[0]);
            json.put("function", menu.getPermission().split(Constant.SPLIT_SECURITY)[1]);
        }else{
            json.put("module", "");
            json.put("function", "");
        }

        return ok(json);
    }
}
