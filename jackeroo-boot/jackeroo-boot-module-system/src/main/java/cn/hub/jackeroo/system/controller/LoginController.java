package cn.hub.jackeroo.system.controller;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.enums.ResultStatusCode;
import cn.hub.jackeroo.persistence.BaseController;
import cn.hub.jackeroo.system.query.Account;
import cn.hub.jackeroo.system.service.SysOperateLogService;
import cn.hub.jackeroo.utils.RandImageUtil;
import cn.hub.jackeroo.utils.StringUtils;
import cn.hub.jackeroo.utils.annotation.OperateLog;
import cn.hub.jackeroo.utils.captcha.Captcha;
import cn.hub.jackeroo.utils.captcha.GifCaptcha;
import cn.hub.jackeroo.vo.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController extends BaseController {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String CAPTCHA_PREFIX = "CAPTCHA:";

    private final SysOperateLogService operateLogService;

    /**
     * 账号、密码登录
     * @param account
     * @return
     */
    @OperateLog(value = "用户登录", type = Constant.OPERATE_LOGIN)
	@RequestMapping("/login")
	public Result login(@Validated Account account) {
		try {
		    String captcha = redisTemplate.opsForValue().get(CAPTCHA_PREFIX + account.getKey());

            redisTemplate.delete(CAPTCHA_PREFIX + account.getKey());
		    if(StringUtils.isBlank(captcha) || !account.getCaptcha().equalsIgnoreCase(captcha)){
		        return result(ResultStatusCode.CAPTCHA_ERROR);
            }

			UsernamePasswordToken token = new UsernamePasswordToken(account.getAccount(), account.getPwd());
			// 登录不在该处处理，交由shiro处理
			Subject subject = SecurityUtils.getSubject();
			subject.login(token);

			if (subject.isAuthenticated()) {
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
    @OperateLog(value = "退出登录", type = Constant.OPERATE_LOGOUT)
	@RequestMapping("/logout")
	public Result logout(HttpServletRequest request) {
		// SecurityUtils.getSubject().logout();

		return ok();
	}

    /**
     * 获取验证码，服务器端生成图片，png
     * @param key
     * @return
     */
	@GetMapping("/generateImg/{key}")
	public Result generateImg(@PathVariable String key){
        try {
            String code = StringUtils.randomGen(4);
            String base64 = RandImageUtil.generate(code);

            redisTemplate.opsForValue().set(CAPTCHA_PREFIX + key, code, 5, TimeUnit.MINUTES);
            return ok(base64);
        } catch (IOException e) {
            return error("获取验证码失败");
        }
    }

    /**
     * 获取验证码，服务器端生成图片，gif
     * @param key
     * @return
     */
    @GetMapping("/generateGif/{key}")
    public void generateGif(@PathVariable String key, HttpServletResponse response){
	    try {
            response.setContentType("image/gif");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            Captcha captcha = new GifCaptcha(105,35,4);
            captcha.out(response.getOutputStream());

            redisTemplate.opsForValue().set(CAPTCHA_PREFIX + key, captcha.text(), 5, TimeUnit.MINUTES);
        }catch (IOException e){

        }
    }
}
