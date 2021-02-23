package cn.hub.jackeroo.system.mapper;

import cn.hub.jackeroo.system.entity.SysDict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* <p>
* 数据字典 Mapper 接口
* </p>
*
* @author jackeroo
* @since 2020-10-10
*/
public interface SysDictMapper extends BaseMapper<SysDict> {

    List<SysDict> findList(SysDict entity);
}
