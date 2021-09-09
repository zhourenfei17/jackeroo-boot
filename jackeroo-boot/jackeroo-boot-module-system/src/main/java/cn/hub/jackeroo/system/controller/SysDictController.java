package cn.hub.jackeroo.system.controller;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.system.entity.SysDict;
import cn.hub.jackeroo.system.service.SysDictService;
import cn.hub.jackeroo.utils.StringUtils;
import cn.hub.jackeroo.utils.annotation.ApiModule;
import cn.hub.jackeroo.utils.validator.annotation.ValidatedUnique;
import cn.hub.jackeroo.utils.validator.groups.First;
import cn.hub.jackeroo.utils.validator.groups.Insert;
import cn.hub.jackeroo.utils.validator.groups.Second;
import cn.hub.jackeroo.utils.validator.groups.Update;
import cn.hub.jackeroo.vo.Id;
import cn.hub.jackeroo.vo.IdList;
import cn.hub.jackeroo.vo.PageParam;
import cn.hub.jackeroo.vo.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@RequiredArgsConstructor
public class SysDictController extends BaseController {

    private final SysDictService service;

    /**
    * 数据字典列表
    * @param entity
    * @param pageParam
    * @return
    */
    @GetMapping("list")
    @ApiOperation("数据字典列表")
    @RequiresPermissions("system:dict:view")
    public Result<IPage<SysDict>> list(SysDict entity, @Validated PageParam pageParam){
        entity.setType(SysDict.TYPE_DICT);
        if(StringUtils.isEmpty(pageParam.getSortField())){
            pageParam.setSortField("createTime");
            pageParam.setSortOrder(Constant.SORT_DESC);
        }
        return ok(service.findPage(entity, pageParam));
    }

    /**
     * 根据字典code获取字典详情
     * @param dictCode
     * @return
     */
    @GetMapping("getByDictCode")
    @ApiOperation("根据字典code获取字典详情")
    public Result<SysDict> getByDictCode(@RequestParam String dictCode){
        return ok(service.getByDictCode(dictCode));
    }

    /**
     * 字典项列表
     * @param pageParam
     * @param dictCode
     * @return
     */
    @GetMapping("itemList")
    @ApiOperation("字典项列表")
    @RequiresPermissions("system:dict:view")
    public Result<IPage<SysDict>> itemList(@Validated PageParam pageParam, @RequestParam String dictCode){
        SysDict sysDict = new SysDict();
        sysDict.setType(SysDict.TYPE_DICT_ITEM);
        sysDict.setDictCode(dictCode);
        return ok(service.findPage(sysDict, pageParam));
    }

    /**
     * 通过字典code获取所有字典项列表
     * @param dictCode
     * @return
     */
    @GetMapping("getDictItemList")
    @ApiOperation("通过字典code获取所有字典项列表")
    public Result<List<SysDict>> getDictItemList(@RequestParam String dictCode){
        return ok(service.findDictItemByDictCode(dictCode));
    }

    /**
     * 通过多个code获取字典项列表
     * @param dictCode
     * @return
     */
    @GetMapping("getDictItemByCodes")
    @ApiOperation("通过多个code获取字典项列表")
    public Result<Map<String, List<SysDict>>> getDictItemByCodes(@RequestParam List<String> dictCode){
        Map<String, List<SysDict>> result = new HashMap<>();
        for (String code : dictCode) {
            result.put(code, service.findDictItemByDictCode(code));
        }
        return ok(result);
    }

    /**
     * 获取当前最大排序号
     * @return
     */
    @GetMapping("getMaxSort")
    @ApiOperation("获取当前最大排序号")
    public Result getMaxSort(@RequestParam String dictCode){
        return ok(service.getMaxSort(dictCode));
    }

    /**
    * 获取数据字典详情
    * @param id
    * @return
    */
    @GetMapping("/{id}")
    @ApiOperation("获取数据字典详情")
    @RequiresPermissions("system:dict:view")
    public Result<SysDict> get(@PathVariable String id){
        return ok(service.getById(id));
    }

    /**
    * 保存数据字典
    * @param entity
    * @return
    */
    @PostMapping("save")
    @ApiOperation("保存数据字典")
    @ValidatedUnique(clazz = SysDict.class, condition = "type=0", groups = Second.class)
    @RequiresPermissions("system:dict:add")
    public Result save(@Validated({Insert.class, First.class}) @RequestBody SysDict entity){
        service.saveDict(entity);
        return ok();
    }

    /**
     * 保存数据字典项
     * @param entity
     * @return
     */
    @PostMapping("saveDictItem")
    @ApiOperation("保存数据字典项")
    @ValidatedUnique(clazz = SysDict.class, condition = "type=1 and dict_code = #{dictCode}", groups = First.class)
    @RequiresPermissions("system:dict:add")
    public Result saveDictItem(@Validated({Insert.class, Second.class}) @RequestBody SysDict entity){
        service.saveDictItem(entity);
        return ok();
    }

    /**
    * 更新数据字典
    * @param entity
    * @return
    */
    @PutMapping("update")
    @ApiOperation("更新数据字典")
    @ValidatedUnique(clazz = SysDict.class)
    @RequiresPermissions("system:dict:edit")
    public Result update(@Validated({Update.class, First.class}) @RequestBody SysDict entity){
        service.updateDict(entity);
        return ok();
    }

    /**
     * 更新数据字典项
     * @param entity
     * @return
     */
    @PutMapping("updateDictItem")
    @ApiOperation("更新数据字典项")
    @ValidatedUnique(clazz = SysDict.class)
    @RequiresPermissions("system:dict:edit")
    public Result updateDictItem(@Validated({Update.class, Second.class}) @RequestBody SysDict entity){
        service.updateDictItem(entity);
        return ok();
    }

    /**
     * 删除字典信息
     * @param id
     * @return
     */
    @DeleteMapping("deleteDict")
    @ApiOperation("删除字典信息")
    @RequiresPermissions("system:dict:delete")
    public Result deleteDict(@Validated @RequestBody Id id){
        service.delete(id.getId());
        return ok();
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("deleteBatch")
    @ApiOperation(value = "批量删除")
    @RequiresPermissions("system:dict:delete")
    public Result deleteBatch(@Validated @RequestBody IdList ids){
        service.delete(ids.getIds().toArray(new String[]{}));
        return ok();
    }

    /**
    * 删除字典项信息
    * @param id
    * @return
    */
    @DeleteMapping("deleteDictItem")
    @ApiOperation("删除字典项信息")
    @RequiresPermissions("system:dict:delete")
    public Result deleteDictItem(@Validated @RequestBody Id id){
        service.delete(id.getId());
        return ok();
    }
}
