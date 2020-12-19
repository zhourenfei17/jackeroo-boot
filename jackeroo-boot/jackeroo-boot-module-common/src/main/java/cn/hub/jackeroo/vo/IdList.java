package cn.hub.jackeroo.vo;

import cn.hub.jackeroo.utils.validator.annotation.ForEachPattern;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author alex
 * @date 2020/12/17
 */
@Data
public class IdList {
    @NotEmpty
    @ForEachPattern(regexp = "^\\w+$")
    private List<String> ids;
}
