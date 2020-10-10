package ${scheme.packageName}.${module.code}.controller;

import ${scheme.packageName}.persistence.BaseController;
import ${scheme.packageName}.${module.code}.entity.${table.className};
import ${scheme.packageName}.${module.code}.service.${table.className}Service;
import ${scheme.packageName}.utils.annotation.ApiModule;
import ${scheme.packageName}.utils.validator.annotation.ValidatedUnique;
import ${scheme.packageName}.utils.validator.groups.Insert;
import ${scheme.packageName}.utils.validator.groups.Update;
import ${scheme.packageName}.vo.Id;
import ${scheme.packageName}.vo.PageParam;
import ${scheme.packageName}.vo.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

/**
* <p>
* ${table.comment} 前端控制器
* </p>
*
* @author ${scheme.author}
* @since ${createDate}
*/
@ApiModule(moduleName = "${module.name}")
@Api(tags = "${table.comment}")
@RestController
@RequestMapping("/${module.code}/module")
public class ${table.className}Controller extends BaseController {

    @Autowired
    private ${table.className}Service service;

    /**
    * ${table.comment}列表
    * @param entity
    * @param pageParam
    * @return
    */
    @GetMapping("list")
    @ApiOperation("${table.comment}列表")
    public Result<IPage<${table.className}>> list(${table.className} entity, @Validated PageParam pageParam){
        return ok(service.findPage(entity, pageParam));
    }

    /**
    * 获取${table.comment}详情
    * @param id
    * @return
    */
    @GetMapping("/{id}")
    @ApiOperation("获取${table.comment}详情")
    public Result<${table.className}> get(@PathVariable String id){
        return ok(service.getById(id));
    }

    /**
    * 添加${table.comment}
    * @param entity
    * @return
    */
    @PostMapping("add")
    @ApiOperation("添加${table.comment}")
    @ValidatedUnique(clazz = ${table.className}.class)
    public Result add(@Validated(Insert.class) @RequestBody ${table.className} entity){
        service.save(entity);
        return ok();
    }

    /**
    * 编辑${table.comment}
    * @param entity
    * @return
    */
    @PutMapping("update")
    @ApiOperation("编辑${table.comment}")
    @ValidatedUnique(clazz = ${table.className}.class)
    public Result update(@Validated(Update.class) @RequestBody ${table.className} entity){
        service.updateById(entity);
        return ok();
    }

    /**
    * 删除${table.comment}
    * @param id
    * @return
    */
    @DeleteMapping("delete")
    @ApiOperation("删除${table.comment}")
    public Result delete(@Validated Id id){
        service.removeById(id.getId());
        return ok();
    }
}
