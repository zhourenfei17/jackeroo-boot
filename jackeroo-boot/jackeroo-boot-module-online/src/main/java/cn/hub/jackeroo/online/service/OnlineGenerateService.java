package cn.hub.jackeroo.online.service;

import cn.hub.jackeroo.online.entity.OnlineTable;
import cn.hub.jackeroo.online.mapper.OnlineDataBaseMapper;
import cn.hub.jackeroo.vo.PageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author alex
 * @date 2020/09/18
 */
@Service
public class OnlineGenerateService {

    @Resource
    private OnlineDataBaseMapper dataBaseMapper;

    /**
     * 获取数据库业务表列表-带分页
     * @param onlineTable
     * @param pageParam
     * @return
     */
    public IPage<OnlineTable> findTableListFromDataSoure(OnlineTable onlineTable, PageParam pageParam){
        Page<OnlineTable> page = onlineTable.initPage(pageParam);
        page.setRecords(dataBaseMapper.findTableInfo(onlineTable));

        return page;
    }
}
