package cn.hub.jackeroo.utils;

import cn.hub.jackeroo.vo.LoginUser;
import org.apache.shiro.SecurityUtils;

public class UserUtils {

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
