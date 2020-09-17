package cn.hub.jackeroo.online.mapper;

import cn.hub.jackeroo.online.entity.OnlineTable;
import cn.hub.jackeroo.online.entity.OnlineTableField;

import java.util.List;

/**
 * @author alex
 * @date 2020/09/17
 */
public interface OnlineDataBaseMapper {

    /**
     * 获取业务表信息
     * @param name
     * @param dbName
     * @return
     */
    OnlineTable findTableInfo(String name, String dbName);

    /**
     * 获取业务表字段信息
     * @param name
     * @param dbName
     * @return
     */
    List<OnlineTableField> findTableColumnList(String name, String dbName);
}
