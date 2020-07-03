package cn.hub.jackeroo.root.config;

import cn.hub.jackeroo.enums.ResultStatusCode;
import cn.hub.jackeroo.exception.JackerooException;
import cn.hub.jackeroo.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

/**
 * 全局异常处理中心
 * @author alex
 * @date 2020/06/06
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ HttpMessageNotReadableException.class, MissingServletRequestParameterException.class,
            ServletRequestBindingException.class, MethodArgumentNotValidException.class, ConstraintViolationException.class })
    public Result handleHttpMessageNotReadableException(Exception e) {
        log.error("参数解析失败", e);
        return new Result(ResultStatusCode.BAD_REQUEST.getCode(), e.getMessage());
    }

    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("不支持当前请求方法", e);
        return new Result(ResultStatusCode.METHOD_NOT_ALLOWED);
    }

    /**
     * shiro权限异常处理
     */
    @ExceptionHandler(UnauthorizedException.class)
    public Result unauthorizedException(UnauthorizedException e) {
        log.error(e.getMessage(), e);

        return new Result(ResultStatusCode.UNAUTHO_ERROR);
    }

    /**
     * 数据校验异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(ValidationException.class)
    public Result validationException(ValidationException e){
        return new Result(ResultStatusCode.BAD_REQUEST.getCode(), e.getMessage());
    }

    /**
     * 系统自定义异常处理
     */
    @ExceptionHandler(JackerooException.class)
    public Result jackerooExcetion(JackerooException e){
        log.error(e.getMessage());

        if(e.getStatusCode() != null){
            return new Result(e.getStatusCode());
        }else{
            return new Result(ResultStatusCode.BUSSINESS_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * 500 - System Error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("服务运行异常", e);
        return new Result(ResultStatusCode.SYSTEM_ERR);
    }
}
