package cn.hub.jackeroo.online.service;

import cn.hub.jackeroo.api.ISystemApi;
import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.exception.JackerooException;
import cn.hub.jackeroo.online.bo.GenTemplateBO;
import cn.hub.jackeroo.online.config.Config;
import cn.hub.jackeroo.online.config.GenerateConfig;
import cn.hub.jackeroo.online.entity.OnlineDatasource;
import cn.hub.jackeroo.online.entity.OnlineDefaultConfig;
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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author alex
 * @date 2020/09/18
 */
@DS("master")
@Slf4j
@Service
public class OnlineGenerateService {

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
    @Autowired
    private Config config;
    @Autowired
    private ISystemApi systemApi;
    @Autowired
    private OnlineDefaultConfigService defaultConfigService;
    @Autowired
    private OnlineDatasourceService datasourceService;
    @Autowired
    private DynamicDataSourceService dynamicDataSourceService;

    /**
     * 获取数据库业务表列表-带分页
     * @param onlineTable
     * @param pageParam
     * @return
     */
    public IPage<OnlineTable> findTableListFromDataSource(OnlineTable onlineTable, PageParam pageParam, String dataSourceName){
        Page<OnlineTable> page = onlineTable.initPage(pageParam);
        page.setRecords(dynamicDataSourceService.findTableInfoByDataSource(onlineTable, dataSourceName));

        return page;
    }

    /**
     * 获取数据表相信信息
     * @param tableName
     * @return
     */
    public Map findTableDetailInfo(String tableName, String dataSource){
        final OnlineDefaultConfig defaultConfig = defaultConfigService.getConfig();

        String dataSourceName = datasourceService.getDataSourceName(dataSource);

        Map<String, Object> map = new HashMap<>();

        // 构建数据库列对象
        List<OnlineTableField> columnList = buildTableField(dynamicDataSourceService.findTableColumnListByDataSource(tableName, dataSourceName), defaultConfig);
        map.put("columns", columnList);

        OnlineTable query = new OnlineTable();
        query.setTableName(tableName);

        List<OnlineTable> tableList = dynamicDataSourceService.findTableInfoByDataSource(query, dataSourceName);
        if(CollectionUtils.isNotEmpty(tableList)){
            OnlineTable table = tableList.get(0);
            // 删除表前缀
            String[] prefix = StringUtils.isNotBlank(defaultConfig.getIgnorePrefix()) ? defaultConfig.getIgnorePrefix().split(",") : null;
            table.setClassName(StringUtils.toCapitalizeCamelCase(StringUtils.removePrefix(tableName, prefix)));
            table.setIdStrategy(defaultConfig.getIdStrategy());
            // 如果当前数据库列对象中存在配置项的默认逻辑删字段，则删除策略为逻辑删
            if(columnList.stream().filter(item -> item.getDbFieldName().equals(defaultConfig.getLogicColumn()) || item.getEntityFieldName().equals(defaultConfig.getLogicColumn())).count() > 0){
                table.setDelStrategy(OnlineTable.DEL_STRATEGY_LOGIC);
            }else{
                table.setDelStrategy(OnlineTable.DEL_STRATEGY_DATABASE);
            }
            // 默认排序字段
            OnlineTableField sortField = columnList.stream()
                    .filter(item -> item.getDbFieldName().equals(defaultConfig.getSortColumn()) || item.getEntityFieldName().equals(defaultConfig.getSortColumn()))
                    .findFirst()
                    .orElse(null);
            if(sortField != null){
                table.setSortColumn(sortField.getDbFieldName());
                table.setSortType(defaultConfig.getSortType());
            }

            map.put("table", table);

            OnlineScheme scheme = new OnlineScheme();
            scheme.setPackageName(defaultConfig.getPackageName());
            scheme.setShowCheckbox(defaultConfig.getShowCheckbox());
            scheme.setFormStyle(defaultConfig.getFormStyle());
            scheme.setTemplate("standard");
            scheme.setEnablePagination(defaultConfig.getEnablePagination());
            scheme.setEnableSwagger(defaultConfig.getEnableSwagger());
            scheme.setEnableServerValid(defaultConfig.getEnableServerValid());
            scheme.setEnableSecurity(defaultConfig.getEnableSecurity());
            map.put("scheme", scheme);
        }

        return map;
    }

    private List<OnlineTableField> buildTableField(List<OnlineTableField> list, OnlineDefaultConfig defaultConfig){
        List<OnlineTableField> configColumns = JSONArray.parseArray(defaultConfig.getColumnConfig(), OnlineTableField.class);
        if(configColumns == null){
            configColumns = new ArrayList<>();
        }
        int sort = 0;
        for (OnlineTableField field : list) {
            field.setDbFieldName(field.getDbFieldName().toLowerCase());
            field.setEntityFieldName(StringUtils.toCamelCase(field.getDbFieldName()));
            field.setEntityFieldType(getJavaTypeFromMysql(field.getDbFieldType()));
            // id列不编辑
            if(field.getPrimaryKey() == Constant.BOOLEAN_YES){
                field.setDisabled(true);
            }
            // 必填
            field.setFormRequired(field.getEnableNull());
            field.setSort(++sort);

            // 如果是公共列，则按照配置的来，否则按默认的
            OnlineTableField configColumn = configColumns.stream().filter(item -> field.getDbFieldName().equals(item.getDbFieldName()) || field.getEntityFieldName().equals(item.getDbFieldName())).findFirst().orElse(null);
            if(configColumn != null){
                field.setEnableList(configColumn.getEnableList());
                field.setEnableForm(configColumn.getEnableForm());
                field.setEnableQuery(configColumn.getEnableQuery());
                field.setEnableSort(configColumn.getEnableSort());
                field.setQueryType(configColumn.getQueryType());
                field.setFormType(configColumn.getFormType());
                field.setLocker(configColumn.getLocker());
                field.setFillStrategy(configColumn.getFillStrategy());
            }else{
                field.setEnableList(Constant.BOOLEAN_YES);
                field.setEnableForm(Constant.BOOLEAN_YES);

                if("text".equalsIgnoreCase(field.getDbFieldType())){
                    field.setFormType("textarea");
                }else if("tinyint".equalsIgnoreCase(field.getDbFieldType())){
                    field.setFormType("select");
                }else if("LocalDate".equals(field.getEntityFieldType())){
                    field.setFormType("date");
                }else if("LocalDateTime".equals(field.getEntityFieldType())){
                    field.setFormType("datetime");
                }else{
                    field.setFormType("input");
                }

                if(field.getFormType().equals("input") && field.getEntityFieldType().equals("String")){
                    field.setQueryType("Like");
                }else if(field.getFormType().equals("date") || field.getFormType().equals("datetime")){
                    field.setQueryType("Between");
                }else{
                    field.setQueryType("=");
                }
            }
        }
        return list;
    }

    private String getJavaTypeFromMysql(String mysqlType){
        String javaType = config.getMysqlToJavaMapping().get(mysqlType);
        if(javaType == null){
            return "String";
        }
        return javaType;
    }

    @Transactional
    public Long save(GenerateTableDetail detail){
        tableService.saveOrUpdate(detail.getOnlineTable());

        detail.getOnlineScheme().setTableId(detail.getOnlineTable().getId());
        schemeService.saveOrUpdate(detail.getOnlineScheme());

        for (OnlineTableField field : detail.getOnlineTableField()) {
            field.setTableId(detail.getOnlineTable().getId());
        }
        tableFieldService.saveOrUpdateBatch(detail.getOnlineTableField());

        return detail.getOnlineTable().getId();
    }

    /**
     * 通过业务表id生成代码
     * @param tableId
     */
    public void generateCode(String tableId, String outputDir, Integer override, List<String> templateType){
        OnlineTable table = tableService.getById(tableId);
        if(table == null){
            throw new JackerooException("业务表信息不存在，生成代码失败");
        }
        OnlineScheme scheme = schemeService.getByTableId(tableId);
        List<OnlineTableField> fieldList = tableFieldService.findByTableId(tableId);

        generateCode(table, scheme, fieldList, outputDir, override, templateType);
    }

    /**
     * 生成代码
     * @param table
     * @param scheme
     * @param fieldList
     * @param outputDir 输出目录
     */
    private void generateCode(OnlineTable table, OnlineScheme scheme, List<OnlineTableField> fieldList, String outputDir, Integer override, List<String> templateType){
        List<GenTemplateBO> templateList = GenerateUtils.getTemplateList(generateConfig.getTemplateRootPath() + scheme.getTemplate() , freeMarkerConfigurer, scheme.getTemplate());

        JSONObject module = systemApi.getModuleById(scheme.getModuleId());
        if(module == null){
            throw new JackerooException("所属模块信息不存在，生成代码失败！");
        }

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("table", table);
        dataMap.put("scheme", scheme);
        dataMap.put("columnList", fieldList);
        dataMap.put("searchList", fieldList.stream().filter(item -> item.getEnableQuery() == Constant.BOOLEAN_YES).collect(Collectors.toList()));
        dataMap.put("tableDictList", fieldList.stream().filter(item -> item.getEnableList() == Constant.BOOLEAN_YES && StringUtils.isNotBlank(item.getFormDictCode())).collect(Collectors.toSet()));
        /*dataMap.put("dictMap", fieldList.stream()
                .filter(item -> StringUtils.isNotBlank(item.getFormDictCode()))
                .collect(Collectors.toMap(item -> item.getDbFieldName().toUpperCase(), item -> systemApi.getDictItemByCode(item.getFormDictCode()))));*/
        dataMap.put("module", module);
        dataMap.put("entityName", table.getClassName());
        dataMap.put("varName", StringUtils.toUnderFirstLetter(table.getClassName()));
        dataMap.put("pathName", StringUtils.toUnderAndJoinSeparator(table.getClassName(), "/"));
        dataMap.put("componentName", StringUtils.toUnderAndJoinSeparator(table.getClassName(), Constant.SPLIT_MIDDLE_LINE));
        dataMap.put("createDate", DateUtils.getDate("yyyy-MM-dd"));

        analysisColumnField(fieldList, dataMap);

        if(table.getDelStrategy() == OnlineTable.DEL_STRATEGY_LOGIC){
            dataMap.put("existLogic", true);
        }else{
            dataMap.put("existLogic", false);
        }

        String outRootPath = FileUtils.path(outputDir
                + File.separator + generateConfig.getSourceRootPackage().replace(Constant.SPLIT_DOT, Constant.SPLIT_SLASH)
                + File.separator + scheme.getPackageName().replace(Constant.SPLIT_DOT, Constant.SPLIT_SLASH)
                + File.separator + module.getString("code"));
        FileUtils.createDirectory(outRootPath);

        log.debug("======================================= 生成代码中... =======================================");
        for (GenTemplateBO templateBO : templateList) {
            if(templateType.contains(templateBO.getTemplateType())){
                GenerateUtils.generateFile(dataMap, templateBO, outRootPath, override);
            }
        }
        log.debug("======================================= 生成代码结束 =======================================");
    }

    private void analysisColumnField(List<OnlineTableField> fieldList, Map<String, Object> dataMap){
        boolean existLength = false, existPrimaryKey = false, existUnique = false, existLocalDateTime = false, existLocalDate = false,
                existBigDecimal = false, existFieldFill = false, existRange = false, existNotEmpty = false, existLocker = false,
                existQuery = false, existValidRules = false, existEmail = false, existUrl = false, existDigits = false, existDict = false,
                existBetween = false;

        for (OnlineTableField field : fieldList) {
            if("String".equals(field.getEntityFieldType())){
                existLength = true;
                if(field.getFormRequired() == Constant.BOOLEAN_YES){
                    existNotEmpty = true;
                }
            }else if("LocalDateTime".equals(field.getEntityFieldType())){
                existLocalDateTime = true;
            }else if("LocalDate".equals(field.getEntityFieldType())){
                existLocalDate = true;
            }else if("BigDecimal".equals(field.getEntityFieldType())){
                existBigDecimal = true;
                existDigits = true;
            }else if("Double".equals(field.getEntityFieldType()) || "Float".equals(field.getEntityFieldType())){
                existDigits = true;
            }else if("Integer".equals(field.getEntityFieldType()) && field.getDbFieldLength() == 3){
                existRange = true;
            }
            if(field.getPrimaryKey() == Constant.BOOLEAN_YES){
                existPrimaryKey = true;
            }

            if(StringUtils.isNotBlank(field.getFormValidator())){
                if(!existUnique && field.getFormValidator().contains("validUnique")){
                    existUnique = true;
                }else if(!existEmail && field.getFormValidator().contains("validEmail")){
                    existEmail = true;
                }else if(!existUrl && field.getFormValidator().contains("validWebsite")){
                    existUrl = true;
                }
                if(!existValidRules && field.getFormValidator()
                        .replace("validUnique", "")
                        .replace("validEmail", "")
                        .replace("validWebsite", "")
                        .replace(",", "").length() > 0){
                    existValidRules = true;
                }
            }
            if(StringUtils.isNotBlank(field.getFormDictCode())){
                existDict = true;
            }

            if(StringUtils.isNotEmpty(field.getFillStrategy())){
                existFieldFill = true;
            }
            if(field.getLocker() == Constant.BOOLEAN_YES){
                existLocker = true;
            }
            if(field.getEnableQuery() == Constant.BOOLEAN_YES){
                existQuery = true;
            }

            if((field.getFormType().equals("date") || field.getFormType().equals("datetime"))
                    && field.getQueryType().equals("Between") && field.getEnableQuery() == Constant.BOOLEAN_YES){
                existBetween = true;
            }
        }

        dataMap.put("existLength", existLength);
        dataMap.put("existPrimaryKey", existPrimaryKey);
        dataMap.put("existUnique", existUnique);
        dataMap.put("existLocalDateTime", existLocalDateTime);
        dataMap.put("existLocalDate", existLocalDate);
        dataMap.put("existBigDecimal", existBigDecimal);
        dataMap.put("existFieldFill", existFieldFill);
        dataMap.put("existRange", existRange);
        dataMap.put("existNotEmpty", existNotEmpty);
        dataMap.put("existLocker", existLocker);
        dataMap.put("existQuery", existQuery);
        dataMap.put("existValidRules", existValidRules);
        dataMap.put("existEmail", existEmail);
        dataMap.put("existUrl", existUrl);
        dataMap.put("existDigits", existDigits);
        dataMap.put("existDict", existDict);
        dataMap.put("existBetween", existBetween);
    }

    /**
     * 获取文件列表
     * @param path
     * @return
     */
    public List<JSONObject> getFileList(String path){
        File[] files;
        if(path == null){
            files = File.listRoots();
        }else{
            File parentFile = new File(path);
            if(!parentFile.exists()){
                return null;
            }
            files = parentFile.listFiles();
        }

        List<JSONObject> list = new ArrayList<>();
        for (File file : files) {
            // 排除隐藏文件
            if(file.isHidden()){
                continue;
            }
            JSONObject json = new JSONObject();
            json.put("key", file.getAbsolutePath());
            json.put("title", Objects.equals("/", file.getAbsolutePath()) ? "/" : file.getName());
            if(file.isDirectory()){
                json.put("isLeaf", false);
            }else{
                json.put("isLeaf", true);
            }

            list.add(json);
        }

        return list;
    }
}
