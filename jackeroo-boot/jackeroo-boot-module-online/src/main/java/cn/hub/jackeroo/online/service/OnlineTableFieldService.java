package cn.hub.jackeroo.online.service;

import cn.hub.jackeroo.online.entity.OnlineTableField;
import cn.hub.jackeroo.online.mapper.OnlineTableFieldMapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author alex
 * @date 2020/09/17
 */
@Service
public class OnlineTableFieldService extends ServiceImpl<OnlineTableFieldMapper, OnlineTableField> {

    /**
     * 通过业务表id获取字段列表信息
     * @param tableId
     * @return
     */
    public List<OnlineTableField> findByTableId(Serializable tableId){
        LambdaUpdateWrapper<OnlineTableField> query = new LambdaUpdateWrapper<>();
        query.eq(OnlineTableField::getTableId, tableId);

        return super.list(query);
    }
}
