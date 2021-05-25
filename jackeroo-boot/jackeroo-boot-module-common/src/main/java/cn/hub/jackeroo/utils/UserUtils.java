package cn.hub.jackeroo.utils;

import cn.hub.jackeroo.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;

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

    /**
     * 更新用户信息
     * @param user
     */
    public static void setUser(LoginUser user){
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection principals = subject.getPrincipals();
        //realName认证信息的key，对应的value就是认证的user对象
        String realName = principals.getRealmNames().iterator().next();
        //创建一个PrincipalCollection对象，userInfo是更新后的user对象
        PrincipalCollection newPrincipalCollection = new SimplePrincipalCollection(user, realName);
        //调用subject的runAs方法，把新的PrincipalCollection放到session里面
        subject.runAs(newPrincipalCollection);
    }

}
