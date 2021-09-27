package cn.hub.jackeroo.online.service;

import cn.hub.jackeroo.online.entity.OnlineTable;
import cn.hub.jackeroo.online.entity.OnlineTableField;
import cn.hub.jackeroo.online.mapper.OnlineDataBaseMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author alex
 * @date 2021/09/26
 */
@Slf4j
@Service
public class DynamicDataSourceService {
    @Resource
    private OnlineDataBaseMapper dataBaseMapper;

    /**
     * 读取指定数据源下的表列表信息
     * @param onlineTable
     * @param dataSourceName
     * @return
     */
    List<OnlineTable> findTableInfoByDataSource(OnlineTable onlineTable, String dataSourceName){
        log.info("正在使用【{}】数据源", dataSourceName);
        //手动切换数据源
        DynamicDataSourceContextHolder.push(dataSourceName);
        return dataBaseMapper.findTableInfo(onlineTable);
    }

    /**
     * 读取指定数据源下业务表的所有字段列表
     * @param tableName
     * @return
     */
    public List<OnlineTableField> findTableColumnListByDataSource(String tableName, String dataSourceName){
        log.info("正在使用【{}】数据源", dataSourceName);
        //手动切换数据源
        DynamicDataSourceContextHolder.push(dataSourceName);
        List<OnlineTableField> list = dataBaseMapper.findTableColumnList(tableName, "mysql");
        for (OnlineTableField field : list) {
            if("text".equals(field.getDbFieldType())){
                field.setDbFieldLength(null);
            }
        }

        return list;
    }
}
