package cn.hub.jackeroo.utils.validator.annotation.validator;

import cn.hub.jackeroo.constant.CodeType;
import cn.hub.jackeroo.utils.StringUtils;
import cn.hub.jackeroo.utils.ValidatorUtils;
import cn.hub.jackeroo.utils.validator.annotation.CodeNum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 号码验证器
 * @author alex
 * @date 2020/06/29
 */
public class CodeNumValidator implements ConstraintValidator<CodeNum, String> {

    private int type;

    @Override
    public void initialize(CodeNum constraintAnnotation) {
        this.type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(StringUtils.isEmpty(value)){
            return true;
        }
        if(type == CodeType.MOBILE){
            return ValidatorUtils.isMobile(value);
        }else if(type == CodeType.TELEPHONE){
            return ValidatorUtils.isTelephone(value);
        }else if(type == CodeType.POST_CODE){
            return ValidatorUtils.isPostCode(value);
        }else if(type == CodeType.ID_CARD){
            return ValidatorUtils.isIDCard(value);
        }
        return false;
    }
}
