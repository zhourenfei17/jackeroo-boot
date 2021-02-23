package ${scheme.packageName}.${module.code}.mapper;

import ${scheme.packageName}.${module.code}.entity.${table.className};
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

    List<${table.className}> findList(${table.className} ${varName});
}
