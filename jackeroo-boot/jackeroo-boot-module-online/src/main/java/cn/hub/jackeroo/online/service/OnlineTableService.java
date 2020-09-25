package cn.hub.jackeroo.online.service;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.online.entity.OnlineScheme;
import cn.hub.jackeroo.online.entity.OnlineTable;
import cn.hub.jackeroo.online.entity.OnlineTableField;
import cn.hub.jackeroo.online.mapper.OnlineSchemeMapper;
import cn.hub.jackeroo.online.mapper.OnlineTableFieldMapper;
import cn.hub.jackeroo.online.mapper.OnlineTableMapper;
import cn.hub.jackeroo.vo.PageParam;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author alex
 * @date 2020/09/17
 */
@Service
public class OnlineTableService extends ServiceImpl<OnlineTableMapper, OnlineTable> {
    @Resource
    private OnlineTableMapper mapper;
    @Resource
    private OnlineSchemeMapper schemeMapper;
    @Resource
    private OnlineTableFieldMapper tableFieldMapper;

    public IPage<OnlineTable> findPage(OnlineTable onlineTable, PageParam pageParam){
        Page<OnlineTable> page = onlineTable.initPage(pageParam);
        page.setRecords(mapper.findList(onlineTable));

        return page;
    }

    public Map get(String tableId){
        OnlineTable table = super.getById(tableId);
        if(table == null){
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("table", table);

        LambdaUpdateWrapper<OnlineScheme> schemeQuery = new LambdaUpdateWrapper<>();
        schemeQuery.eq(OnlineScheme::getTableId, tableId);
        OnlineScheme scheme = schemeMapper.selectOne(schemeQuery);
        result.put("scheme", scheme);

        LambdaUpdateWrapper<OnlineTableField> fieldQuery = new LambdaUpdateWrapper<>();
        fieldQuery.eq(OnlineTableField::getTableId, tableId);
        List<OnlineTableField> tableFieldList = tableFieldMapper.selectList(fieldQuery);
        for (OnlineTableField field : tableFieldList) {
            if(field.getPrimaryKey() != null && field.getPrimaryKey() == Constant.BOOLEAN_YES){
                field.setEnable(false);
            }
        }
        result.put("columns", tableFieldList);

        return result;
    }
}
