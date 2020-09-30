package ${scheme.packageName}.${scheme.moduleName}.mapper;

import ${scheme.packageName}.${scheme.moduleName}.entity.${table.className};
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* <p>
* ${table.comment} Mapper 接口
* </p>
*
* @author ${scheme.author}
* @since ${createDate}
*/
public interface ${table.className}Mapper extends BaseMapper<${table.className}> {

    List<${table.className}> findList(${table.className} entity);
}
