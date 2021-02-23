package cn.hub.jackeroo.system.query;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 登录用的查询对象
 * @author alex
 * @date 2020/05/26
 */
@Getter
@Setter
public class Account {
    @NotBlank
    private String account;
    @NotBlank
    private String pwd;
    @NotBlank
    private String captcha;
    @NotBlank
    private String key;
}
