package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.system.entity.SysDict;
import cn.hub.jackeroo.system.mapper.SysDictMapper;
import cn.hub.jackeroo.vo.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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
     * 通过字典code获取字典项列表
     * @param dictCode
     * @return
     */
    public List<SysDict> findDictItemByDictCode(String dictCode){
        LambdaQueryWrapper<SysDict> query = new LambdaQueryWrapper<>();
        query.orderByAsc(SysDict::getSort);
        query.eq(SysDict::getType, SysDict.TYPE_DICT_ITEM);
        query.eq(SysDict::getDictCode, dictCode);

        return super.list(query);
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

    /**
     * 获取当前排序号
     * @param dictCode
     * @return
     */
    public int getMaxSort(String dictCode){
        LambdaQueryWrapper<SysDict> query = new LambdaQueryWrapper<>();
        query.eq(SysDict::getDictCode, dictCode);
        query.eq(SysDict::getType, SysDict.TYPE_DICT_ITEM);
        query.orderByDesc(SysDict::getSort);
        query.last("limit 1");

        SysDict dict = super.getOne(query);
        if(dict == null){
            return 10;
        }else{
            return dict.getSort() - (dict.getSort() % 10) + 10;
        }
    }
}