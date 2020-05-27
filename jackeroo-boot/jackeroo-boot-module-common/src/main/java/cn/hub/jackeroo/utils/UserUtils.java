package cn.hub.jackeroo.utils;

import cn.hub.jackeroo.vo.LoginUser;
import org.apache.shiro.SecurityUtils;

public class UserUtils {

	public static final String USER_CACHE = "SYS:CACHE:USER:";

	public static final String USER_SESSION = "SYS:SESSION:USER:";

	public static final String USER_ROLE_MENULIST = "SYS:CACHE:MENU:";

	/**
	 * 获取当前用户信息
	 * 
	 * @return
	 */
	public static LoginUser getUser() {
		try {
			return (LoginUser) SecurityUtils.getSubject().getPrincipal();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
