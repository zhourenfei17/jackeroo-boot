package ${scheme.packageName}.${scheme.moduleName}.service;

import ${scheme.packageName}.constant.Constant;
import ${scheme.packageName}.${scheme.moduleName}.entity.${table.className};
import ${scheme.packageName}.${scheme.moduleName}.mapper.${table.className}Mapper;
import ${scheme.packageName}.vo.PageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* <p>
* ${table.comment} 服务实现类
* </p>
*
* @author ${scheme.author}
* @since ${createDate}
*/
@Service
public class ${table.className}Service extends ServiceImpl<${table.className}Mapper, ${table.className}> {
    @Resource
    private ${table.className}Mapper mapper;

    /**
    * 查询数据列表-带分页
    * @param entity
    * @param pageParam
    * @return
    */
    public IPage<${table.className}> findPage(${table.className} entity, PageParam pageParam){
        Page<${table.className}> page = entity.initPage(pageParam);
        page.setRecords(mapper.findList(entity));

        return page;
    }
}