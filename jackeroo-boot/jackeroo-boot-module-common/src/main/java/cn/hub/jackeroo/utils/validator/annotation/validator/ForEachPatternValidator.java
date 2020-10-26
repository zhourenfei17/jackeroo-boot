package cn.hub.jackeroo.utils.validator.annotation.validator;

import cn.hub.jackeroo.utils.validator.annotation.ForEachPattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author alex
 * @date 2020/10/23
 */
public class ForEachPatternValidator implements ConstraintValidator<ForEachPattern, Iterable> {

    private String regexp;

    @Override
    public void initialize(ForEachPattern constraintAnnotation) {
        this.regexp = constraintAnnotation.regexp();
    }

    @Override
    public boolean isValid(Iterable value, ConstraintValidatorContext context) {
        if(value == null){
            return true;
        }
        for (Object val : value) {
            if(!Pattern.matches(regexp, val.toString())){
                return false;
            }
        }
        return true;
    }
}
