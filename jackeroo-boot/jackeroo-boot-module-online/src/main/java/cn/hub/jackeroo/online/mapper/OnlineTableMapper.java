package cn.hub.jackeroo.online.mapper;

import cn.hub.jackeroo.online.entity.OnlineTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 代码生成表信息 Mapper 接口
 * </p>
 *
 * @author jackeroo
 * @since 2020-09-17
 */
public interface OnlineTableMapper extends BaseMapper<OnlineTable> {

    List<OnlineTable> findList(OnlineTable onlineTable);
}
