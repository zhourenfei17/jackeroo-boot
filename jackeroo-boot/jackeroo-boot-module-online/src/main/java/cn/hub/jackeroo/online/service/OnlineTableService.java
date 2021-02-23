package cn.hub.jackeroo.online.service;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.online.entity.OnlineScheme;
import cn.hub.jackeroo.online.entity.OnlineTable;
import cn.hub.jackeroo.online.entity.OnlineTableField;
import cn.hub.jackeroo.online.mapper.OnlineTableMapper;
import cn.hub.jackeroo.vo.PageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
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
    @Autowired
    private OnlineSchemeService schemeService;
    @Autowired
    private OnlineTableFieldService tableFieldService;

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

        OnlineScheme scheme = schemeService.getByTableId(tableId);
        result.put("scheme", scheme);

        List<OnlineTableField> tableFieldList = tableFieldService.findByTableId(tableId);
        for (OnlineTableField field : tableFieldList) {
            if(field.getPrimaryKey() != null && field.getPrimaryKey() == Constant.BOOLEAN_YES){
                field.setDisabled(true);
            }
        }
        result.put("columns", tableFieldList);

        return result;
    }

    /**
     * 删除业务表配置信息
     * @param ids
     */
    @Transactional
    public void delete(Serializable ...ids){
        for (Serializable tableId : ids) {
            tableFieldService.deleteByTableId(tableId);

            schemeService.deleteByTableId(tableId);

            super.removeById(tableId);
        }
    }
}
