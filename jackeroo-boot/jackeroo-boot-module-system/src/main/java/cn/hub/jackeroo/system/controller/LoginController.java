package cn.hub.jackeroo.system.controller;

import cn.hub.jackeroo.enums.ResultStatusCode;
import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.system.query.Account;
import cn.hub.jackeroo.system.service.SysUserService;
import cn.hub.jackeroo.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class LoginController extends BaseController {
    @Autowired
    private SysUserService userService;

	@RequestMapping("/login")
	public Result login(@Validated Account account) {
		try {
			UsernamePasswordToken token = new UsernamePasswordToken(account.getAccount(), account.getPwd());
			// 登录不在该处处理，交由shiro处理
			Subject subject = SecurityUtils.getSubject();
			subject.login(token);

			if (subject.isAuthenticated()) {
				// JSON json = new JSONObject();
				// ((JSONObject) json).put("token", subject.getSession().getId());

				return ok(subject.getSession().getId());
			} else {
				return error("登录失败");
			}
		}
		catch (IncorrectCredentialsException | UnknownAccountException e) {
			return result(ResultStatusCode.NOT_EXIST_USER_OR_ERROR_PWD);
		}
		catch (LockedAccountException e) {
			return result(ResultStatusCode.USER_FROZEN);
		}
	}

	/**
	 * 退出登录
	 * 
	 * @return
	 */
	@RequestMapping("/logout")
	public Result logout() {
		SecurityUtils.getSubject().logout();
		return ok();
	}
}
