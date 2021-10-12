package cn.hub.jackeroo.system.mapper;

import cn.hub.jackeroo.system.entity.SysOperateLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* <p>
* 系统操作日志 Mapper 接口
* </p>
*
* @author alex
* @since 2021-10-11
*/
public interface SysOperateLogMapper extends BaseMapper<SysOperateLog> {

    List<SysOperateLog> findList(SysOperateLog sysOperateLog);
}
