package cn.hub.jackeroo.root.filter;

import cn.hub.jackeroo.root.filter.wrapper.HttpRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 对于post和put请求，只能读取一次body数据进行处理，返回servletRequest的包装类，以便后续仍然可以读取body数据
 * @author alex
 * @date 2020/09/02
 */
@Slf4j
@WebFilter(filterName = "aBodyFilter", urlPatterns = "/*")
public class BodyFilter implements Filter {

    @Value("${server.servlet.context-path}")
    private String contentPath;
    /**
     * 白名单
     */
    private static final String[] whiteList = {"/upload/"};

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        // 过滤掉白名单中的链接请求
        for (String str : whiteList) {
            if(request.getRequestURI().startsWith(contentPath + str)){
                chain.doFilter(req, res);
                return;
            }
        }

        if(request.getMethod().equalsIgnoreCase(HttpMethod.POST.name()) || request.getMethod().equalsIgnoreCase(HttpMethod.PUT.name())
            || request.getMethod().equalsIgnoreCase(HttpMethod.DELETE.name())){
            HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
            chain.doFilter(requestWrapper, response);
            return;
        }
        chain.doFilter(req, res);
    }
}
