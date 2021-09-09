package cn.hub.jackeroo.system.query;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author alex
 * @date 2021/09/09
 */
@Data
public class ChangePwd {
    @NotBlank
    private String pwd;
}
