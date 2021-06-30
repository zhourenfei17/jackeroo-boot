package cn.hub.jackeroo.root.config;

import cn.hub.jackeroo.constant.RedisKeyPrefix;
import cn.hub.jackeroo.root.shiro.AuthcShiroFilter;
import cn.hub.jackeroo.root.shiro.CredentialsMatcher;
import cn.hub.jackeroo.root.shiro.SessionControlFilter;
import cn.hub.jackeroo.root.shiro.SessionManager;
import cn.hub.jackeroo.root.shiro.ShiroLogoutFilter;
import cn.hub.jackeroo.root.shiro.ShiroRealm;
import cn.hub.jackeroo.service.RedisService;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

	@Value("${spring.redis.host}")
	private String redisHost;
	@Value("${spring.redis.port}")
	private int redisPort;
	@Value("${spring.redis.password}")
	private String redisPassword;
    @Value("${spring.redis.database}")
	private int database;
    @Value("${spring.session.timeout}")
    private int expire;
    @Value("${spring.session.same-user-count}")
    private int sameUserCount;
    @Autowired
    private RedisService redisService;

	@Bean
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		// 自定义拦截器
		Map<String, Filter> filtersMap = new LinkedHashMap<>();
        // 自定义authc访问拦截器
        filtersMap.put("authc", new AuthcShiroFilter());
        // 自定义退出登录拦截器
        filtersMap.put("logout", shiroLogoutFilter());

		// 限制同一帐号同时在线的个数。
        if(sameUserCount > 0){
            filtersMap.put("kickout", kickOutSessionControlFilter());
        }
        shiroFilterFactoryBean.setFilters(filtersMap);

		// 权限控制map.
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		// 公共请求
		filterChainDefinitionMap.put("/common/**", "anon");
		// 静态资源
		filterChainDefinitionMap.put("/static/**", "anon");
		// 登录方法
		filterChainDefinitionMap.put("/auth/login", "anon");
		// 获取登录验证码
		filterChainDefinitionMap.put("/auth/generateImg/**", "anon");
        filterChainDefinitionMap.put("/auth/generateGif/**", "anon");
        // actuator监控
        filterChainDefinitionMap.put("/actuator/**", "anon");

        filterChainDefinitionMap.put("/auth/logout", "logout");

        // swagger相关直接放行
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/swagger**/**", "anon");
        filterChainDefinitionMap.put("/v2/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/**/*.js", "anon");
        filterChainDefinitionMap.put("/**/*.css", "anon");
        filterChainDefinitionMap.put("/**/*.html", "anon");

        if(sameUserCount > 0){
            // 此处需要添加一个kickout，上面添加的自定义拦截器才能生效，authc表示需要认证才能访问
            filterChainDefinitionMap.put("/**", "authc,kickout");
        }else{
            // authc表示需要认证才能访问
            filterChainDefinitionMap.put("/**", "authc");
        }
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

    /**
     * 自定义退出登录拦截器
     * @return
     */
	public ShiroLogoutFilter shiroLogoutFilter(){
	    ShiroLogoutFilter shiroLogoutFilter = new ShiroLogoutFilter();
        shiroLogoutFilter.setSessionControlFilter(kickOutSessionControlFilter());
	    return shiroLogoutFilter;
    }

	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm.
		securityManager.setRealm(myShiroRealm());
		// 自定义缓存实现 使用redis
		securityManager.setCacheManager(cacheManager());
		// 自定义session管理 使用redis
		securityManager.setSessionManager(sessionManager());
		return securityManager;
	}

	/**
	 * 身份认证realm; (这个需要自己写，账号密码校验；权限等)
	 *
	 * @return
	 */
	@Bean
	public ShiroRealm myShiroRealm() {
		ShiroRealm shiroRealm = new ShiroRealm();
		// 自定义密码验证器
        shiroRealm.setCredentialsMatcher(credentialsMatcher());
		// 启用身份验证器缓存，即缓存AuthenticationInfo信息，默认为false
        shiroRealm.setAuthenticationCachingEnabled(true);
        // 缓存AuthenticationInfo信息的名称
        shiroRealm.setAuthenticationCacheName(RedisKeyPrefix.AUTHENTICATION_NAME);
        // 启用授权缓存，即缓存AuthorizationInfo信息，默认为false
        shiroRealm.setAuthorizationCachingEnabled(true);
        // 缓存AuthorizationInfo信息的名称
        shiroRealm.setAuthorizationCacheName(RedisKeyPrefix.AUTHORIZATION_NAME);

		return shiroRealm;
	}

	@Bean
	public CredentialsMatcher credentialsMatcher() {
		return new CredentialsMatcher();
	}

	/**
	 * cacheManager 缓存 redis实现 使用的是shiro-redis开源插件
	 *
	 * @return
	 */
	public RedisCacheManager cacheManager() {
		RedisCacheManager redisCacheManager = new RedisCacheManager();
		redisCacheManager.setRedisManager(redisManager());
        // 设置前缀
		redisCacheManager.setKeyPrefix(RedisKeyPrefix.USER_CACHE);
        // 指定对象的字段的值为redis的key
        redisCacheManager.setPrincipalIdFieldName("account");
        // redis缓存时间
        redisCacheManager.setExpire(expire);

        // 自定义序列化方式
        // StringSerializer stringSerializer = new StringSerializer();
        // stringSerializer.setKeyPrefix(UserUtils.USER_CACHE);
        // redisCacheManager.setKeySerializer(stringSerializer);
        // redisCacheManager.setValueSerializer(new ValueSerializer(Object.class));
		return redisCacheManager;
	}

	/**
	 * RedisSessionDAO
     * shiro sessionDao层的实现 通过redis 使用的是shiro-redis开源插件
	 */
	@Bean
	public RedisSessionDAO redisSessionDAO() {
		RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
		redisSessionDAO.setRedisManager(redisManager());
		// session在redis中的保存时间，特殊值：-1 表示不超时，-2 表示指定超时时间与会话相同
		redisSessionDAO.setExpire(-2);

		redisSessionDAO.setKeyPrefix(RedisKeyPrefix.USER_SESSION);
		// redisSessionDAO.setKeySerializer(new StringSerializer());
		// redisSessionDAO.setValueSerializer(new JsonSerializer(Object.class));
		return redisSessionDAO;
	}

	/**
	 * Session Manager
     * 使用的是shiro-redis开源插件
	 */
	@Bean
	public DefaultWebSessionManager sessionManager() {
        /*SimpleCookie simpleCookie = new SimpleCookie("Token");
        simpleCookie.setPath("/");
        simpleCookie.setHttpOnly(false);*/

        SessionManager sessionManager = new SessionManager();
        // 全局会话超时时间，单位：ms
        sessionManager.setGlobalSessionTimeout(expire * 1000);
        // 是否开启删除无效的session对象，默认为true
        sessionManager.setDeleteInvalidSessions(true);
        // 是否开启定时任务调度器监测过期session，默认为true
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionDAO(redisSessionDAO());
        sessionManager.setCacheManager(cacheManager());

        //sessionManager.setSessionIdCookieEnabled(false);
        // 取消url后面的JESSIONID
        //sessionManager.setSessionIdUrlRewritingEnabled(false);
        //sessionManager.setSessionIdCookie(simpleCookie);
        return sessionManager;
	}

	/**
	 * 配置shiro redisManager 使用的是shiro-redis开源插件
     * redis独立版本
	 *
	 * @return
	 */
	public RedisManager redisManager() {
		RedisManager redisManager = new RedisManager();
		redisManager.setHost(redisHost + ":" + redisPort);
		// redisManager.setPort(redisPort);
		redisManager.setPassword(redisPassword);
		redisManager.setDatabase(database);
		return redisManager;
	}

	/**
	 * 限制同一账号登录同时登录人数控制
	 *
	 * @return
	 */
	@Bean
	public SessionControlFilter kickOutSessionControlFilter() {
		SessionControlFilter kickOutSessionControlFilter = new SessionControlFilter();
        kickOutSessionControlFilter.setRedisService(redisService);
        kickOutSessionControlFilter.setSessionManager(sessionManager());
        kickOutSessionControlFilter.setKickOutAfter(false);
        kickOutSessionControlFilter.setMaxSession(sameUserCount);
        kickOutSessionControlFilter.setKeyPrefix(RedisKeyPrefix.SAME_USER_SESSION_LIST);
        // kickOutSessionControlFilter.setKickOutUrl("/common/kickout");
		return kickOutSessionControlFilter;
	}

	/***
	 * 授权所用配置
	 *
	 * @return
	 */
	@Bean
	public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		return defaultAdvisorAutoProxyCreator;
	}

	/***
	 * 开启shiro注解模式，如@RequiresPermissions("user:add")
	 *
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * Shiro生命周期处理器 此方法需要用static作为修饰词，否则无法通过@Value()注解的方式获取配置文件的值
	 */
	@Bean
	public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}
}
