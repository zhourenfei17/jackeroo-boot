package cn.hub.jackeroo.system.mapper;

import cn.hub.jackeroo.system.entity.SysModule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 系统模块信息 Mapper 接口
 * </p>
 *
 * @author jackeroo
 * @since 2020-09-16
 */
public interface SysModuleMapper extends BaseMapper<SysModule> {

    List<SysModule> findList(SysModule module);
}
