package cn.hub.jackeroo.root.shiro;

import cn.hub.jackeroo.system.entity.Menu;
import cn.hub.jackeroo.system.entity.Role;
import cn.hub.jackeroo.system.entity.User;
import cn.hub.jackeroo.system.service.MenuService;
import cn.hub.jackeroo.system.service.RoleService;
import cn.hub.jackeroo.system.service.UserService;
import cn.hub.jackeroo.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

import java.util.List;

@Slf4j
public class ShiroRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private MenuService menuService;

	/**
	 * 认证信息.(身份验证) : Authentication 是用来验证用户身份
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		log.info("---------------- 执行 Shiro 凭证认证 ----------------------");
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String name = token.getUsername();
		// 从数据库获取对应用户名密码的用户
		User user = userService.getUserByName(name);
		if (user != null) {
			// 用户为禁用状态
			if (user.getFlag().intValue() != 0) {
				throw new DisabledAccountException();
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
		throw new UnknownAccountException();
	}

	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		log.info("---------------- 执行 Shiro 权限获取 ---------------------");
		Object principal = principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		if (principal instanceof User) {
			User userLogin = (User) principal;
			if (userLogin != null) {
				List<Role> roleList = roleService.findByUserid(userLogin.getId());
				if (CollectionUtils.isNotEmpty(roleList)) {
					for (Role role : roleList) {
						info.addRole(role.getName());

						List<Menu> menuList = menuService.getAllMenuByRoleId(role.getId());
						if (CollectionUtils.isNotEmpty(menuList)) {
							for (Menu menu : menuList) {
								if (StringUtils.isNoneBlank(menu.getPermission())) {
									info.addStringPermission(menu.getPermission());
								}
							}
						}
					}
				}
			}
		}
		log.info("---------------- 获取到以下权限 ----------------");
		log.info(info.getStringPermissions().toString());
		log.info("---------------- Shiro 权限获取成功 ----------------------");
		return info;
	}
}
