package cn.hub.jackeroo.online.service;

import cn.hub.jackeroo.exception.JackerooException;
import cn.hub.jackeroo.online.entity.OnlineDatasource;
import cn.hub.jackeroo.online.mapper.OnlineDatasourceMapper;
import cn.hub.jackeroo.utils.StringUtils;
import cn.hub.jackeroo.utils.encrypt.AESUtils;
import cn.hub.jackeroo.vo.PageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 数据源配置 服务实现类
 * </p>
 *
 * @author alex
 * @since 2021-08-21
 */
@Service
public class OnlineDatasourceService extends ServiceImpl<OnlineDatasourceMapper, OnlineDatasource> {

    /**
     * 查询数据列表-带分页
     * @param onlineDatasource
     * @param pageParam
     * @return
     */
    public IPage<OnlineDatasource> findPage(OnlineDatasource onlineDatasource, PageParam pageParam){
        Page<OnlineDatasource> page = onlineDatasource.initPage(pageParam);
        page.setRecords(this.findList(onlineDatasource));

        return page;
    }

    /**
     * 查询数据列表
     * @param onlineDatasource
     * @return
     */
    public List<OnlineDatasource> findList(OnlineDatasource onlineDatasource){
        return getBaseMapper().findList(onlineDatasource);
    }

    /**
     * 更新数据源
     * @param onlineDatasource
     */
    public void updateDatasource(OnlineDatasource onlineDatasource){
        OnlineDatasource datasource = super.getById(onlineDatasource.getId());
        if(datasource == null){
            throw new JackerooException("记录不存在");
        }
        datasource.setType(onlineDatasource.getType());
        datasource.setIp(onlineDatasource.getIp());
        datasource.setPort(onlineDatasource.getPort());
        datasource.setUser(onlineDatasource.getUser());
        datasource.setDatabaseName(onlineDatasource.getDatabaseName());
        if(StringUtils.isNotBlank(onlineDatasource.getPassword())){
            datasource.setSlat(StringUtils.randomGen(16));
            datasource.setPassword(AESUtils.encrypt(onlineDatasource.getPassword(), datasource.getSlat()));
        }
        datasource.setUpdateTime(LocalDateTime.now());

        super.updateById(datasource);
    }
}