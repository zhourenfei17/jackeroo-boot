package cn.hub.jackeroo.online.service;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.exception.JackerooException;
import cn.hub.jackeroo.online.bo.GenTemplateBO;
import cn.hub.jackeroo.online.config.GenerateConfig;
import cn.hub.jackeroo.online.entity.OnlineScheme;
import cn.hub.jackeroo.online.entity.OnlineTable;
import cn.hub.jackeroo.online.entity.OnlineTableField;
import cn.hub.jackeroo.online.mapper.OnlineDataBaseMapper;
import cn.hub.jackeroo.online.param.GenerateTableDetail;
import cn.hub.jackeroo.online.utils.GenerateUtils;
import cn.hub.jackeroo.utils.DateUtils;
import cn.hub.jackeroo.utils.FileUtils;
import cn.hub.jackeroo.utils.StringUtils;
import cn.hub.jackeroo.vo.PageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author alex
 * @date 2020/09/18
 */
@Slf4j
@Service
public class OnlineGenerateService {

    @Resource
    private OnlineDataBaseMapper dataBaseMapper;
    @Autowired
    private OnlineTableService tableService;
    @Autowired
    private OnlineSchemeService schemeService;
    @Autowired
    private OnlineTableFieldService tableFieldService;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Autowired
    private GenerateConfig generateConfig;

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

            OnlineScheme scheme = new OnlineScheme();
            scheme.setPackageName("cn.hub.jackeroo");
            scheme.setShowCheckbox(Constant.BOOLEAN_YES);
            scheme.setFormStyle(2);
            scheme.setTemplate("standard");
            scheme.setEnablePagination(Constant.BOOLEAN_YES);
            scheme.setEnableSwagger(Constant.BOOLEAN_YES);
            scheme.setEnableServerValid(Constant.BOOLEAN_YES);
            map.put("scheme", scheme);
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
            if(field.getPrimaryKey() == Constant.BOOLEAN_YES){
                field.setEnable(false);
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
            field.setQueryType("=");
            field.setFormType("input");
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

    @Transactional
    public void save(GenerateTableDetail detail){
        tableService.save(detail.getOnlineTable());

        detail.getOnlineScheme().setTableId(detail.getOnlineTable().getId());
        schemeService.save(detail.getOnlineScheme());

        for (OnlineTableField field : detail.getOnlineTableField()) {
            field.setTableId(detail.getOnlineTable().getId());
        }
        tableFieldService.saveBatch(detail.getOnlineTableField());
    }

    /**
     * 通过业务表id生成代码
     * @param tableId
     */
    public void generateCode(String tableId){
        OnlineTable table = tableService.getById(tableId);
        if(table == null){
            throw new JackerooException("业务表信息不存在，生成代码失败");
        }
        OnlineScheme scheme = schemeService.getByTableId(tableId);
        List<OnlineTableField> fieldList = tableFieldService.findByTableId(tableId);

        generateCode(table, scheme, fieldList);
    }

    /**
     * 生成代码
     * @param table
     * @param scheme
     * @param fieldList
     */
    private void generateCode(OnlineTable table, OnlineScheme scheme, List<OnlineTableField> fieldList){
        List<GenTemplateBO> templateList = GenerateUtils.getTemplateList(generateConfig.getTemplateRootPath() + scheme.getTemplate() , freeMarkerConfigurer, scheme.getTemplate());

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("table", table);
        dataMap.put("scheme", scheme);
        dataMap.put("columnList", fieldList);
        dataMap.put("entityName", table.getClassName());
        dataMap.put("createDate", DateUtils.getDate("yyyy-MM-dd"));

        boolean hasQuery = false;
        for (OnlineTableField field : fieldList) {
            if(field.getEnableQuery() == Constant.BOOLEAN_YES){
                hasQuery = true;
            }
        }
        dataMap.put("hasQuery", hasQuery);

        String outRootPath = FileUtils.path(scheme.getOutputDir()
                + File.separator + generateConfig.getSourceRootPackage().replace(Constant.SPLIT_DOT, Constant.SPLIT_SLASH)
                + File.separator + scheme.getPackageName().replace(Constant.SPLIT_DOT, Constant.SPLIT_SLASH)
                + File.separator + scheme.getModuleName());
        FileUtils.createDirectory(outRootPath);

        log.debug("======================================= 生成代码中... =======================================");
        for (GenTemplateBO templateBO : templateList) {
            GenerateUtils.generateFile(dataMap, templateBO, outRootPath);
        }
        log.debug("======================================= 生成代码结束 =======================================");
    }
}
