package cn.hub.jackeroo.system.controller;

import cn.hub.jackeroo.enums.ResultStatusCode;
import cn.hub.jackeroo.persistence.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("${adminPath}/login")
public class LoginController extends BaseController {

	/*@RequestMapping("")
	private String index(ModelMap model) {
		return "login";
	}

	@RequestMapping("/doLogin")
	public String login(String loginName, String pwd, ModelMap model) {
		try {
			UsernamePasswordToken token = new UsernamePasswordToken(loginName, pwd);
			// 登录不在该处处理，交由shiro处理
			Subject subject = SecurityUtils.getSubject();
			subject.login(token);

			if (subject.isAuthenticated()) {
				// JSON json = new JSONObject();
				// ((JSONObject) json).put("token", subject.getSession().getId());

				return "redirect:" + adminPath + "/index";
			} else {
				model.addAttribute("error", ResultStatusCode.SHIRO_ERROR.getMsg());
				return index(model);
			}
		}
		catch (IncorrectCredentialsException | UnknownAccountException e) {
			log.error("登录失败", e);
			model.addAttribute("error", ResultStatusCode.NOT_EXIST_USER_OR_ERROR_PWD.getMsg());
			return index(model);
		}
		catch (LockedAccountException e) {
			log.error("登录失败", e);
			model.addAttribute("error", ResultStatusCode.USER_FROZEN.getMsg());
			return index(model);
		}
		catch (Exception e) {
			log.error("登录失败", e);
			model.addAttribute("error", ResultStatusCode.SYSTEM_ERR.getMsg());
			return index(model);
		}
	}

	*//**
	 * 退出登录
	 * 
	 * @return
	 *//*
	@RequestMapping("/logout")
	public String logout() {
		SecurityUtils.getSubject().logout();
		return "redirect:" + adminPath + "/login";
	}*/
}
