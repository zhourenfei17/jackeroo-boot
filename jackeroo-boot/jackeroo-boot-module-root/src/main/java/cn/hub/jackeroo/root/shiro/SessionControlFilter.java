package cn.hub.jackeroo.root.shiro;

import cn.hub.jackeroo.enums.ResultStatusCode;
import cn.hub.jackeroo.service.RedisService;
import cn.hub.jackeroo.utils.ResultUtil;
import cn.hub.jackeroo.vo.LoginUser;
import cn.hub.jackeroo.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 自定义session拦截器，实现同一用户并发登录人数控制
 */
@Slf4j
public class SessionControlFilter extends AccessControlFilter {

	// private String kickOutUrl; // 踢出后到的地址

	private boolean kickOutAfter = false; // 踢出之前登录的/之后登录的用户 默认踢出之前登录的用户

	private int maxSession = 1; // 同一个帐号最大会话数 默认1

	private SessionManager sessionManager;

	private RedisService redisService;

	private String keyPrefix = "cache:sessionId";

    /**
     * 是否允许访问，返回true表示允许
     * @param request
     * @param response
     * @param mappedValue
     * @return
     * @throws Exception
     */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		Subject subject = getSubject(request, response);
		if (!subject.isAuthenticated() && !subject.isRemembered()) {
			// 如果没有登录，直接进行之后的流程
			return true;
		}

		Session session = subject.getSession();
        LoginUser user = (LoginUser) subject.getPrincipal();
		String sessionId = (String)session.getId();
		String redisKey = keyPrefix + user.getAccount();

		// 读取缓存 没有就存入
		List<String> deque;

		// 如果此用户没有session队列，也就是还没有登录过，缓存中没有
		// 就new一个空队列，不然deque对象为空，会报空指针
		if (!redisService.existKey(redisKey)) {
			deque = new LinkedList<>();
		}else{
            deque = redisService.getList(redisKey, String.class);
        }

		// 如果队列里没有此sessionId，且用户没有被踢出；放入队列
		if (!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
			// 将sessionId存入队列
			deque.add(sessionId);
			// 将用户的sessionId队列缓存
            // redisService.setList(redisKey, deque);
		}

		// 如果队列里的sessionId数超出最大会话数，开始踢人
		while (deque.size() > maxSession) {
			Serializable kickOutSessionId;
			if (kickOutAfter) { // 如果踢出后者
				// kickoutSessionId = deque.removeFirst();
                kickOutSessionId = deque.remove(deque.size() - 1);
				// 踢出后再更新下缓存队列
				// cache.put(username, deque);
			} else { // 否则踢出前者
				// kickoutSessionId = deque.removeLast();
                kickOutSessionId = deque.remove(0);
				// 踢出后再更新下缓存队列
				// cache.put(username, deque);
			}
			// redisService.setList(redisKey, deque);

			try {
				// 获取被踢出的sessionId的session对象
				Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickOutSessionId));
				if (kickoutSession != null) {
					// 设置会话的kickout属性表示踢出了
					kickoutSession.setAttribute("kickout", true);
				}
			}
			catch (Exception e) {// ignore exception

			}
		}
        // 每次请求同时会更新redis，避免session未过期，该key的值过期读取不到的情况
        redisService.setList(redisKey, deque);

		// 如果被踢出了，直接退出，重定向到踢出后的地址
		if (session.getAttribute("kickout") != null) {
			// 会话被踢出了
			try {
				// 退出登录
				subject.logout();
			}
			catch (Exception e) { // ignore
			}
			saveRequest(request);

            ResultUtil.writeJson(response, new Result<>(ResultStatusCode.KICK_OUT_ERROR));
			return false;
		}
		return true;
	}

	public void setKickOutAfter(boolean kickOutAfter) {
		this.kickOutAfter = kickOutAfter;
	}


	public void setMaxSession(int maxSession) {
		this.maxSession = maxSession;
	}

    public int getMaxSession() {
        return maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public RedisService getRedisService() {
        return redisService;
    }

    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }
}
