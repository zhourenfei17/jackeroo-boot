package cn.hub.jackeroo.online.mapper;

import cn.hub.jackeroo.online.entity.OnlineDatasource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* <p>
* 数据源配置 Mapper 接口
* </p>
*
* @author alex
* @since 2021-08-21
*/
public interface OnlineDatasourceMapper extends BaseMapper<OnlineDatasource> {

    List<OnlineDatasource> findList(OnlineDatasource onlineDatasource);
}
