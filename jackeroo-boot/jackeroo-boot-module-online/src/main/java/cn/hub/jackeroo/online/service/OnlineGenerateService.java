package cn.hub.jackeroo.online.service;

import cn.hub.jackeroo.api.ISystemApi;
import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.exception.JackerooException;
import cn.hub.jackeroo.online.bo.GenTemplateBO;
import cn.hub.jackeroo.online.config.Config;
import cn.hub.jackeroo.online.config.GenerateConfig;
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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private Config config;
    @Autowired
    private ISystemApi systemApi;
    @Autowired
    private OnlineDefaultConfigService defaultConfigService;

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
        List<OnlineTableField> list = dataBaseMapper.findTableColumnList(tableName, "mysql");
        for (OnlineTableField field : list) {
            if("text".equals(field.getDbFieldType())){
                field.setDbFieldLength(null);
            }
        }

        return list;
    }

    /**
     * 获取数据表相信信息
     * @param tableName
     * @return
     */
    public Map findTableDetailInfo(String tableName){
        final OnlineDefaultConfig defaultConfig = defaultConfigService.getConfig();

        Map<String, Object> map = new HashMap<>();

        // 构建数据库列对象
        List<OnlineTableField> columnList = buildTableField(findTableColumnList(tableName), defaultConfig);
        map.put("columns", columnList);

        OnlineTable query = new OnlineTable();
        query.setTableName(tableName);
        List<OnlineTable> tableList = dataBaseMapper.findTableInfo(query);
        if(CollectionUtils.isNotEmpty(tableList)){
            OnlineTable table = tableList.get(0);
            table.setClassName(StringUtils.toCapitalizeCamelCase(tableName));
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

    private boolean existPublicColumn(OnlineTableField field){
        return field.getDbFieldName().equalsIgnoreCase("create_by") ||
                field.getDbFieldName().equalsIgnoreCase("create_time") ||
                field.getDbFieldName().equalsIgnoreCase("update_by") ||
                field.getDbFieldName().equalsIgnoreCase("update_time") ||
                field.getDbFieldName().equalsIgnoreCase("del_flag");
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
        dataMap.put("module", module);
        dataMap.put("entityName", table.getClassName());
        dataMap.put("varName", StringUtils.toCamelCase(table.getClassName()));
        dataMap.put("componentName", StringUtils.toUnderAndJoinSeparator(table.getClassName(), Constant.SPLIT_MIDDLE_LINE));
        dataMap.put("createDate", DateUtils.getDate("yyyy-MM-dd"));

        boolean hasQuery = false;
        for (OnlineTableField field : fieldList) {
            if(field.getEnableQuery() == Constant.BOOLEAN_YES){
                hasQuery = true;
            }
        }
        dataMap.put("hasQuery", hasQuery);

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
