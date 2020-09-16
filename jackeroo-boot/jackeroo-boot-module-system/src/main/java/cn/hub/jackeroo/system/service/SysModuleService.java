package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.system.entity.SysModule;
import cn.hub.jackeroo.system.mapper.SysModuleMapper;
import cn.hub.jackeroo.vo.PageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 系统模块信息 服务实现类
 * </p>
 *
 * @author jackeroo
 * @since 2020-09-16
 */
@Service
public class SysModuleService extends ServiceImpl<SysModuleMapper, SysModule> {
    @Resource
    private SysModuleMapper mapper;

    /**
     * 查询数据列表-带分页
     * @param module
     * @param pageParam
     * @return
     */
    public IPage<SysModule> findPage(SysModule module, PageParam pageParam){
        module.setDelFlag(Constant.DEL_FLAG_NORMAL);

        Page<SysModule> page = module.initPage(pageParam);
        page.setRecords(mapper.findList(module));

        return page;
    }
}
