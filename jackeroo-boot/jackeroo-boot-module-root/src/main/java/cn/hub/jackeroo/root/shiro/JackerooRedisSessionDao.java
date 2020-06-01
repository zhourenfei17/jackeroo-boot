package cn.hub.jackeroo.root.shiro;

import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.functions.Serialize;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.crazycake.shiro.RedisSessionDAO;
import org.crazycake.shiro.SessionInMemory;
import org.crazycake.shiro.exception.SerializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author alex
 * @date 2020/05/29
 */
// @Component
@Slf4j
public class JackerooRedisSessionDao extends AbstractSessionDAO {

    @Resource
    private RedisTemplate<Serializable, Session> redisTemplate;

    private static ThreadLocal sessionsInThread = new ThreadLocal();

    private long sessionInMemoryTimeout = 1000L;

    private String keyPrefix = "SYS:SESSION:USER";

    @Override
    protected Serializable doCreate(Session session) {
        if (session == null) {
            //logger.error("session is null");
            throw new UnknownSessionException("session is null");
        }
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
        return sessionId;
    }

    private void saveSession(Session session){
        redisTemplate.opsForValue().set(this.keyPrefix + session.getId(), session);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    @Override
    public void delete(Session session) {
        redisTemplate.delete(this.keyPrefix + session.getId());
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<Serializable> keys = redisTemplate.keys(this.keyPrefix + "*");
        List<Session> sessionList = redisTemplate.opsForValue().multiGet(keys);
        return sessionList;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            log.warn("session id is null");
            return null;
        }
        Session s = getSessionFromThreadLocal(sessionId);

        if (s != null) {
            return s;
        }

        log.debug("read session from redis");
        try {
            s = redisTemplate.opsForValue().get(this.keyPrefix + sessionId);
            setSessionToThreadLocal(sessionId, s);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("read session error. settionId=" + sessionId);
        }
        return s;
    }

    private void setSessionToThreadLocal(Serializable sessionId, Session s) {
        Map<Serializable, SessionInMemory> sessionMap = (Map<Serializable, SessionInMemory>) sessionsInThread.get();
        if (sessionMap == null) {
            sessionMap = new HashMap<Serializable, SessionInMemory>();
            sessionsInThread.set(sessionMap);
        }
        SessionInMemory sessionInMemory = new SessionInMemory();
        sessionInMemory.setCreateTime(new Date());
        sessionInMemory.setSession(s);
        sessionMap.put(sessionId, sessionInMemory);
    }

    private Session getSessionFromThreadLocal(Serializable sessionId) {
        Session s = null;

        if (sessionsInThread.get() == null) {
            return null;
        }

        Map<Serializable, SessionInMemory> sessionMap = (Map<Serializable, SessionInMemory>) sessionsInThread.get();
        SessionInMemory sessionInMemory = sessionMap.get(sessionId);
        if (sessionInMemory == null) {
            return null;
        }
        Date now = new Date();
        long duration = now.getTime() - sessionInMemory.getCreateTime().getTime();
        if (duration < sessionInMemoryTimeout) {
            s = sessionInMemory.getSession();
            log.debug("read session from memory");
        } else {
            sessionMap.remove(sessionId);
        }

        return s;
    }
}
