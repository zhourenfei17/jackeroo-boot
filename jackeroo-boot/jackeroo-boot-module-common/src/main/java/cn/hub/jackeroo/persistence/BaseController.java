package cn.hub.jackeroo.persistence;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.util.Map;

public abstract class BaseController {

	protected static final String successPath = "success";

	/**
	 * 管理基础路径
	 */
	@Value("${adminPath}")
	protected String adminPath;

	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;

	/**
	 * 为Thymeleaf添加全局参数
	 * 
	 * @param viewResolver
	 */
	@Resource
	private void configureThymeleafStaticVars(ThymeleafViewResolver viewResolver) {
		if (viewResolver != null) {
			Map<String, Object> vars = Maps.newHashMap();
			vars.put("ctx", "/admin");
			viewResolver.setStaticVariables(vars);
		}
	}

	/**
	 * 添加Model消息
	 * 
	 * @param messages
	 */
	protected void addMessage(ModelMap model, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			sb.append(message).append(messages.length > 1 ? "<br/>" : "");
		}
		model.addAttribute("message", sb.toString());
	}
}
