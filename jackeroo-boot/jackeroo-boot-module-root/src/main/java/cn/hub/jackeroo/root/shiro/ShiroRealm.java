package cn.hub.jackeroo.root.shiro;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.system.entity.SysMenu;
import cn.hub.jackeroo.system.entity.SysUser;
import cn.hub.jackeroo.system.mapper.SysMenuMapper;
import cn.hub.jackeroo.system.service.SysUserService;
import cn.hub.jackeroo.utils.StringUtils;
import cn.hub.jackeroo.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ShiroRealm extends AuthorizingRealm {

	@Autowired
    @Lazy   // 必须延时加载，原因是bean实例化的顺序上，shiro的bean必须要先实例化，否则@cacheable注解无效
	private SysUserService userService;
	@Resource
    @Lazy
	private SysMenuMapper menuMapper;

	/**
	 * 认证信息.(身份验证) : Authentication 是用来验证用户身份
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		log.info("---------------- 执行 Shiro 凭证认证 ----------------------");
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String name = token.getUsername();
		// 从数据库获取对应用户名密码的用户
		SysUser user = userService.findByAccount(name);
		if (user != null) {
			// 用户为禁用状态
			if (user.getStatus().intValue() == Constant.USER_STATUS_FROZEN) {
				throw new DisabledAccountException("用户账号不可用！");
			}
			log.info("---------------- Shiro 凭证认证成功 ----------------------");

            LoginUser loginUser = new LoginUser();
            BeanUtils.copyProperties(user, loginUser);

			SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(loginUser, // 用户
			        user.getPassword(), // 密码
			        getName() // realm name
			);
			return authenticationInfo;
		}
		throw new UnknownAccountException("用户名或者密码错误！");
	}

    /**
     * 授权用户权限
     * 授权的方法是在碰到Controller方法上包含的Shiro注解(@RequiresPermissions、@RequiresRoles等)时调用
     * 它会去检测shiro框架中的权限(这里的permissions)是否包含有该标签的name值,如果有,里面的内容显示
     * 如果没有,里面的内容不予显示(这就完成了对于权限的认证.)
     *
     * shiro的权限授权是通过继承AuthorizingRealm抽象类，重载doGetAuthorizationInfo();
     * 当访问到页面的时候，链接配置了相应的权限或者shiro标签才会执行此方法否则不会执行
     * 所以如果只是简单的身份认证没有权限的控制的话，那么这个方法可以不进行实现，直接返回null即可。
     *
     * 在这个方法中主要是使用类：SimpleAuthorizationInfo 进行角色的添加和权限的添加。
     * authorizationInfo.addRole(role.getRole()); authorizationInfo.addStringPermission(p.getPermission());
     *
     * 当然也可以添加set集合：roles是从数据库查询的当前用户的角色，stringPermissions是从数据库查询的当前用户对应的权限
     * authorizationInfo.setRoles(roles); authorizationInfo.setStringPermissions(stringPermissions);
     *
     * 就是说如果在shiro配置文件中添加了filterChainDefinitionMap.put("/add", "perms[权限添加]");
     * 就说明访问/add这个链接必须要有“权限添加”这个权限才可以访问
     *
     * 如果在shiro配置文件中添加了filterChainDefinitionMap.put("/add", "roles[100002]，perms[权限添加]");
     * 就说明访问/add这个链接必须要有 "权限添加" 这个权限和具有 "100002" 这个角色才可以访问
     * @param principals
     * @return
     */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		log.info("---------------- 执行 Shiro 权限获取 ---------------------");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

		LoginUser user = (LoginUser) principals.getPrimaryPrincipal();
		if(user != null){
            info.addRole(user.getRoleCode());

            List<SysMenu> menuList = menuMapper.findMenuByRoleId(user.getRoleId());
            if(CollectionUtils.isNotEmpty(menuList)){
                Set<String> permissionSet = menuList.stream()
                        .filter(item -> StringUtils.isNotBlank(item.getPermission()))
                        .map(SysMenu::getPermission)
                        .collect(Collectors.toSet());
                if(CollectionUtils.isNotEmpty(permissionSet)){
                    info.addStringPermissions(permissionSet);
                }
            }
        }
		log.info("---------------- 获取到以下权限 ----------------");
		log.info(info.getStringPermissions().toString());
		log.info("---------------- Shiro 权限获取成功 ----------------------");
        return info;
	}

    @Override
    public Object getAuthorizationCacheKey(PrincipalCollection principals) {
        LoginUser user = (LoginUser) principals.getPrimaryPrincipal();
        return user.getAccount();
    }

    @Override
    public Object getAuthenticationCacheKey(PrincipalCollection principals) {
        LoginUser user = (LoginUser) principals.getPrimaryPrincipal();
        return user.getAccount();
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    /**
     * 自定义方法：清除所有 授权缓存
     */
    public void clearAllCachedAuthorizationInfo(){
	    getAuthorizationCache().clear();
    }

    /**
     * 自定义方法：清除所有 认证缓存
     */
    public void clearAllCachedAuthenticationInfo(){
	    getAuthenticationCache().clear();
    }

    /**
     * 自定义方法：清除所有 认证缓存 和 授权缓存
     */
    public void clearAllCache() {
	    clearAllCachedAuthenticationInfo();
	    clearAllCachedAuthorizationInfo();
    }
}
