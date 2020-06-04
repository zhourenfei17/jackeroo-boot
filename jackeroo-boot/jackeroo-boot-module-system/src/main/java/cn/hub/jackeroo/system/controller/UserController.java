package cn.hub.jackeroo.system.controller;

import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.system.service.SysUserService;
import cn.hub.jackeroo.vo.PageParam;
import cn.hub.jackeroo.vo.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author alex
 * @date 2020/06/01
 */
@Api(value = "系统用户模块")
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
    @ApiOperation(value = "获取用户列表", response = Result.class)
    public Result list(PageParam pageParam){
        Page page = pageParam.getPage();
        IPage pageList = userService.page(page);
        return ok(pageList);
    }
}
