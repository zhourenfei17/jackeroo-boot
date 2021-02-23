package cn.hub.jackeroo.utils.validator.annotation.validator;

import cn.hub.jackeroo.enums.ValidRuleType;
import cn.hub.jackeroo.utils.StringUtils;
import cn.hub.jackeroo.utils.ValidatorUtils;
import cn.hub.jackeroo.utils.validator.annotation.ValidRules;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义校验规则验证器
 * @author alex
 * @date 2020/11/25
 */
public class ValidRulesValidator implements ConstraintValidator<ValidRules, Serializable> {

    private ValidRuleType[] types;

    @Override
    public void initialize(ValidRules constraintAnnotation) {
        this.types = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(Serializable value, ConstraintValidatorContext context) {
        String val = null;
        if(value != null){
            val = value.toString();
        }
        if(StringUtils.isEmpty(val) || this.types.length == 0){
            return true;
        }
        List<Boolean> validStatus = new ArrayList<>();
        for (ValidRuleType type : types) {
            switch (type){
                case VALID_MOBILE:
                    validStatus.add(ValidatorUtils.isMobile(val));
                    break;
                case VALID_TELEPHONE:
                    validStatus.add(ValidatorUtils.isTelephone(val));
                    break;
                case VALID_POSTCDOE:
                    validStatus.add(ValidatorUtils.isPostCode(val));
                    break;
                case VALID_ID_CARD:
                    validStatus.add(ValidatorUtils.isIDCard(val));
                    break;
                case VALID_LETTER_AND_UNDERLINE:
                    validStatus.add(ValidatorUtils.isLetterAndUnderline(val));
                    break;
            }
        }
        return !validStatus.contains(false);
    }
}
