package cn.hub.jackeroo.online.utils;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.online.bo.GenTemplateBO;
import cn.hub.jackeroo.utils.FileUtils;
import cn.hub.jackeroo.utils.FreeMarkers;
import cn.hub.jackeroo.utils.Global;
import cn.hub.jackeroo.utils.PathUtil;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author alex
 * @date 2020/09/25
 */
@Slf4j
public class GenerateUtils {
    public static String generateToFile(Map<String, Object> model, boolean isReplaceFile) {
        // 获取生成文件
        String fileName = PathUtil.getFileSavePath(File.separator
                + StringUtils.replaceEach(FreeMarkers.renderString(  "/", model), new String[] { "//", "/", "." },
                new String[] { File.separator, File.separator, File.separator })
                + FreeMarkers.renderString("", model));
        log.info(" fileName === " + fileName);

        // 获取生成文件内容
        String content = FreeMarkers.renderString(StringUtils.trimToEmpty(""), model);
        log.debug(" content === \r\n" + content);

        // 如果选择替换文件，则删除原文件
        if (isReplaceFile) {
            FileUtils.deleteFile(fileName);
        }

        // 创建并写入文件
        if (FileUtils.createFile(fileName)) {
            FileUtils.writeToFile(fileName, content, true);
            log.info(" file create === " + fileName);
            return "生成成功：" + fileName + "<br/>";
        } else {
            log.info(" file extents === " + fileName);
            return "文件已存在：" + fileName + "<br/>";
        }
    }

    /**
     * 获取freemarker导出模板
     * @param templateRootPath
     * @param freeMarkerConfigurer
     * @param templateCode
     * @return
     */
    public static List<GenTemplateBO> getTemplateList(String templateRootPath, FreeMarkerConfigurer freeMarkerConfigurer, String templateCode){
        List<GenTemplateBO> list = new ArrayList<>();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources(templateRootPath);
            for (Resource resource : resources) {
                log.info("fileName={}", resource.getFilename());
                String fileName = resource.getFilename();
                String prefix = fileName.substring(0, fileName.indexOf(Constant.SPLIT_DOT));

                Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templateCode + File.separator +fileName);

                GenTemplateBO genTemplateBO = new GenTemplateBO();
                genTemplateBO.setTemplate(template);
                genTemplateBO.setTemplateName(prefix);

                list.add(genTemplateBO);
            }
        } catch (IOException e) {
            log.error("读取配置文件失败", e);
        }

        return list;
    }

    /**
     * 生成文件
     * @param dataMap
     * @param genTemplate
     * @param outRootPath
     */
    public static void generateFile(Map<String, Object> dataMap, GenTemplateBO genTemplate, String outRootPath){
        File outputFile = new File(outRootPath + File.separator + genTemplate.getOutputPath((String)dataMap.get("entityName")));
        FileUtils.createFileAndFolder(outputFile);
        try (
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)))
        ){
            genTemplate.getTemplate().process(dataMap, out);
        } catch (FileNotFoundException e) {
            log.error("输出文件不存在", e);
        } catch (IOException e) {
            log.error("输出文件异常", e);
        } catch (TemplateException e) {
            log.error("生成代码失败", e);
        }
    }
}
