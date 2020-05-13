package cn.hub.jackeroo.utils;

import cn.hub.jackeroo.vo.IUser;
import cn.hub.jackeroo.vo.LoginUser;
import org.apache.shiro.SecurityUtils;

public class UserUtils {

	public static final String USER_CACHE = "SPRINGBOOT_CACHE:";

	public static final String USER_SESSION = "SPRINGBOOT_SESSION:";

	public static final String USER_ROLE_MENULIST = "SPRINGBOOT_MENU:";

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
