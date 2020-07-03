package cn.hub.jackeroo.system.controller;

import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.system.query.UniqueVo;
import cn.hub.jackeroo.system.service.ValidService;
import cn.hub.jackeroo.utils.StringUtils;
import cn.hub.jackeroo.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给前端校验用的接口
 * @author alex
 * @date 2020/06/29
 */
@RestController
@RequestMapping("/system/valid")
public class ValidController extends BaseController {

    @Autowired
    private ValidService service;

    @Value("${mybatis-plus.configuration.map-underscore-to-camel-case}")
    private Boolean mapUnderscoreToCamelCase;
    /**
     * 验证字段是否唯一
     * @param uniqueVo
     * @return
     */
    @GetMapping("unique")
    @ApiOperation(value = "校验表单一字段唯一性", tags = "校验管理")
    public Result unique(UniqueVo uniqueVo){
        // 如果开启了驼峰命名规则，则需要将columnName进行转换
        if(mapUnderscoreToCamelCase){
            uniqueVo.setColumnName(StringUtils.toUnderScoreCase(uniqueVo.getColumnName()));
        }
        return ok(service.isUnique(uniqueVo));
    }
}
