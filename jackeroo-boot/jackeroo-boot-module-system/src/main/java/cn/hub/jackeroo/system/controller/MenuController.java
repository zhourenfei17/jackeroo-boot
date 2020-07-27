package cn.hub.jackeroo.system.controller;

import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.system.entity.SysMenu;
import cn.hub.jackeroo.system.service.SysMenuService;
import cn.hub.jackeroo.utils.annotation.ApiModule;
import cn.hub.jackeroo.vo.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     *
     * @return
     */
    @GetMapping("list")
    public Result<SysMenu> list(String name){
        return ok(menuService.getMenuFullTree());
    }
}
