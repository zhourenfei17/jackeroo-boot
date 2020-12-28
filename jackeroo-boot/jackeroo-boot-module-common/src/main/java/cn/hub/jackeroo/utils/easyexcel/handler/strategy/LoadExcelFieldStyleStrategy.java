package cn.hub.jackeroo.utils.easyexcel.handler.strategy;

import cn.hub.jackeroo.utils.easyexcel.annotation.ExcelField;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.AbstractVerticalCellStyleStrategy;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.List;

/**
 * 读取自定义注解@ExcelField中的样式作为excel中的样式
 * @author alex
 * @date 2020/12/25
 */
public class LoadExcelFieldStyleStrategy extends AbstractVerticalCellStyleStrategy {

    private List<ExcelField> excelFieldList;

    public LoadExcelFieldStyleStrategy(List<ExcelField> excelFieldList) {
        this.excelFieldList = excelFieldList;
    }

    @Override
    protected WriteCellStyle headCellStyle(Head head) {
        ExcelField excelField = excelFieldList.get(head.getColumnIndex());
        if(excelField == null){
            return null;
        }
        WriteCellStyle headCellStyle = new WriteCellStyle();
        // 对齐方式：居中
        headCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 边框
        headCellStyle.setBorderBottom(BorderStyle.THIN);
        headCellStyle.setBorderLeft(BorderStyle.THIN);
        headCellStyle.setBorderRight(BorderStyle.THIN);
        headCellStyle.setBorderTop(BorderStyle.THIN);
        // 背景色填充策略
        headCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 前景色
        headCellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());

        return headCellStyle;
    }

    @Override
    protected WriteCellStyle contentCellStyle(Head head) {
        ExcelField excelField = excelFieldList.get(head.getColumnIndex());
        if(excelField == null){
            return null;
        }
        WriteCellStyle bodyCellStyle = new WriteCellStyle();
        // 对齐方式
        bodyCellStyle.setHorizontalAlignment(excelField.align());
        // 边框
        bodyCellStyle.setBorderBottom(BorderStyle.THIN);
        bodyCellStyle.setBorderLeft(BorderStyle.THIN);
        bodyCellStyle.setBorderRight(BorderStyle.THIN);
        bodyCellStyle.setBorderTop(BorderStyle.THIN);

        return bodyCellStyle;
    }
}
