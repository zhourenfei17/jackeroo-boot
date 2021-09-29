package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.constant.RedisKeyPrefix;
import cn.hub.jackeroo.exception.JackerooException;
import cn.hub.jackeroo.system.entity.SysDict;
import cn.hub.jackeroo.system.mapper.SysDictMapper;
import cn.hub.jackeroo.vo.PageParam;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* <p>
* 数据字典 服务实现类
* </p>
*
* @author jackeroo
* @since 2020-10-10
*/
@DS("master")
@Service
@RequiredArgsConstructor
public class SysDictService extends ServiceImpl<SysDictMapper, SysDict> {

    /**
    * 查询数据列表-带分页
    * @param entity
    * @param pageParam
    * @return
    */
    public IPage<SysDict> findPage(SysDict entity, PageParam pageParam){
        Page<SysDict> page = entity.initPage(pageParam);
        page.setRecords(getBaseMapper().findList(entity));

        return page;
    }

    /**
     * 通过字典code获取字典信息详情
     * @param dictCode
     * @return
     */
    public SysDict getByDictCode(String dictCode){
        LambdaUpdateWrapper<SysDict> query = new LambdaUpdateWrapper<>();
        query.eq(SysDict::getDictCode, dictCode);
        query.eq(SysDict::getType, SysDict.TYPE_DICT);

        return super.getOne(query, false);
    }

    /**
     * 通过字典code获取字典项列表
     * @param dictCode
     * @return
     */
    @Cacheable(value = RedisKeyPrefix.CACHE_DICT, key = "#dictCode")
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
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveDict(SysDict entity) {
        entity.setType(SysDict.TYPE_DICT);
        super.save(entity);
    }

    /**
     * 保存字典项信息
     * @param dict
     */
    @CacheEvict(value = RedisKeyPrefix.CACHE_DICT, key = "#dict.dictCode")
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveDictItem(SysDict dict){
        dict.setType(SysDict.TYPE_DICT_ITEM);
        super.save(dict);
    }

    /**
     * 修改数据字典信息
     * @param dict
     */
    @CacheEvict(value = RedisKeyPrefix.CACHE_DICT, key = "#dict.dictCode")
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateDict(SysDict dict){
        SysDict oldDict = super.getById(dict.getId());
        if(oldDict.getCategory() == SysDict.CATEGORY_SYSTEM){
            throw new JackerooException("系统字典不可修改");
        }
        // 如果字典code发生变化，同步修改字典项的code
        if(!dict.getDictCode().equals(oldDict.getDictCode())){
            LambdaUpdateWrapper<SysDict> update = new LambdaUpdateWrapper<>();
            update.eq(SysDict::getDictCode, oldDict.getDictCode());
            update.eq(SysDict::getType, SysDict.TYPE_DICT_ITEM);
            update.set(SysDict::getDictCode, dict.getDictCode());

            super.update(update);
        }

        super.updateById(dict);
    }

    /**
     * 修改字典项
     * @param dict
     */
    @CacheEvict(value = RedisKeyPrefix.CACHE_DICT, key = "#dict.dictCode")
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateDictItem(SysDict dict){
        SysDict oldDict = super.getById(dict.getId());
        if(oldDict.getCategory() == SysDict.CATEGORY_SYSTEM){
            throw new JackerooException("系统字典不可修改");
        }

        super.updateById(dict);
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

    /**
     * 删除字典信息或字典项
     * @param id
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void delete(String ...id){
        for (String s : id) {
            SysDict dict = super.getById(s);
            if(dict == null){
                return;
            }
            if(dict.getCategory() == SysDict.CATEGORY_SYSTEM){
                throw new JackerooException("系统字典无法删除");
            }
            LambdaUpdateWrapper<SysDict> query = new LambdaUpdateWrapper<>();
            if(dict.getType() == SysDict.TYPE_DICT){
                // 除了要删除字典信息，还要同时删除字典项
                query.eq(SysDict::getDictCode, dict.getDictCode());
                super.remove(query);
            }else{
                query.eq(SysDict::getId, s);
                query.eq(SysDict::getType, dict.getType());

                super.remove(query);
            }
        }
    }
}