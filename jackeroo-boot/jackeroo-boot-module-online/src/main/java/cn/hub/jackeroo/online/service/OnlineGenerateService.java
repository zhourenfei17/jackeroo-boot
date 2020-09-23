package cn.hub.jackeroo.online.service;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.online.entity.OnlineTable;
import cn.hub.jackeroo.online.entity.OnlineTableField;
import cn.hub.jackeroo.online.mapper.OnlineDataBaseMapper;
import cn.hub.jackeroo.utils.StringUtils;
import cn.hub.jackeroo.vo.PageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author alex
 * @date 2020/09/18
 */
@Service
public class OnlineGenerateService {

    @Resource
    private OnlineDataBaseMapper dataBaseMapper;

    /**
     * 获取数据库业务表列表-带分页
     * @param onlineTable
     * @param pageParam
     * @return
     */
    public IPage<OnlineTable> findTableListFromDataSoure(OnlineTable onlineTable, PageParam pageParam){
        Page<OnlineTable> page = onlineTable.initPage(pageParam);
        page.setRecords(dataBaseMapper.findTableInfo(onlineTable));

        return page;
    }

    /**
     * 获取数据库业务表的所有字段列表
     * @param tableName
     * @return
     */
    public List<OnlineTableField> findTableColumnList(String tableName){
        return dataBaseMapper.findTableColumnList(tableName, "mysql");
    }

    public Map findTableDetailInfo(String tableName){
        Map<String, Object> map = new HashMap<>();
        map.put("columns", buildTableField(findTableColumnList(tableName)));

        OnlineTable query = new OnlineTable();
        query.setTableName(tableName);
        List<OnlineTable> tableList = dataBaseMapper.findTableInfo(query);
        if(CollectionUtils.isNotEmpty(tableList)){
            OnlineTable table = tableList.get(0);
            table.setClassName(StringUtils.toCapitalizeCamelCase(tableName));
            table.setIdStrategy("ASSIGN_ID");
            table.setDelStrategy(0);

            map.put("table", table);
        }

        return map;
    }

    private List<OnlineTableField> buildTableField(List<OnlineTableField> list){
        int sort = 0;
        for (OnlineTableField field : list) {
            field.setDbFieldName(field.getDbFieldName().toLowerCase());
            field.setEntityFieldName(StringUtils.toCamelCase(field.getDbFieldName()));
            field.setEntityFieldType(Mysql.getJavaType(field.getDbFieldType()));
            // id列不编辑
            if(field.getDbFieldName().equalsIgnoreCase("id")){
                field.setEnable(false);
            }else{
                field.setEnable(true);
            }
            // 列表和表单字段
            if(existPublicColumn(field)){
                field.setEnableList(Constant.BOOLEAN_NO);
                field.setEnableForm(Constant.BOOLEAN_NO);
            }else{
                field.setEnableList(Constant.BOOLEAN_YES);
                field.setEnableForm(Constant.BOOLEAN_YES);
            }
            // 必填
            field.setFormRequired(field.getEnableNull());
            field.setSort(++sort);
        }
        return list;
    }

    private boolean existPublicColumn(OnlineTableField field){
        return field.getDbFieldName().equalsIgnoreCase("create_by") ||
                field.getDbFieldName().equalsIgnoreCase("create_time") ||
                field.getDbFieldName().equalsIgnoreCase("update_by") ||
                field.getDbFieldName().equalsIgnoreCase("update_time") ||
                field.getDbFieldName().equalsIgnoreCase("del_flag");
    }

    private enum Mysql{
        VARCHAR("String"),
        CHAR("Char"),
        BLOB("byte[]"),
        TEXT("String"),
        INTEGER("Long"),
        TINYINT("Integer"),
        SMALLINT("Integer"),
        MEDIUMINT("Integer"),
        BIGINT("Long"),
        FLOAT("Float"),
        DOUBLE("Double"),
        DECIMAL("BigDecimal"),
        DATE("LocalDate"),
        DATETIME("LocalDateTime");

        private String javaType;

        Mysql(String javaType) {
            this.javaType = javaType;
        }

        public static String getJavaType(String mysqlType){
            for (Mysql mysql : Mysql.values()) {
                if(mysql.name().equalsIgnoreCase(mysqlType)){
                    return mysql.javaType;
                }
            }
            return "String";
        }
    }
}
