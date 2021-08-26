package cn.hub.jackeroo.root.shiro;

import cn.hub.jackeroo.utils.encrypt.PasswordUtil;
import cn.hub.jackeroo.vo.LoginUser;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

public class CredentialsMatcher extends SimpleCredentialsMatcher {

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		UsernamePasswordToken utoken = (UsernamePasswordToken) token;
		// 获得用户输入的密码:(可以采用加盐(salt)的方式去检验)
		String inPassword = new String(utoken.getPassword());
		// 获得数据库中的密码
		String dbPassword = (String) info.getCredentials();

		LoginUser user = (LoginUser) info.getPrincipals().getPrimaryPrincipal();
		// 进行密码的比对
		return this.equals(PasswordUtil.encrypt(user.getAccount(), inPassword, user.getSalt()), dbPassword);
	}
}
