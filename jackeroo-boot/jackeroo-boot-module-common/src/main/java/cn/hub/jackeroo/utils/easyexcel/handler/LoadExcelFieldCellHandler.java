package cn.hub.jackeroo.utils.easyexcel.handler;

import cn.hub.jackeroo.api.ISystemApi;
import cn.hub.jackeroo.utils.SpringContextHolder;
import cn.hub.jackeroo.utils.StringUtils;
import cn.hub.jackeroo.utils.easyexcel.annotation.ExcelField;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.excel.enums.CellDataTypeEnum.EMPTY;

/**
 * 读取自定义注解@ExcelField并对cell的值进行处理
 * @author alex
 * @date 2020/12/25
 */
@Slf4j
public class LoadExcelFieldCellHandler implements CellWriteHandler {


    private List<ExcelField> excelFieldList;

    /**
     * 字典项缓存，避免频繁去redis读
     */
    private Map<String, List<JSONObject>> CACHE_DICT_MAP = new HashMap<>();

    public LoadExcelFieldCellHandler(List<ExcelField> excelFieldList) {
        this.excelFieldList = excelFieldList;
    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

    }

    /**
     * 单元格数据转换后调用
     * @param writeSheetHolder
     * @param writeTableHolder
     * @param cellData
     * @param cell
     * @param head
     * @param relativeRowIndex
     * @param isHead
     */
    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
    }

    /**
     * 在单元上的所有操作完成后调用
     * @param writeSheetHolder
     * @param writeTableHolder
     * @param cellDataList
     * @param cell
     * @param head
     * @param relativeRowIndex
     * @param isHead
     */
    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        if(!isHead){
            ExcelField excelField = excelFieldList.get(cell.getColumnIndex());
            CellData cellData = cellDataList.get(0);
            // 处理字典项字段
            if(StringUtils.isNotBlank(excelField.dictType()) && cellData.getType() != EMPTY){
                String value = null;
                switch (cellData.getType()){
                    case STRING:
                        value = cellData.getStringValue();
                        break;
                    case NUMBER:
                        value = cellData.getNumberValue().toPlainString();
                        break;
                }
                if(StringUtils.isBlank(value)){
                    return;
                }
                cell.setCellValue(getDictItemText(value, getDictItemList(excelField.dictType())));
            }
        }
    }

    /**
     * 通过字典code获取字典项列表
     * @param dictCode
     * @return
     */
    private List<JSONObject> getDictItemList(String dictCode){
        if(this.CACHE_DICT_MAP.containsKey(dictCode)){
            return this.CACHE_DICT_MAP.get(dictCode);
        }
        ISystemApi api = (ISystemApi) SpringContextHolder.getApplicationContext().getBean("systemApi");

        List<JSONObject> dictList = api.getDictItemByCode(dictCode);
        this.CACHE_DICT_MAP.put(dictCode, dictList);

        return dictList;
    }

    /**
     * 通过字典项值获取字典项名
     * @param dictItemValue
     * @param dictItemList
     * @return
     */
    private String getDictItemText(String dictItemValue, List<JSONObject> dictItemList){
        if(CollectionUtils.isEmpty(dictItemList)){
            return null;
        }
        for (JSONObject json : dictItemList) {
            if(dictItemValue.equals(json.getString("value"))){
                return json.getString("label");
            }
        }
        return null;
    }
}
