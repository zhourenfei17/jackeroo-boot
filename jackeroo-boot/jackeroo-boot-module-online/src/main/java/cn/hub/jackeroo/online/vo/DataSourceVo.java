package cn.hub.jackeroo.online.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author alex
 * @date 2021/09/24
 */
@Data
public class DataSourceVo {
    /**
     * JDBC driver
     */
    @NotBlank
    private String driverClassName;
    /**
     * JDBC url 地址
     */
    @NotBlank
    private String url;
    /**
     * JDBC 用户名
     */
    @NotBlank
    private String username;
    /**
     * JDBC 密码
     */
    @NotBlank
    private String password;
}
