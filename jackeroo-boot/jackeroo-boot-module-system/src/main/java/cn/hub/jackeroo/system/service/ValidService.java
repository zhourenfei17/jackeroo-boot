package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.system.mapper.ValidMapper;
import cn.hub.jackeroo.system.query.UniqueVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author alex
 * @date 2020/06/29
 */
@Service
public class ValidService {

    @Resource
    private ValidMapper mapper;

    public Boolean unique(UniqueVo uniqueVo){
        return mapper.uniqueFromTable(uniqueVo) > 0;
    }
}
