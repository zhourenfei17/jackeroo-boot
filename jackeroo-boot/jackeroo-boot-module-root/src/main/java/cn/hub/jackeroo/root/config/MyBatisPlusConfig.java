package cn.hub.jackeroo.root.config;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.root.config.bean.MybatisPaginationInnerInterceptor;
import cn.hub.jackeroo.utils.UserUtils;
import cn.hub.jackeroo.vo.LoginUser;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
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
     * 分页插件配置 -- 旧版本
     * @return
     */
    /*@Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor paginationInterceptor = new MyBatisPaginationInterceptor();
        // 开启count的join优化，只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));

        return paginationInterceptor;
    }*/

    /**
     * 分页插件配置
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 自定义实现的分页处理器
        interceptor.addInnerInterceptor(new MybatisPaginationInnerInterceptor());
        return interceptor;
    }

    /**
     * 新增数据默认字段填充策略
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        LoginUser user = UserUtils.getUser();
        if(user != null){
            this.strictInsertFill(metaObject, "createBy", String.class, user.getAccount());
            this.strictInsertFill(metaObject, "updateBy", String.class, user.getAccount());
        }
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "delFlag", Integer.class, Constant.DEL_FLAG_NORMAL);
    }

    /**
     * 更新数据默认字段填充策略
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        LoginUser user = UserUtils.getUser();
        if(user != null){
            this.strictUpdateFill(metaObject, "updateBy", String.class, UserUtils.getUser().getAccount());
        }
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
