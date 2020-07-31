package cn.hub.jackeroo.root.filter;

import cn.hub.jackeroo.system.service.ValidService;
import cn.hub.jackeroo.utils.validator.annotation.ValidatedUnique;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 唯一校验过滤，只过滤包含@ValidatedUnique注解的方法
 * @author alex
 * @date 2020/07/06
 */
@Slf4j
@Aspect
@Component
public class UniqueValidFilter {
    @Autowired
    private ValidService validService;

    @Pointcut("@annotation(cn.hub.jackeroo.utils.validator.annotation.ValidatedUnique)")
    public void validatedUnique() {
    }

    @Before("validatedUnique()")
    public void doBefore(JoinPoint joinPoint) {
        MethodSignature sign = (MethodSignature)joinPoint.getSignature();
        Method method = sign.getMethod();
        ValidatedUnique annotation = method.getAnnotation(ValidatedUnique.class);
        if(annotation != null){
            org.springframework.web.context.request.RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            HttpServletRequest request = sra.getRequest();

            Map<String, String[]> parameterMap = request.getParameterMap();
            JSONObject entity = new JSONObject();
            for (String paramKey : parameterMap.keySet()) {
                entity.put(paramKey, parameterMap.get(paramKey)[0]);
            }
            validService.validEntityUniqueField(entity.toJavaObject(annotation.clazz()), annotation.groups());
        }
    }
}
