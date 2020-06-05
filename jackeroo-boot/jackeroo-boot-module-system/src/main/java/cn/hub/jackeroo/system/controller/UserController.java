package cn.hub.jackeroo.system.controller;

import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.system.entity.SysUser;
import cn.hub.jackeroo.system.service.SysUserService;
import cn.hub.jackeroo.utils.groups.First;
import cn.hub.jackeroo.vo.Id;
import cn.hub.jackeroo.vo.PageParam;
import cn.hub.jackeroo.vo.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统用户模块接口
 * @author alex
 * @date 2020/06/01
 */
@Api(value = "系统用户模块", description = "系统用户模块api", tags = "系统管理", position = 1)
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private SysUserService userService;

    /**
     * 用户列表
     * @return
     */
    @GetMapping("list")
    @ApiOperation(value = "用户列表", notes = "根据条件查询用户列表", response = Result.class)
    public Result list(PageParam pageParam){
        Page page = pageParam.getPage();
        IPage pageList = userService.page(page);
        return ok(pageList);
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @PostMapping("add")
    @ApiOperation(value = "添加用户", notes = "添加用户信息")
    public Result add(@Validated SysUser user){
        userService.save(user);
        return ok();
    }

    /**
     * 编辑用户
     * @param user
     * @return
     */
    @PutMapping("edit")
    @ApiOperation(value = "编辑用户", notes = "编辑用户信息")
    public Result edit(@Validated(First.class) SysUser user){
        userService.updateById(user);
        return ok();
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping("delete")
    @ApiOperation(value = "删除用户", notes = "根据id删除用户")
    public Result delete(@Validated Id id){
        userService.removeById(id.getId());

        return ok();
    }
}
