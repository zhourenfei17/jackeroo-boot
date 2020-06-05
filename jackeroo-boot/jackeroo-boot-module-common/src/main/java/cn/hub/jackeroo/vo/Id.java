package cn.hub.jackeroo.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author alex
 * @date 2020/06/05
 */
@Data
public class Id {
    @NotBlank
    private String id;
}
