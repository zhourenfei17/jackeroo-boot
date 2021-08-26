package cn.hub.jackeroo.root.config.bean;

/**
 * mybatis-puls 3.4.0版本以前（不含）使用此版本
 * 主要是重写了PaginationInterceptor中的intercept方法，增加了Page对象从实体类中获取
 * @author alex
 * @date 2020/06/22
 */
// @Setter
// @Accessors(chain = true)
// @Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class MyBatisPaginationInterceptor /*extends PaginationInterceptor*/ {
    /**
     * 数据库类型
     *
     * @since 3.3.1
     *//*
    private DbType dbType;
    *//**
     * 方言实现类
     *
     * @since 3.3.1
     *//*
    private IDialect dialect;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);

        // SQL 解析
        this.sqlParser(metaObject);

        // 先判断是不是SELECT操作  (2019-04-10 00:37:31 跳过存储过程)
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        if (SqlCommandType.SELECT != mappedStatement.getSqlCommandType()
                || StatementType.CALLABLE == mappedStatement.getStatementType()) {
            return invocation.proceed();
        }

        // 针对定义了rowBounds，做为mapper接口方法的参数
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        Object paramObj = boundSql.getParameterObject();

        // 判断参数里是否有page对象
        IPage<?> page = null;
        if (paramObj instanceof IPage) {
            page = (IPage<?>) paramObj;
        } else if (paramObj instanceof Map) {
            for (Object arg : ((Map<?, ?>) paramObj).values()) {
                if (arg instanceof IPage) {
                    page = (IPage<?>) arg;
                    break;
                }
            }
        }else if(Reflections.isExistField("page", paramObj)){
            page = (IPage<?>)Reflections.invokeGetter(paramObj, "page");
        }

        *//*
     * 不需要分页的场合，如果 size 小于 0 返回结果集
     *//*
        if (null == page || page.getSize() < 0) {
            return invocation.proceed();
        }

        if (this.limit > 0 && this.limit <= page.getSize()) {
            //处理单页条数限制
            handlerLimit(page);
        }

        String originalSql = boundSql.getSql();
        Connection connection = (Connection) invocation.getArgs()[0];

        if (page.isSearchCount() && !page.isHitCount()) {
            SqlInfo sqlInfo = SqlParserUtils.getOptimizeCountSql(page.optimizeCountSql(), countSqlParser, originalSql);
            this.queryTotal(sqlInfo.getSql(), mappedStatement, boundSql, page, connection);
            if (page.getTotal() <= 0) {
                return null;
            }
        }
        DbType dbType = Optional.ofNullable(this.dbType).orElse(JdbcUtils.getDbType(connection.getMetaData().getURL()));
        IDialect dialect = Optional.ofNullable(this.dialect).orElse(DialectFactory.getDialect(dbType));
        String buildSql = concatOrderBy(originalSql, page);
        DialectModel model = dialect.buildPaginationSql(buildSql, page.offset(), page.getSize());
        Configuration configuration = mappedStatement.getConfiguration();
        List<ParameterMapping> mappings = new ArrayList<>(boundSql.getParameterMappings());
        Map<String, Object> additionalParameters = (Map<String, Object>) metaObject.getValue("delegate.boundSql.additionalParameters");
        model.consumers(mappings, configuration, additionalParameters);
        metaObject.setValue("delegate.boundSql.sql", model.getDialectSql());
        metaObject.setValue("delegate.boundSql.parameterMappings", mappings);
        return invocation.proceed();
    }*/
}
