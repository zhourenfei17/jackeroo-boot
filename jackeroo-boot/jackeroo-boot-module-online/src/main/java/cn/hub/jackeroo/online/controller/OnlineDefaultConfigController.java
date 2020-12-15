package cn.hub.jackeroo.online.controller;

import cn.hub.jackeroo.online.entity.OnlineDefaultConfig;
import cn.hub.jackeroo.online.service.OnlineDefaultConfigService;
import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.utils.annotation.ApiModule;
import cn.hub.jackeroo.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author alex
 * @date 2020/10/27
 */
@Slf4j
@ApiModule(moduleName = "在线开发")
@Api("默认配置")
@RestController
@RequestMapping("/online/default/config")
public class OnlineDefaultConfigController extends BaseController {

    @Autowired
    private OnlineDefaultConfigService service;

    /**
     * 获取默认配置记录
     * @return
     */
    @GetMapping("getConfig")
    @ApiOperation("获取默认配置记录")
    @RequiresPermissions("online:generate:setting")
    public Result<OnlineDefaultConfig> getConfig(){
        return ok(service.getConfig());
    }

    /**
     * 保存默认配置
     * @param onlineDefaultConfig
     * @return
     */
    @PostMapping("save")
    @ApiOperation("保存默认配置")
    @RequiresPermissions("online:generate:setting")
    public Result save(@RequestBody @Validated OnlineDefaultConfig onlineDefaultConfig){
        service.saveOrUpdate(onlineDefaultConfig);

        return ok();
    }
}
