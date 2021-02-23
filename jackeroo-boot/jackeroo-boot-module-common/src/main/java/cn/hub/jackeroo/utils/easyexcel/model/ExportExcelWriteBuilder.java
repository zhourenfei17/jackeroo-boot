package cn.hub.jackeroo.utils.easyexcel.model;

import cn.hub.jackeroo.utils.LocalDateUtils;
import cn.hub.jackeroo.utils.StringUtils;
import cn.hub.jackeroo.utils.easyexcel.annotation.ExcelField;
import cn.hub.jackeroo.utils.easyexcel.annotation.enums.ExcelType;
import cn.hub.jackeroo.utils.easyexcel.converter.LocalDateTimeConverter;
import cn.hub.jackeroo.utils.easyexcel.handler.LoadExcelFieldCellHandler;
import cn.hub.jackeroo.utils.easyexcel.handler.strategy.LoadExcelFieldStyleStrategy;
import cn.hub.jackeroo.utils.easyexcel.handler.strategy.LoadExcelFieldWidthStrategy;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.row.SimpleRowHeightStyleStrategy;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import net.sf.cglib.beans.BeanMap;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author alex
 * @date 2020/12/29
 */
public class ExportExcelWriteBuilder {

    private String sheetName = "Sheet1";
    private String fileName;
    /**
     * 导出文件名时候自动添加导出时间后缀，默认导出带时间后缀名
     */
    private Boolean useTime = true;

    /**
     * 文件后缀名
     */
    private String fileSuffix = ExcelTypeEnum.XLSX.getValue();
    /**
     * 是否为自定义宽度
     */
    private Boolean autoWidth = false;
    /**
     * 行高
     */
    private Short rowHeight;

    private ExcelWriterBuilder builder;

    private ExcelWriterSheetBuilder sheetBuilder;

    private static final String FILE_ENCODING = "UTF-8";
    private static final String CONTENT_TYPE_EXCEL = "application/vnd.ms-excel";
    private static final short MIN_ROW_HEIGHT = 20;

    public ExportExcelWriteBuilder() {
        builder = EasyExcel.write()
                .autoCloseStream(Boolean.FALSE);
    }

    /**
     * excel文件类型，默认为：XLSX
     * @param excelType
     * @return
     */
    public ExportExcelWriteBuilder excelType(ExcelTypeEnum excelType){
        builder.excelType(excelType);
        this.fileSuffix = excelType.getValue();
        return this;
    }

    /**
     * 注册全局类型转换器，可覆盖默认的转换器
     * @param converter
     * @return
     */
    public ExportExcelWriteBuilder registerConverter(Converter converter){
        builder.registerConverter(converter);
        return this;
    }

    /**
     * 设置excel打开的密码
     * @param password
     * @return
     */
    public ExportExcelWriteBuilder password(String password){
        builder.password(password);
        return this;
    }

    /**
     * 设置cell内容是否去除两端多余空格
     * @param autoTrim
     * @return
     */
    public ExportExcelWriteBuilder autoTrim(Boolean autoTrim){
        builder.autoTrim(autoTrim);
        return this;
    }

    /**
     * 是否需要excel头
     * @param needHead
     * @return
     */
    public ExportExcelWriteBuilder needHead(Boolean needHead){
        builder.needHead(needHead);
        return this;
    }

    /**
     * 是否自适应宽度
     * @param autoWidth
     * @return
     */
    public ExportExcelWriteBuilder autoWidth(Boolean autoWidth){
        this.autoWidth = autoWidth;
        return this;
    }

    /**
     * 设置行高
     * @param rowHeight
     * @return
     */
    public ExportExcelWriteBuilder rowHeight(short rowHeight){
        this.rowHeight = rowHeight;
        return this;
    }

    /**
     * 设置sheet的名称
     * @param sheetName
     * @return
     */
    public ExportExcelWriteBuilder sheetName(String sheetName){
        this.sheetName = sheetName;
        return this;
    }

    /**
     * 设置excel文件的名称
     * @param fileName
     * @return
     */
    public ExportExcelWriteBuilder fileName(String fileName){
        this.fileName = fileName;
        return this;
    }

    /**
     * 同时设置excel文件和sheet的名称
     * @param name
     * @return
     */
    public ExportExcelWriteBuilder fileAndSheetName(String name){
        this.sheetName = name;
        this.fileName = name;
        return this;
    }

    public void doWrite(HttpServletResponse response, List<? extends Object> list, Class clazz) throws IOException {
        Assert.notNull(response, "response can not be null");
        Assert.notNull(clazz, "class can not be null");

        Field[] fields = clazz.getDeclaredFields();

        Assert.notEmpty(fields, "class must have field");
        // 记录所需要的列，根据ExcelField的order排序
        Map<Integer, String> columnMap = new TreeMap<>(Comparator.comparingInt(v -> v));
        // 记录所有的excelField
        List<ExcelField> excelFieldList = new ArrayList<>();
        for (Field field : fields) {
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            if(excelField != null && excelField.type() != ExcelType.ONLY_IMPORT){
                excelFieldList.add(excelField);
                columnMap.put(excelField.sort(), field.getName());
            }
        }
        Assert.notEmpty(excelFieldList, "class must have @interface @ExcelField");
        // 按照sort排序
        excelFieldList.sort(Comparator.comparingInt(ExcelField::sort));
        // 获取表头名称
        List<List<String>> headers = excelFieldList.stream().map(item -> {
            List<String> head = new ArrayList<>();
            head.add(item.title());
            return head;
        }).collect(Collectors.toList());

        response.setContentType(CONTENT_TYPE_EXCEL);
        response.setCharacterEncoding(FILE_ENCODING);

        if(StringUtils.isBlank(fileName)){
            fileName = "文档";
        }
        if(this.useTime){
            fileName = fileName + "_" + LocalDateUtils.getLocalDateTimeStr();
        }
        // 这里URLEncoder.encode可以防止中文乱码
        fileName = URLEncoder.encode(fileName, FILE_ENCODING).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + fileSuffix);

        List<List> newDataList = list.stream().map(item -> {
            List entity = new ArrayList();
            BeanMap beanMap = BeanMap.create(item);
            for (Map.Entry<Integer, String> entry : columnMap.entrySet()) {
                entity.add(beanMap.get(entry.getValue()));
            }
            return entity;
        }).collect(Collectors.toList());
        // 定义LocalDateTime类型转化器
        builder.registerConverter(new LocalDateTimeConverter());
        // 宽度调整策略
        if(autoWidth){
            builder.registerWriteHandler(new LongestMatchColumnWidthStyleStrategy());
        }else{
            builder.registerWriteHandler(new LoadExcelFieldWidthStrategy(excelFieldList));
        }
        // 设置行高
        if(this.rowHeight == null || this.rowHeight < MIN_ROW_HEIGHT){
            this.rowHeight = MIN_ROW_HEIGHT;
        }
        builder.registerWriteHandler(new SimpleRowHeightStyleStrategy(rowHeight, rowHeight));
        // 样式调整策略
        builder.registerWriteHandler(new LoadExcelFieldStyleStrategy(excelFieldList));
        // 自定义cell值填充策略
        builder.registerWriteHandler(new LoadExcelFieldCellHandler(excelFieldList));
        builder.head(headers);
        builder.file(response.getOutputStream());
        sheetBuilder = new ExcelWriterSheetBuilder(builder.build());
        sheetBuilder.sheetName(sheetName);

        sheetBuilder.doWrite(newDataList);
    }
}
