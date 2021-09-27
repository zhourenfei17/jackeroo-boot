package cn.hub.jackeroo.utils;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;

/**
 * @author alex
 * @date 2021/09/23
 */
public class DbUtils {

    private static final String DB_TYPE_MYSQL = "mysql";

    public static String buildUrl(String type, String ip, Integer port, String database){
        switch (type){
            case DB_TYPE_MYSQL:
                return String.format("jdbc:mysql://%s:%d/%s?allowMultiQueries=true&characterEncoding=utf-8&autoReconnect=true", ip, port, database);
        }
        return null;
    }

    public static String getDriverClassName(String type){
        switch (type){
            case DB_TYPE_MYSQL:
                return "com.mysql.jdbc.Driver";
        }
        return null;
    }
}
