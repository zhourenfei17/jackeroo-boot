package cn.hub.jackeroo.online.bo;

import freemarker.template.Template;
import lombok.Data;

/**
 * @author alex
 * @date 2020/09/28
 */
@Data
public class GenTemplateBO {
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件路径，完整包路径：${sourceRootPackage} + ${moduleName} + filePath
     */
    private String filePath;
    /**
     * 模板文件类型，(controller、service、mapper、entity、vue)
     */
    private String templateType;
    /**
     * 模板文件对象
     */
    private Template template;
}
