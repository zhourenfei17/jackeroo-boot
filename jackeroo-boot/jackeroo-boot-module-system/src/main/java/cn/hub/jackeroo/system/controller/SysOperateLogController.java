package cn.hub.jackeroo.system.controller;

import cn.hub.jackeroo.enums.ResultStatusCode;
import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.system.entity.SysOperateLog;
import cn.hub.jackeroo.system.service.SysOperateLogService;
import cn.hub.jackeroo.utils.ResultUtil;
import cn.hub.jackeroo.utils.easyexcel.model.ExportExcelWriteBuilder;
import cn.hub.jackeroo.vo.Id;
import cn.hub.jackeroo.vo.IdList;
import cn.hub.jackeroo.vo.PageParam;
import cn.hub.jackeroo.vo.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 系统操作日志 前端控制器
 * </p>
 *
 * @author alex
 * @since 2021-10-11
 */
@RestController
@RequestMapping("/system/operate/log")
@RequiredArgsConstructor
public class SysOperateLogController extends BaseController {

    private final SysOperateLogService service;

    /**
     * 系统操作日志列表
     * @param sysOperateLog
     * @param pageParam
     * @return
     */
    @GetMapping("list")
    @RequiresPermissions("system:log:view")
    public Result<IPage<SysOperateLog>> list(SysOperateLog sysOperateLog, PageParam pageParam){
        return ok(service.findPage(sysOperateLog, pageParam));
    }

    /**
     * 获取系统操作日志详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @RequiresPermissions("system:log:view")
    public Result<SysOperateLog> get(@PathVariable String id){
        return ok(service.getById(id));
    }

    /**
     * 保存系统操作日志
     * @param sysOperateLog
     * @return
     */
    @PostMapping("save")
    @RequiresPermissions("system:log:add")
    public Result save(@RequestBody SysOperateLog sysOperateLog){
        service.save(sysOperateLog);
        return ok();
    }

    /**
     * 更新系统操作日志
     * @param sysOperateLog
     * @return
     */
    @PutMapping("update")
    @RequiresPermissions("system:log:update")
    public Result update(@RequestBody SysOperateLog sysOperateLog){
        service.updateById(sysOperateLog);
        return ok();
    }

    /**
     * 删除系统操作日志
     * @param id
     * @return
     */
    @DeleteMapping("delete")
    @RequiresPermissions("system:log:delete")
    public Result delete(@RequestBody Id id){
        service.removeById(id.getId());
        return ok();
    }

    /**
     * 批量删除系统操作日志
     * @param ids
     * @return
     */
    @DeleteMapping("deleteBatch")
    @RequiresPermissions("system:log:delete")
    public Result deleteBatch(@RequestBody IdList ids){
        service.removeByIds(ids.getIds());
        return ok();
    }

    /**
     * 导出excel
     * @param response
     * @return
     */
    @GetMapping("exportExcel")
    @RequiresPermissions("system:log:export")
    public void exportExcel(HttpServletResponse response, SysOperateLog sysOperateLog){
        try {
            new ExportExcelWriteBuilder()
                .autoTrim(true)
                .fileAndSheetName("系统操作日志")
                .doWrite(response, service.findList(sysOperateLog), SysOperateLog.class);
        } catch (IOException e) {
            ResultUtil.writeJson(response, ResultStatusCode.EXCEL_EXPORT_ERROR);
        }
    }
}
