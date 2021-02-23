package cn.hub.jackeroo.utils.easyexcel.handler.strategy;

import cn.hub.jackeroo.utils.easyexcel.annotation.ExcelField;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;

/**
 * 读取自定义注解@ExcelField中的width作为excel的对应列的宽度
 * @author alex
 * @date 2020/12/25
 */
public class LoadExcelFieldWidthStrategy extends AbstractColumnWidthStyleStrategy {

    private List<ExcelField> excelFieldList;

    private static final int MAX_COLUMN_WIDTH = 255;

    public LoadExcelFieldWidthStrategy(List<ExcelField> excelFieldList) {
        this.excelFieldList = excelFieldList;
    }

    @Override
    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<CellData> cellDataList, Cell cell, Head head,
                                  Integer relativeRowIndex, Boolean isHead) {
        if(isHead || relativeRowIndex > 0){
            return;
        }
        Integer columnWidth;
        ExcelField excelField = excelFieldList.get(cell.getColumnIndex());
        if(excelField == null){
            columnWidth = 60;
        }else{
            columnWidth = excelField.width();
        }
        if (columnWidth < 0) {
            return;
        }else if (columnWidth > MAX_COLUMN_WIDTH){
            columnWidth = MAX_COLUMN_WIDTH;
        }

        writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), columnWidth * 256);
        writeSheetHolder.getSheet().setDefaultRowHeightInPoints(22);
    }
}
