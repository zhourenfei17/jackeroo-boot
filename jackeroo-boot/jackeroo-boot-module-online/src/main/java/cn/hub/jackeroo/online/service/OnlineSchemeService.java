package cn.hub.jackeroo.online.service;

import cn.hub.jackeroo.online.entity.OnlineScheme;
import cn.hub.jackeroo.online.mapper.OnlineSchemeMapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @author alex
 * @date 2020/09/17
 */
@Service
public class OnlineSchemeService extends ServiceImpl<OnlineSchemeMapper, OnlineScheme> {
    /**
     * 通过业务表id获取代码生成相关信息
     * @param tableId
     * @return
     */
    public OnlineScheme getByTableId(Serializable tableId){
        return super.getOne(buildTableIdQuery(tableId));
    }

    private LambdaUpdateWrapper<OnlineScheme> buildTableIdQuery(Serializable tableId){
        LambdaUpdateWrapper<OnlineScheme> query = new LambdaUpdateWrapper<>();
        query.eq(OnlineScheme::getTableId, tableId);

        return query;
    }

    /**
     * 通过tableId删除
     * @param tableId
     */
    @Transactional
    public void deleteByTableId(Serializable tableId){
        super.remove(buildTableIdQuery(tableId));
    }
}
