package cn.hub.jackeroo.system.mapper;

import cn.hub.jackeroo.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

/**
 * @author alex
 * @date 2020/06/29
 */
public interface ValidMapper {

    @SelectProvider(type = SqlProvider.class, method = "getUniqueSql")
    int uniqueFromTable(JSONObject json);

    class SqlProvider{
        public String getUniqueSql(final JSONObject json){
            return new SQL(){{
                SELECT("count(1)");
                FROM(json.getString("tableName"));
                WHERE("${columnName} = #{dataValue}");
                if(StringUtils.isNotBlank(json.getString("dataId"))){
                    WHERE("id != #{dataId}");
                }
                if(StringUtils.isNotBlank(json.getString("condition"))){
                    WHERE("${condition}");
                }
            }}.toString();
        }
    }
}
