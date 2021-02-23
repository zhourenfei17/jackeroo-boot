package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.system.entity.SysDict;
import cn.hub.jackeroo.system.entity.SysModule;
import cn.hub.jackeroo.system.mapper.SysModuleMapper;
import cn.hub.jackeroo.vo.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    /**
     * 获取所有数据列表
     * @return
     */
    public List<SysModule> findAll(){
        LambdaUpdateWrapper<SysModule> query = new LambdaUpdateWrapper<>();
        query.orderByAsc(SysModule::getSort);

        return mapper.selectList(query);
    }

    /**
     * 获取当前排序号
     * @return
     */
    public int getMaxSort(){
        LambdaQueryWrapper<SysModule> query = new LambdaQueryWrapper<>();
        query.eq(SysModule::getDelFlag, Constant.DEL_FLAG_NORMAL);
        query.orderByDesc(SysModule::getSort);
        query.last("limit 1");

        SysModule module = super.getOne(query);
        if(module == null){
            return 10;
        }else{
            return module.getSort() - (module.getSort() % 10) + 10;
        }
    }
}
