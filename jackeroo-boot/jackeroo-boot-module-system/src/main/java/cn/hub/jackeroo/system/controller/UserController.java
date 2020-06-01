package cn.hub.jackeroo.system.controller;

import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.system.service.SysUserService;
import cn.hub.jackeroo.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author alex
 * @date 2020/06/01
 */
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
    public Result list(){
        return ok(userService.list());
    }
}
