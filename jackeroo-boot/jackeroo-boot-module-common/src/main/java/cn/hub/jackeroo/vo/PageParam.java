package cn.hub.jackeroo.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author alex
 * @date 2020/06/03
 */
@Data
public class PageParam {
    @NotNull
    @Min(1)
    private Integer pageNo;
    @NotNull
    @Min(5)
    @Max(200)
    private Integer pageSize;
}
