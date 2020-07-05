package cn.hub.jackeroo.system.service;

import cn.hub.jackeroo.system.entity.SysRole;
import cn.hub.jackeroo.system.mapper.ValidMapper;
import cn.hub.jackeroo.system.query.UniqueVo;
import cn.hub.jackeroo.utils.ArrayUtils;
import cn.hub.jackeroo.utils.Reflections;
import cn.hub.jackeroo.utils.StringUtils;
import cn.hub.jackeroo.utils.validator.annotation.Unique;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.ValidationException;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author alex
 * @date 2020/06/29
 */
@Service
public class ValidService {

    @Resource
    private ValidMapper mapper;

    /**
     * 唯一性判断，唯一返回true，不唯一返回false
     * @param uniqueVo
     * @return
     */
    public Boolean isUnique(UniqueVo uniqueVo){
        return mapper.uniqueFromTable(uniqueVo) == 0;
    }

    public void validEntityUniqueField(Object obj){
        this.validEntityUniqueField(obj, null);
    }
    /**
     * 验证实体类带有@Unique注解的字段
     * @param obj
     */
    public void validEntityUniqueField(Object obj, Class<?> validGroupClass){
        String tableNameStr;
        TableName tableName = obj.getClass().getAnnotation(TableName.class);
        if(tableName != null){
            tableNameStr = tableName.value();
        }else{
            tableNameStr = StringUtils.toUnderScoreCase(obj.getClass().getSimpleName());
        }
        Object dataId = Reflections.getFieldValue(obj, "id");
        Field[] fields = obj.getClass().getDeclaredFields();
        if(fields != null){
            for (Field field : fields) {
                Unique unique = field.getAnnotation(Unique.class);
                if(unique == null){
                    continue;
                }
                if(validGroupClass != null){
                    if(!ArrayUtils.contains(unique.groups(), validGroupClass)){
                        continue;
                    }
                }
                // 值为空则不进行唯一性判断
                String dataValue = Reflections.getFieldValue(obj, field.getName()).toString();
                if(StringUtils.isEmpty(dataValue)){
                    continue;
                }
                UniqueVo uniqueVo = new UniqueVo();
                uniqueVo.setTableName(tableNameStr);
                uniqueVo.setColumnName(StringUtils.toUnderScoreCase(field.getName()));
                uniqueVo.setDataValue(dataValue);
                if(dataId != null){
                    uniqueVo.setDataId(dataId.toString());
                }
                if(!this.isUnique(uniqueVo)){
                    throw new ValidationException(unique.name() + unique.message());
                }
            }
        }
    }
}
