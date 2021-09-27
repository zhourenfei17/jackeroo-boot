package cn.hub.jackeroo.online.service;

import cn.hub.jackeroo.exception.JackerooException;
import cn.hub.jackeroo.online.entity.OnlineDatasource;
import cn.hub.jackeroo.online.mapper.OnlineDatasourceMapper;
import cn.hub.jackeroo.utils.StringUtils;
import cn.hub.jackeroo.utils.encrypt.AESUtils;
import cn.hub.jackeroo.vo.PageParam;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.creator.DruidDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
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
@DS("master")
@Slf4j
@Service
public class OnlineDatasourceService extends ServiceImpl<OnlineDatasourceMapper, OnlineDatasource> {

    @Autowired
    private DynamicRoutingDataSource dataSource;
    @Autowired
    private DefaultDataSourceCreator dataSourceCreator;
    @Autowired
    private DruidDataSourceCreator druidDataSourceCreator;

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
        datasource.setName(onlineDatasource.getName());
        datasource.setUrl(onlineDatasource.getUrl());
        datasource.setDriverClassName(onlineDatasource.getDriverClassName());
        datasource.setUsername(onlineDatasource.getUsername());
        if(StringUtils.isNotBlank(onlineDatasource.getPassword())){
            datasource.setSlat(StringUtils.randomGen(16));
            datasource.setPassword(AESUtils.encrypt(onlineDatasource.getPassword(), datasource.getSlat()));
        }
        datasource.setUpdateTime(LocalDateTime.now());

        super.updateById(datasource);
    }

    /**
     * 动态添加数据源
     * @param datasource
     */
    public void createDataSource(OnlineDatasource datasource){
        if(datasource != null && !dataSource.getDataSources().containsKey(datasource.getName())){
            log.info("正在加载数据源【{}】", datasource.getName());
            DataSourceProperty dataSourceProperty = new DataSourceProperty();
            BeanUtils.copyProperties(datasource, dataSourceProperty);
            dataSourceProperty.setPassword(AESUtils.decrypt(datasource.getPassword(), datasource.getSlat()));

            DataSource ds = dataSourceCreator.createDataSource(dataSourceProperty);
            dataSource.addDataSource(datasource.getName(), ds);

            log.info("添加数据源【{}】成功", datasource.getName());
        }
    }

    /**
     * 获取选择的数据源
     * @param dataSource
     * @return
     */
    public String getDataSourceName(String dataSource){
        if("0".equals(dataSource)){
            return "master";
        }else{
            OnlineDatasource datasource = super.getById(dataSource);
            this.createDataSource(datasource);
            return datasource.getName();
        }
    }

    /**
     * 数据库连接测试
     * @param url
     * @param username
     * @param password
     * @param driverClassName
     * @return
     */
    public boolean testConnect(String url, String username, String password, String driverClassName) {
        Connection conn = null;
        try {
            if (!StringUtils.isEmpty(driverClassName)) {
                Class.forName(driverClassName);
                log.info("成功加载数据库驱动程序");
            }
            conn = DriverManager.getConnection(url, username, password);
            log.info("成功获取数据库连接");
            return true;
        } catch (Exception var8) {
            var8.printStackTrace();
        } finally {
            JdbcUtils.closeConnection(conn);
        }
        return false;
    }

    /**
     * 动态移除数据源
     * @param datasource
     */
    public void removeDataSource(OnlineDatasource datasource){
        dataSource.removeDataSource(datasource.getName());
    }
}