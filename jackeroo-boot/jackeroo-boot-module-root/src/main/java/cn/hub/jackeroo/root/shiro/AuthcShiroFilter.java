package cn.hub.jackeroo.root.shiro;

import cn.hub.jackeroo.enums.ResultStatusCode;
import cn.hub.jackeroo.vo.Result;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * 自定义的shiro拦截器，用于解决无权限访问仍然会存入redis的问题
 * @author alex
 * @date 2020/05/27
 */
public class AuthcShiroFilter extends FormAuthenticationFilter {
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                return executeLogin(request, response);
            } else {
                return true;
            }
        } else {
            // option请求处理
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;
            if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
                resp.setStatus(HttpStatus.OK.value());
                return true;
            }
            if(req.getRequestURI().indexOf("/auth/logout") >= 0){
                return true;
            }
            // 取消重定向，直接返回结果
            returnTokenInvalid((HttpServletRequest)request, (HttpServletResponse)response);
            return false;
        }
    }

    /**
     * 替代shiro重定向
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    private void returnTokenInvalid(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("UTF-8");
        Writer out = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream()));
        out.write(JSONObject.toJSONString(new Result(ResultStatusCode.INVALID_TOKEN)));
        out.flush();
        out.close();
    }
}
