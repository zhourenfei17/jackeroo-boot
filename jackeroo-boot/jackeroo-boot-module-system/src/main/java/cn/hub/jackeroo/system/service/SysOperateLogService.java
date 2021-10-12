package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.system.entity.SysOperateLog;
import cn.hub.jackeroo.system.mapper.SysOperateLogMapper;
import cn.hub.jackeroo.vo.PageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统操作日志 服务实现类
 * </p>
 *
 * @author alex
 * @since 2021-10-11
 */
@Service
public class SysOperateLogService extends ServiceImpl<SysOperateLogMapper, SysOperateLog> {

    /**
     * 查询数据列表-带分页
     * @param sysOperateLog
     * @param pageParam
     * @return
     */
    public IPage<SysOperateLog> findPage(SysOperateLog sysOperateLog, PageParam pageParam){
        Page<SysOperateLog> page = sysOperateLog.initPage(pageParam);
        page.setRecords(this.findList(sysOperateLog));

        return page;
    }

    /**
     * 查询数据列表
     * @param sysOperateLog
     * @return
     */
    public List<SysOperateLog> findList(SysOperateLog sysOperateLog){
        return getBaseMapper().findList(sysOperateLog);
    }
}