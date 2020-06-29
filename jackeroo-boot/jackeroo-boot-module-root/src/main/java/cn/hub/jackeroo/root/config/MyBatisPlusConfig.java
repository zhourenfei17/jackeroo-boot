package cn.hub.jackeroo.root.config;

import cn.hub.jackeroo.root.config.bean.MyBatisPaginationInterceptor;
import cn.hub.jackeroo.utils.UserUtils;
import cn.hub.jackeroo.vo.LoginUser;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;

/**
 * mybatis plus相关配置
 * @author alex
 * @date 2020/06/03
 */
@EnableTransactionManagement
@Configuration
@MapperScan("cn.hub.jackeroo.**.mapper*")
public class MyBatisPlusConfig implements MetaObjectHandler {

    /**
     * 分页插件配置
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor paginationInterceptor = new MyBatisPaginationInterceptor();
        // 开启count的join优化，只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));

        return paginationInterceptor;
    }

    /**
     * 新增数据默认字段填充策略
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        LoginUser user = UserUtils.getUser();
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "createBy", String.class, user.getAccount());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateBy", String.class, user.getAccount());
    }

    /**
     * 更新数据默认字段填充策略
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictUpdateFill(metaObject, "updateBy", String.class, UserUtils.getUser().getAccount());
    }
}
