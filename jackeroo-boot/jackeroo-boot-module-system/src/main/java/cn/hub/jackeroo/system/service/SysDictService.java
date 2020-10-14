package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.system.entity.SysDict;
import cn.hub.jackeroo.system.mapper.SysDictMapper;
import cn.hub.jackeroo.vo.PageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* <p>
* 数据字典 服务实现类
* </p>
*
* @author jackeroo
* @since 2020-10-10
*/
@Service
public class SysDictService extends ServiceImpl<SysDictMapper, SysDict> {
    @Resource
    private SysDictMapper mapper;

    /**
    * 查询数据列表-带分页
    * @param entity
    * @param pageParam
    * @return
    */
    public IPage<SysDict> findPage(SysDict entity, PageParam pageParam){
        Page<SysDict> page = entity.initPage(pageParam);
        page.setRecords(mapper.findList(entity));

        return page;
    }

    /**
     * 保存字典信息
     * @param entity
     */
    @Transactional
    public void saveDict(SysDict entity) {
        entity.setType(SysDict.TYPE_DICT);
        super.save(entity);
    }

    /**
     * 保存字典项信息
     * @param entity
     */
    @Transactional
    public void saveDictItem(SysDict entity){
        entity.setType(SysDict.TYPE_DICT_ITEM);
        super.save(entity);
    }
}