package cn.hub.jackeroo.online.param;

import cn.hub.jackeroo.utils.validator.annotation.ForEachPattern;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 生成代码所需传递参数
 * @author alex
 * @date 2020/10/23
 */
@Data
public class GenerateCodeParam {

    @NotBlank
    private String id;
    @NotBlank
    private String outputDir;
    @NotNull
    @Range(max = 1)
    private Integer override;
    @NotNull
    @ForEachPattern(regexp = "^(controller|service|mapper|entity|vue)$")
    private List<String> templateType;

}
