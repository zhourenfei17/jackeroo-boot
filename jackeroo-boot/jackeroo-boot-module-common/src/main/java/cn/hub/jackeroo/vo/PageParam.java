package cn.hub.jackeroo.vo;

import io.swagger.annotations.ApiParam;
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
    @ApiParam(value = "当前页", required = true, example = "1")
    private Integer pageNo;
    @NotNull
    @Min(5)
    @Max(200)
    @ApiParam(value = "每页记录数", required = true, example = "10")
    private Integer pageSize;
}
