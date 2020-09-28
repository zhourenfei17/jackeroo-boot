package cn.hub.jackeroo.online.bo;

import cn.hub.jackeroo.utils.StringUtils;
import freemarker.template.Template;
import lombok.Data;

import java.io.File;

/**
 * @author alex
 * @date 2020/09/28
 */
@Data
public class GenTemplateBO {
    private String templateName;
    private Template template;

    public String getOutputPath(String entityName){
        if(StringUtils.isBlank(this.templateName)){
            return null;
        }
        if(this.templateName.equals("xml")){
            return "mapper/xml/" + entityName + "Mapper.xml";

        }else if(this.templateName.equals("vue")){
            return "vue/" + entityName + ".vue";
        }else{
            return this.templateName + File.separator + entityName + StringUtils.toUpperFirstLetter(this.templateName) + ".java";
        }
    }
}
