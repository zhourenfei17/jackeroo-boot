package cn.hub.jackeroo.system.controller;

import cn.hub.jackeroo.enums.ResultStatusCode;
import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.vo.Result;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("${adminPath}/common")
@RestController
public class CommonController extends BaseController {

	/**
	 * 未授权跳转方法
	 * 
	 * @return
	 */
	@RequestMapping("/unauth")
	public Result unauth() {
		SecurityUtils.getSubject().logout();
		return new Result(ResultStatusCode.UNAUTHO_ERROR);
	}

	/**
	 * 被踢出后跳转方法
	 * 
	 * @return
	 */
	@RequestMapping("/kickout")
	public Result kickout() {
		return new Result(ResultStatusCode.INVALID_TOKEN);
	}

}
