package cn.hub.jackeroo.system.controller;

import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.system.entity.SysDict;
import cn.hub.jackeroo.system.service.SysDictService;
import cn.hub.jackeroo.utils.annotation.ApiModule;
import cn.hub.jackeroo.utils.validator.annotation.ValidatedUnique;
import cn.hub.jackeroo.utils.validator.groups.First;
import cn.hub.jackeroo.utils.validator.groups.Insert;
import cn.hub.jackeroo.utils.validator.groups.Second;
import cn.hub.jackeroo.utils.validator.groups.Update;
import cn.hub.jackeroo.vo.Id;
import cn.hub.jackeroo.vo.PageParam;
import cn.hub.jackeroo.vo.Result;
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
* 数据字典 前端控制器
* </p>
*
* @author jackeroo
* @since 2020-10-10
*/
@ApiModule(moduleName = "系统管理")
@Api(tags = "数据字典")
@RestController
@RequestMapping("/system/dict")
public class SysDictController extends BaseController {

    @Autowired
    private SysDictService service;

    /**
    * 数据字典列表
    * @param entity
    * @param pageParam
    * @return
    */
    @GetMapping("list")
    @ApiOperation("数据字典列表")
    public Result<IPage<SysDict>> list(SysDict entity, @Validated PageParam pageParam){
        return ok(service.findPage(entity, pageParam));
    }

    /**
    * 获取数据字典详情
    * @param id
    * @return
    */
    @GetMapping("/{id}")
    @ApiOperation("获取数据字典详情")
    public Result<SysDict> get(@PathVariable String id){
        return ok(service.getById(id));
    }

    /**
    * 添加数据字典
    * @param entity
    * @return
    */
    @PostMapping("add")
    @ApiOperation("添加数据字典")
    @ValidatedUnique(clazz = SysDict.class)
    public Result add(@Validated({Insert.class, First.class}) @RequestBody SysDict entity){
        service.saveDict(entity);
        return ok();
    }

    /**
     * 添加数据字典项
     * @param entity
     * @return
     */
    @PostMapping("addDictItem")
    @ApiOperation("添加数据字典项")
    @ValidatedUnique(clazz = SysDict.class)
    public Result addDictItem(@Validated({Insert.class, Second.class}) @RequestBody SysDict entity){
        service.saveDictItem(entity);
        return ok();
    }

    /**
    * 编辑数据字典
    * @param entity
    * @return
    */
    @PutMapping("update")
    @ApiOperation("编辑数据字典")
    @ValidatedUnique(clazz = SysDict.class)
    public Result update(@Validated({Update.class, First.class}) @RequestBody SysDict entity){
        service.updateById(entity);
        return ok();
    }

    /**
     * 编辑数据字典项
     * @param entity
     * @return
     */
    @PutMapping("updateDictItem")
    @ApiOperation("编辑数据字典项")
    @ValidatedUnique(clazz = SysDict.class)
    public Result updateDictItem(@Validated({Update.class, Second.class}) @RequestBody SysDict entity){
        service.updateById(entity);
        return ok();
    }

    /**
    * 删除数据字典
    * @param id
    * @return
    */
    @DeleteMapping("delete")
    @ApiOperation("删除数据字典")
    public Result delete(@Validated Id id){
        service.removeById(id.getId());
        return ok();
    }
}
