package cn.hub.jackeroo.system.mapper;

import cn.hub.jackeroo.system.query.UniqueVo;
import cn.hub.jackeroo.utils.StringUtils;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

/**
 * @author alex
 * @date 2020/06/29
 */
public interface ValidMapper {

    @SelectProvider(type = SqlProvider.class, method = "getUniqueSql")
    int uniqueFromTable(UniqueVo uniqueVo);

    class SqlProvider{
        public String getUniqueSql(final UniqueVo uniqueVo){
            return new SQL(){{
                SELECT("count(1)");
                FROM(uniqueVo.getTableName());
                WHERE("${columnName} = #{dataValue}");
                if(StringUtils.isNotBlank(uniqueVo.getDataId())){
                    WHERE("id != #{dataId}");
                }
            }}.toString();
        }
    }
}
