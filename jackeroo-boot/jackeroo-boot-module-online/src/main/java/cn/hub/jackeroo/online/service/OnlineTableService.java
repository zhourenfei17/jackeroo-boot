package cn.hub.jackeroo.online.service;

import cn.hub.jackeroo.online.entity.OnlineTable;
import cn.hub.jackeroo.online.mapper.OnlineTableMapper;
import cn.hub.jackeroo.vo.PageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author alex
 * @date 2020/09/17
 */
@Service
public class OnlineTableService extends ServiceImpl<OnlineTableMapper, OnlineTable> {
    @Resource
    private OnlineTableMapper mapper;

    public IPage<OnlineTable> findPage(OnlineTable onlineTable, PageParam pageParam){
        Page<OnlineTable> page = onlineTable.initPage(pageParam);
        page.setRecords(mapper.findList(onlineTable));

        return page;
    }
}
