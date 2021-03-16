package cn.hub.jackeroo.utils.validator.annotation.validator;

import cn.hub.jackeroo.api.ISystemApi;
import cn.hub.jackeroo.utils.SpringContextHolder;
import cn.hub.jackeroo.utils.StringUtils;
import cn.hub.jackeroo.utils.validator.annotation.Dict;
import com.alibaba.fastjson.JSONObject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;
import java.util.List;

/**
 * 数据字典字段验证器
 * @author alex
 * @date 2020/11/13
 */
public class DictValidator implements ConstraintValidator<Dict, Serializable> {

    private String dictCode;

    @Override
    public void initialize(Dict constraintAnnotation) {
        this.dictCode = constraintAnnotation.dictCode();
    }

    @Override
    public boolean isValid(Serializable value, ConstraintValidatorContext context) {
        String val = String.valueOf(value);
        if(StringUtils.isNotBlank(val) && !"null".equals(val)){
            ISystemApi api = (ISystemApi) SpringContextHolder.getApplicationContext().getBean("systemApi");

            List<JSONObject> dictList = api.getDictItemByCode(dictCode);

            if(dictList.stream().filter(item -> item.getString("value").equals(val)).count() > 0){
                return true;
            }else{
                return false;
            }
        }
        return true;
    }
}
