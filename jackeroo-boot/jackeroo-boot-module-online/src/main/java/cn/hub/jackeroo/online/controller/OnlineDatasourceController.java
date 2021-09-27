package cn.hub.jackeroo.online.controller;

import cn.hub.jackeroo.enums.ResultStatusCode;
import cn.hub.jackeroo.online.entity.OnlineDatasource;
import cn.hub.jackeroo.online.service.OnlineDatasourceService;
import cn.hub.jackeroo.online.vo.DataSourceVo;
import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.utils.ResultUtil;
import cn.hub.jackeroo.utils.StringUtils;
import cn.hub.jackeroo.utils.annotation.ApiModule;
import cn.hub.jackeroo.utils.easyexcel.model.ExportExcelWriteBuilder;
import cn.hub.jackeroo.utils.encrypt.AESUtils;
import cn.hub.jackeroo.utils.validator.groups.Insert;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 数据源配置 前端控制器
 * </p>
 *
 * @author alex
 * @since 2021-08-21
 */
@ApiModule(moduleName = "代码生成")
@Api(tags = "数据源配置")
@RestController
@RequestMapping("/online/datasource")
@RequiredArgsConstructor
public class OnlineDatasourceController extends BaseController {

    private final OnlineDatasourceService service;

    /**
     * 数据源配置列表
     * @param onlineDatasource
     * @param pageParam
     * @return
     */
    @GetMapping("list")
    @ApiOperation("数据源配置列表")
    @RequiresPermissions("online:datasource:view")
    public Result<IPage<OnlineDatasource>> list(OnlineDatasource onlineDatasource, @Validated PageParam pageParam){
        return ok(service.findPage(onlineDatasource, pageParam));
    }

    /**
     * 数据源列表
     * @return
     */
    @GetMapping("allList")
    @ApiOperation("数据源列表")
    public Result<List<OnlineDatasource>> allList(){
        return ok(service.list());
    }

    /**
     * 获取数据源配置详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("获取数据源配置详情")
    @RequiresPermissions("online:datasource:view")
    public Result<OnlineDatasource> get(@PathVariable String id){
        return ok(service.getById(id));
    }

    /**
     * 保存数据源配置
     * @param onlineDatasource
     * @return
     */
    @PostMapping("save")
    @ApiOperation("保存数据源配置")
    @RequiresPermissions("online:datasource:add")
    public Result save(@Validated(Insert.class) @RequestBody OnlineDatasource onlineDatasource){
        onlineDatasource.setSlat(StringUtils.randomGen(16));
        onlineDatasource.setPassword(AESUtils.encrypt(onlineDatasource.getPassword(), onlineDatasource.getSlat()));
        service.save(onlineDatasource);
        return ok();
    }

    /**
     * 更新数据源配置
     * @param onlineDatasource
     * @return
     */
    @PutMapping("update")
    @ApiOperation("更新数据源配置")
    @RequiresPermissions("online:datasource:update")
    public Result update(@Validated(Update.class) @RequestBody OnlineDatasource onlineDatasource){
        service.updateDatasource(onlineDatasource);
        return ok();
    }

    /**
     * 删除数据源配置
     * @param id
     * @return
     */
    @DeleteMapping("delete")
    @ApiOperation("删除数据源配置")
    @RequiresPermissions("online:datasource:delete")
    public Result delete(@Validated @RequestBody Id id){
        service.removeById(id.getId());
        return ok();
    }

    /**
     * 批量删除数据源配置
     * @param ids
     * @return
     */
    @DeleteMapping("deleteBatch")
    @ApiOperation("批量删除数据源配置")
    @RequiresPermissions("online:datasource:delete")
    public Result deleteBatch(@Validated @RequestBody IdList ids){
        service.removeByIds(ids.getIds());
        return ok();
    }

    /**
     * 导出excel
     * @param response
     * @return
     */
    @GetMapping("exportExcel")
    @ApiOperation("导出excel")
    @RequiresPermissions("online:datasource:export")
    public void exportExcel(HttpServletResponse response, OnlineDatasource onlineDatasource){
        try {
            new ExportExcelWriteBuilder()
                    .autoTrim(true)
                    .fileAndSheetName("数据源配置")
                    .doWrite(response, service.findList(onlineDatasource), OnlineDatasource.class);
        } catch (IOException e) {
            ResultUtil.writeJson(response, ResultStatusCode.EXCEL_EXPORT_ERROR);
        }
    }

    /**
     * 数据源连接测试
     * @param dataSourceVo
     * @return
     */
    @GetMapping("testConnect")
    @ApiOperation("数据源连接测试")
    @RequiresPermissions({"online:datasource:add", "online:datasource:update"})
    public Result testConnect(@Validated DataSourceVo dataSourceVo){
        if(!service.testConnect(dataSourceVo.getUrl(), dataSourceVo.getUsername(), dataSourceVo.getPassword(), dataSourceVo.getDriverClassName())){
            return error("连接失败！");
        }
        return ok();
    }
}
