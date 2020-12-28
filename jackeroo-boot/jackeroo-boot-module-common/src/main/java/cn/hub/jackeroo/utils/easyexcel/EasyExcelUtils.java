package cn.hub.jackeroo.utils.easyexcel;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.utils.Reflections;
import cn.hub.jackeroo.utils.ResultUtil;
import cn.hub.jackeroo.utils.easyexcel.annotation.ExcelField;
import cn.hub.jackeroo.utils.easyexcel.converter.LocalDateTimeConverter;
import cn.hub.jackeroo.utils.easyexcel.handler.LoadExcelFieldCellHandler;
import cn.hub.jackeroo.utils.easyexcel.handler.strategy.LoadExcelFieldStyleStrategy;
import cn.hub.jackeroo.utils.easyexcel.handler.strategy.LoadExcelFieldWidthStrategy;
import cn.hub.jackeroo.vo.Result;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import lombok.extern.slf4j.Slf4j;
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
 * @date 2020/12/24
 */
@Slf4j
public class EasyExcelUtils {

    public static void exportExcel(HttpServletResponse response, List<? extends Object> list, String fileName, String sheetName, Class clazz){
        try {
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
                if(excelField != null){
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

            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            List<List> newDataList = list.stream().map(item -> {
                List entity = new ArrayList();
                BeanMap beanMap = BeanMap.create(item);
                for (Map.Entry<Integer, String> entry : columnMap.entrySet()) {
                    entity.add(beanMap.get(entry.getValue()));
                }
                return entity;
            }).collect(Collectors.toList());

            EasyExcel.write(response.getOutputStream())
                    .head(headers)
                    .registerWriteHandler(new LoadExcelFieldWidthStrategy(excelFieldList))
                    .registerWriteHandler(new LoadExcelFieldStyleStrategy(excelFieldList))
                    .registerWriteHandler(new LoadExcelFieldCellHandler(excelFieldList))
                    // 注册全局LocalDateTime类型转换器
                    .registerConverter(new LocalDateTimeConverter())
                    // .registerWriteHandler(cellStyleStrategy)
                    .autoCloseStream(Boolean.FALSE)
                    .sheet(sheetName)
                    .doWrite(newDataList);
        } catch (IOException e) {
            log.error("导出excel失败！", e);

            ResultUtil.writeJson(response, new Result<>(Constant.SYSTEM_ERROR_CODE, "导出excel失败！"));
        }
    }
}
