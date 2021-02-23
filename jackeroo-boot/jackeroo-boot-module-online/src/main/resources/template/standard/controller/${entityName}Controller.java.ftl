package ${scheme.packageName}.${module.code}.controller;

import ${scheme.packageName}.persistence.BaseController;
import ${scheme.packageName}.${module.code}.entity.${table.className};
import ${scheme.packageName}.${module.code}.service.${table.className}Service;
<#if scheme.enableSwagger == 1>
import ${scheme.packageName}.utils.annotation.ApiModule;
</#if>
<#if scheme.enableServerValid == 1>
    <#if existUnique>
import ${scheme.packageName}.utils.validator.annotation.ValidatedUnique;
    </#if>
import ${scheme.packageName}.utils.validator.groups.Insert;
import ${scheme.packageName}.utils.validator.groups.Update;
</#if>
import ${scheme.packageName}.vo.Id;
import ${scheme.packageName}.vo.IdList;
import ${scheme.packageName}.vo.PageParam;
import ${scheme.packageName}.vo.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
<#if scheme.enableSwagger == 1>
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
</#if>
<#if scheme.enableSecurity == 1>
import org.apache.shiro.authz.annotation.RequiresPermissions;
</#if>
import org.springframework.beans.factory.annotation.Autowired;
<#if scheme.enableServerValid == 1>
import org.springframework.validation.annotation.Validated;
</#if>
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * ${table.comment} 前端控制器
 * </p>
 *
 * @author ${scheme.author}
 * @since ${createDate}
 */
<#if scheme.enableSwagger == 1>
@ApiModule(moduleName = "${module.name}")
@Api(tags = "${table.comment}")
</#if>
@RestController
@RequestMapping("/${module.code}/${pathName}")
public class ${table.className}Controller extends BaseController {

    @Autowired
    private ${table.className}Service service;

    /**
     * ${table.comment}列表
     * @param ${varName}
     * @param pageParam
     * @return
     */
    @GetMapping("list")
<#if scheme.enableSwagger == 1>
    @ApiOperation("${table.comment}列表")
</#if>
<#if scheme.enableSecurity == 1>
    @RequiresPermissions("${module.code}:${scheme.securitySign}:view")
</#if>
    public Result<IPage<${table.className}>> list(${table.className} ${varName}, <#if scheme.enableServerValid == 1>@Validated </#if>PageParam pageParam){
        return ok(service.findPage(${varName}, pageParam));
    }

    /**
     * 获取${table.comment}详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
<#if scheme.enableSwagger == 1>
    @ApiOperation("获取${table.comment}详情")
</#if>
<#if scheme.enableSecurity == 1>
    @RequiresPermissions("${module.code}:${scheme.securitySign}:view")
</#if>
    public Result<${table.className}> get(@PathVariable String id){
        return ok(service.getById(id));
    }

    /**
     * 保存${table.comment}
     * @param ${varName}
     * @return
     */
    @PostMapping("save")
<#if scheme.enableSwagger == 1>
    @ApiOperation("保存${table.comment}")
</#if>
<#if existUnique && scheme.enableServerValid == 1>
    @ValidatedUnique(clazz = ${table.className}.class)
</#if>
<#if scheme.enableSecurity == 1>
    @RequiresPermissions("${module.code}:${scheme.securitySign}:add")
</#if>
    public Result save(<#if scheme.enableServerValid == 1>@Validated(Insert.class) </#if>@RequestBody ${table.className} ${varName}){
        service.save(${varName});
        return ok();
    }

    /**
     * 更新${table.comment}
     * @param ${varName}
     * @return
     */
    @PutMapping("update")
<#if scheme.enableSwagger == 1>
    @ApiOperation("更新${table.comment}")
</#if>
<#if existUnique && scheme.enableServerValid == 1>
    @ValidatedUnique(clazz = ${table.className}.class)
</#if>
<#if scheme.enableSecurity == 1>
    @RequiresPermissions("${module.code}:${scheme.securitySign}:update")
</#if>
    public Result update(<#if scheme.enableServerValid == 1>@Validated(Update.class) </#if>@RequestBody ${table.className} ${varName}){
        service.updateById(${varName});
        return ok();
    }

    /**
     * 删除${table.comment}
     * @param id
     * @return
     */
    @DeleteMapping("delete")
<#if scheme.enableSwagger == 1>
    @ApiOperation("删除${table.comment}")
</#if>
<#if scheme.enableSecurity == 1>
    @RequiresPermissions("${module.code}:${scheme.securitySign}:delete")
</#if>
    public Result delete(<#if scheme.enableServerValid == 1>@Validated </#if>Id id){
        service.removeById(id.getId());
        return ok();
    }

    /**
     * 批量删除${table.comment}
     * @param id
     * @return
     */
    @DeleteMapping("deleteBatch")
    <#if scheme.enableSwagger == 1>
    @ApiOperation("批量删除${table.comment}")
    </#if>
    <#if scheme.enableSecurity == 1>
    @RequiresPermissions("${module.code}:${scheme.securitySign}:delete")
    </#if>
    public Result deleteBatch(<#if scheme.enableServerValid == 1>@Validated </#if>@RequestBody IdList ids){
        service.removeByIds(ids.getIds());
        return ok();
    }

    /**
     * 导出excel
     * @param response
     * @return
     */
    @GetMapping("exportExcel")
    <#if scheme.enableSwagger == 1>
    @ApiOperation("导出excel")
    </#if>
    <#if scheme.enableSecurity == 1>
    @RequiresPermissions("${module.code}:${scheme.securitySign}:export")
    </#if>
    public void exportExcel(HttpServletResponse response, ${table.className} ${varName}){
        try {
            new ExportExcelWriteBuilder()
                .autoTrim(true)
                .fileAndSheetName("${table.comment}")
                .doWrite(response, service.findList(${varName}), ${table.className}.class);
        } catch (IOException e) {
            ResultUtil.writeJson(response, ResultStatusCode.EXCEL_EXPORT_ERROR);
        }
    }
}
