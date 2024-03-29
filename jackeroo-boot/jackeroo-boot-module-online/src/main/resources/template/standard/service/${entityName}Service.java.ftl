package ${scheme.packageName}.${module.code}.service;

import ${scheme.packageName}.${module.code}.entity.${table.className};
import ${scheme.packageName}.${module.code}.mapper.${table.className}Mapper;
import ${scheme.packageName}.vo.PageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * ${table.comment} 服务实现类
 * </p>
 *
 * @author ${scheme.author}
 * @since ${createDate}
 */
@Service
@RequiredArgsConstructor
public class ${table.className}Service extends ServiceImpl<${table.className}Mapper, ${table.className}> {

    /**
     * 查询数据列表-带分页
     * @param ${varName}
     * @param pageParam
     * @return
     */
    public IPage<${table.className}> findPage(${table.className} ${varName}, PageParam pageParam){
        Page<${table.className}> page = ${varName}.initPage(pageParam);
        page.setRecords(this.findList(${varName}));

        return page;
    }

    /**
     * 查询数据列表
     * @param ${varName}
     * @return
     */
    public List<${table.className}> findList(${table.className} ${varName}){
        return getBaseMapper().findList(${varName});
    }
}