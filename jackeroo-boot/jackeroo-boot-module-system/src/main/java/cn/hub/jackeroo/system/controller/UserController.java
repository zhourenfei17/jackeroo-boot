package cn.hub.jackeroo.system.controller;

import cn.hub.jackeroo.constant.ParamType;
import cn.hub.jackeroo.enums.ResultStatusCode;
import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.system.entity.SysUser;
import cn.hub.jackeroo.system.service.SysMenuService;
import cn.hub.jackeroo.system.service.SysUserService;
import cn.hub.jackeroo.utils.ResultUtil;
import cn.hub.jackeroo.utils.UserUtils;
import cn.hub.jackeroo.utils.annotation.ApiModule;
import cn.hub.jackeroo.utils.easyexcel.EasyExcelUtils;
import cn.hub.jackeroo.utils.easyexcel.model.ExportExcelWriteBuilder;
import cn.hub.jackeroo.utils.validator.annotation.ValidatedUnique;
import cn.hub.jackeroo.utils.validator.groups.Insert;
import cn.hub.jackeroo.utils.validator.groups.Update;
import cn.hub.jackeroo.vo.Id;
import cn.hub.jackeroo.vo.IdList;
import cn.hub.jackeroo.vo.LoginUser;
import cn.hub.jackeroo.vo.PageParam;
import cn.hub.jackeroo.vo.Result;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * 系统用户相关接口
 * @author alex
 * @date 2020/06/01
 */
@Slf4j
@ApiModule(moduleName = "系统管理")
@Api(tags = "用户管理")
@RestController
@RequestMapping("/system/user")
public class UserController extends BaseController {
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysMenuService menuService;

    /**
     * 用户列表
     * @return
     */
    @GetMapping("list")
    @ApiOperation(value = "用户列表", notes = "根据条件查询用户列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "name", value = "名称", paramType = ParamType.QUERY),
        @ApiImplicitParam(name = "account", value = "登录账号", paramType = ParamType.QUERY),
        @ApiImplicitParam(name = "phone", value = "手机号", paramType = ParamType.QUERY)
    })
    @RequiresPermissions("system:user:view")
    public Result<IPage<SysUser>> list(@ApiIgnore SysUser sysUser, @Validated PageParam pageParam){
        return ok(userService.findPage(sysUser, pageParam));
    }

    /**
     * 获取当前登录用户详细信息
     * @return
     */
    @GetMapping("info")
    @ApiOperation(value = "获取当前登录用户详细信息")
    public Result<SysUser> info(){
        LoginUser user = UserUtils.getUser();
        SysUser userInfo = userService.findById(user.getId());
        userInfo.setPassword(null);
        userInfo.setPermissionList(menuService.getPermissionByRole(user.getRoleCode()));
        return ok(userInfo);
    }

    /**
     * 根据id查询用户详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "用户详情", notes = "根据id获取用户详情")
    @RequiresPermissions("system:user:view")
    public Result<SysUser> getById(@PathVariable String id){
        SysUser user = userService.findById(id);
        user.setPassword(null);
        return ok(user);
    }

    /**
     * 保存用户
     * @param user
     * @return
     */
    @PostMapping("save")
    @ApiOperation(value = "保存用户", notes = "保存用户信息")
    @ValidatedUnique(clazz = SysUser.class)
    @RequiresPermissions("system:user:add")
    public Result save(@Validated(Insert.class) @RequestBody SysUser user){
        userService.insertUser(user);
        return ok();
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @PutMapping("update")
    @ApiOperation(value = "更新用户", notes = "更新用户信息")
    @ValidatedUnique(clazz = SysUser.class, groups = Update.class)
    @RequiresPermissions("system:user:update")
    public Result update(@Validated(Update.class) @RequestBody SysUser user){
        // SysUser sysUser = new SysUser();
        // BeanUtils.copyProperties(user, sysUser);
        // 编辑用户无法修改密码和账号
        // sysUser.setPassword(null);
        // sysUser.setAccount(null);
        // userService.updateById(sysUser);
        userService.updateUser(user);
        return ok();
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping("delete")
    @ApiOperation(value = "删除用户", notes = "根据id删除用户")
    @RequiresPermissions("system:user:delete")
    public Result delete(@Validated Id id){
        userService.removeById(id.getId());

        return ok();
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("deleteBatch")
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("system:user:delete")
    public Result deleteBatch(@Validated @RequestBody IdList ids){
        userService.removeByIds(ids.getIds());
        return ok();
    }

    /**
     * 冻结用户
     * @param id
     * @return
     */
    @PutMapping("frozen")
    @ApiOperation(value = "冻结用户")
    @RequiresPermissions("system:user:frozen")
    public Result frozen(@Validated @RequestBody Id id){
        userService.frozenUser(id.getId());

        return ok();
    }

    /**
     * 解冻用户
     * @param id
     * @return
     */
    @PutMapping("unfrozen")
    @ApiOperation(value = "解冻用户")
    @RequiresPermissions("system:user:frozen")
    public Result unfrozen(@Validated @RequestBody Id id){
        userService.unfrozenUser(id.getId());

        return ok();
    }

    /**
     * 重置密码
     * @param id
     * @return
     */
    @PutMapping("resetPwd")
    @ApiOperation(value = "重置密码")
    @RequiresPermissions("system:user:reset")
    public Result resetPassword(@Validated @RequestBody Id id){
        userService.resetPassword(id.getId());

        return ok();
    }

    /**
     * 批量冻结用户
     * @param ids
     * @return
     */
    @PutMapping("frozenBatch")
    @ApiOperation("批量冻结用户")
    @RequiresPermissions("system:user:frozen")
    public Result frozenBatch(@Validated @RequestBody IdList ids){
        userService.frozenUser(ids.getIds().toArray(new String[]{}));
        return ok();
    }

    /**
     * 批量解冻用户
     * @param ids
     * @return
     */
    @PutMapping("unfrozenBatch")
    @ApiOperation("批量解冻用户")
    @RequiresPermissions("system:user:frozen")
    public Result unfrozenBatch(@Validated @RequestBody IdList ids){
        userService.unfrozenUser(ids.getIds().toArray(new String[]{}));
        return ok();
    }

    /**
     * 导出excel
     * @param response
     * @return
     */
    @GetMapping("exportExcel")
    public void exportExcel(HttpServletResponse response){
        try {
            new ExportExcelWriteBuilder().autoTrim(true).needHead(false).fileAndSheetName("用户信息").doWrite(response, userService.list(), SysUser.class);
        } catch (IOException e) {
            ResultUtil.writeJson(response, ResultStatusCode.EXCEL_EXPORT_ERROR);
        }
    }
}
