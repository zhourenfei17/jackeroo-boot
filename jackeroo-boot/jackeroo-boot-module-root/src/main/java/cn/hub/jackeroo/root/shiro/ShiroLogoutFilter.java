package cn.hub.jackeroo.root.shiro;

import cn.hub.jackeroo.service.RedisService;
import cn.hub.jackeroo.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.List;
import java.util.Locale;

/**
 * 自定义退出登录拦截器，额外实现同一用户并发登录人数信息redis缓存处理
 * @author alex
 * @date 2020/11/19
 */
@Slf4j
public class ShiroLogoutFilter extends LogoutFilter {

    private SessionControlFilter sessionControlFilter;

    /**
     * 复制父类的退出登录的拦截方式，但在原有的基础上更改了 1.取消了重定向，2.增加了对同一用户并发登录控制的redis缓存处理
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);

        String sessionId = (String)subject.getSession().getId();

        LoginUser user = (LoginUser)subject.getPrincipal();
        if(user == null){
            return true;
        }

        // Check if POST only logout is enabled
        if (isPostOnlyLogout()) {
            // check if the current request's method is a POST, if not redirect
            if (!WebUtils.toHttp(request).getMethod().toUpperCase(Locale.ENGLISH).equals(HttpMethod.POST.name())) {
                return onLogoutRequestNotAPost(request, response);
            }
        }

        //try/catch added for SHIRO-298:
        try {
            subject.logout();

            if(sessionControlFilter != null){
                // 如果没有限制同一用户并发登录数，则不要处理redis
                if(sessionControlFilter.getMaxSession() > 0){
                    RedisService redisService = sessionControlFilter.getRedisService();
                    String key = sessionControlFilter.getKeyPrefix() + user.getAccount();
                    if(redisService != null){
                        if(redisService.existKey(key)){
                            List<String> deque = redisService.getList(key, String.class);

                            if(deque.size() == 1 && deque.contains(sessionId)){
                                // 如果当前用户已登录的sessionId集合只剩当前登录用户，则直接删除该redis的key
                                redisService.deleteKey(key);
                            }else{
                                // 如果当前用户已登录的sessionId集合还有其他登录用户，则删除当前登录用户的sessionId，更新redis的key
                                deque.remove(sessionId);

                                redisService.setList(key, deque);
                            }
                        }
                    }else{
                        throw new RuntimeException("redisTemplate can not be null");
                    }
                }
            }
        } catch (SessionException ise) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
        }

        // 这里返回true主要是因为LoginController里也实现了logout的方法，用于返回给前端统一的json格式；如果这里返回false则不会执行controller方法，需要自己定义返回给前端的json数据
        return true;
    }

    public void setSessionControlFilter(SessionControlFilter sessionControlFilter) {
        this.sessionControlFilter = sessionControlFilter;
    }
}
