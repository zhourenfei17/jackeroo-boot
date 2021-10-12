package cn.hub.jackeroo.root.filter;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.system.entity.SysOperateLog;
import cn.hub.jackeroo.system.service.SysOperateLogService;
import cn.hub.jackeroo.utils.HttpUtils;
import cn.hub.jackeroo.utils.LocalDateUtils;
import cn.hub.jackeroo.utils.StringUtils;
import cn.hub.jackeroo.utils.UserUtils;
import cn.hub.jackeroo.utils.annotation.OperateLog;
import cn.hub.jackeroo.vo.LoginUser;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author alex
 * @date 2021/10/11
 */
@Slf4j
@Aspect
@Order(5)
@Component
public class OperateLogAspect {
    @Autowired
    private SysOperateLogService logService;

    @Autowired
    ObjectMapper objectMapper;

    private ThreadLocal<LocalDateTime> startTime = new ThreadLocal<>();

    @Pointcut("@annotation(cn.hub.jackeroo.utils.annotation.OperateLog)")
    public void pointcut() {

    }

    /**
     * 前置通知，在Controller层操作前拦截
     *
     * @param joinPoint 切入点
     */
    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) {
        // 获取当前调用时间
        startTime.set(LocalDateTime.now());
    }

    /**
     * 正常情况返回
     *
     * @param joinPoint 切入点
     * @param rvt       正常结果
     */
    @AfterReturning(pointcut = "pointcut()", returning = "rvt")
    public void doAfter(JoinPoint joinPoint, Object rvt) throws Exception {
        handleLog(joinPoint, null, rvt);
    }

    /**
     * 异常信息拦截
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void doAfter(JoinPoint joinPoint, Exception e) throws Exception {
        handleLog(joinPoint, e, null);
    }

    @Async
    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object rvt) throws Exception{
        // 获得注解
        Method method = getMethod(joinPoint);
        OperateLog operateLog = getAnnotationLog(method);
        if (operateLog == null) {
            return;
        }

        SysOperateLog erpLog = new SysOperateLog();
        // 请求信息
        org.springframework.web.context.request.RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        erpLog.setOperateContent(operateLog.value());
        erpLog.setOperateType(Integer.parseInt(operateLog.type()));
        erpLog.setRequestIp(ServletUtil.getClientIP(request));
        erpLog.setRequestMethod(request.getMethod());
        erpLog.setRequestUri(request.getRequestURI());
        erpLog.setRequestParams(getRequestParams(request));

        // 操作人
        LoginUser user = UserUtils.getUser();
        if(user != null){
            erpLog.setOperateUser(user.getAccount());
        }

        if(StringUtils.isEmpty(erpLog.getOperateUser())){
            // 如果是登录失败的或者退出登录，则操作人信息从请求参数中获取
            if(Constant.OPERATE_LOGIN.equals(operateLog.type()) || Constant.OPERATE_LOGOUT.equals(operateLog.type())){

                try {
                    erpLog.setOperateUser(JSONObject.parseObject(erpLog.getRequestParams()).getString("account"));
                }catch (Exception ex){

                }
            }
        }

        erpLog.setExecuteTime(LocalDateUtils.getChronoUnitBetween(startTime.get(), LocalDateTime.now(), ChronoUnit.MILLIS));
        if(e != null){
            erpLog.setResponseStatus(1);
        }else{
            JSONObject result = JSONObject.parseObject(JSONObject.toJSONString(rvt));
            if(result.getInteger("code") == 0){
                erpLog.setResponseStatus(SysOperateLog.RESPONSE_STATUS_SUCCESS);
            }else{
                erpLog.setResponseStatus(SysOperateLog.RESPONSE_STATUS_FAIL);
            }
        }

        logService.save(erpLog);
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private OperateLog getAnnotationLog(Method method) {
        if (method != null) {
            return method.getAnnotation(OperateLog.class);
        }
        return null;
    }

    private Method getMethod(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method;
        }
        return null;
    }

    /**
     * 获取请求参数
     * @param request
     * @return
     */
    private String getRequestParams(HttpServletRequest request) {
        if(request.getMethod().equalsIgnoreCase(HttpMethod.POST.name()) || request.getMethod().equalsIgnoreCase(HttpMethod.PUT.name())){
            return HttpUtils.getBodyString(request);
        }
        return JSONObject.toJSONString(ServletUtil.getParamMap(request));
    }
}
