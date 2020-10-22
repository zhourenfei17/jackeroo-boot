package cn.hub.jackeroo.online.utils;

import cn.hub.jackeroo.constant.Constant;
import cn.hub.jackeroo.online.bo.GenTemplateBO;
import cn.hub.jackeroo.utils.FileUtils;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
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
            Resource[] resources = resolver.getResources(templateRootPath + "/*");
            for (Resource resource : resources) {
                String fileName = resource.getFilename();
                if(resource.getFile().isDirectory()){
                    getTemplateInResources(templateRootPath, new StringBuilder().append(File.separator).append(fileName), resolver, freeMarkerConfigurer, templateCode, list);
                }else{
                    list.add(loadTemplate(fileName, "", freeMarkerConfigurer, templateCode));
                }
            }
        } catch (IOException e) {
            log.error("读取配置文件失败", e);
        }


        return list;
    }

    /**
     * 循环获取指定根目录下所有的模板文件
     * @param rootPath
     * @param appendPath
     * @param resolver
     * @param freeMarkerConfigurer
     * @param templateCode
     * @param list
     */
    private static void getTemplateInResources(String rootPath, StringBuilder appendPath, ResourcePatternResolver resolver, FreeMarkerConfigurer freeMarkerConfigurer, String templateCode, List<GenTemplateBO> list){
        try {
            Resource[] resources = resolver.getResources(rootPath + appendPath + "/*");
            for (Resource resource : resources) {
                String fileName = resource.getFilename();
                if(resource.getFile().isDirectory()){
                    appendPath.append(File.separator).append(fileName);
                    getTemplateInResources(rootPath, appendPath, resolver, freeMarkerConfigurer, templateCode, list);
                }else{
                    list.add(loadTemplate(fileName, appendPath.toString(), freeMarkerConfigurer, templateCode));
                }
            }
        } catch (IOException e) {
            log.error("rootPath={},appendPath={}", rootPath, appendPath);
            log.error("读取配置文件失败", e);
        }
    }

    /**
     * 读取单个freemarker模板文件
     * @param fileName
     * @param filePath
     * @param freeMarkerConfigurer
     * @param templateCode
     * @return
     * @throws IOException
     */
    private static GenTemplateBO loadTemplate(String fileName, String filePath, FreeMarkerConfigurer freeMarkerConfigurer, String templateCode) throws IOException {
        String generateFileName = fileName.substring(0, fileName.lastIndexOf(Constant.SPLIT_DOT));

        Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templateCode + filePath + File.separator +fileName);

        GenTemplateBO genTemplateBO = new GenTemplateBO();
        genTemplateBO.setTemplate(template);
        genTemplateBO.setFileName(generateFileName);
        genTemplateBO.setFilePath(filePath);

        return genTemplateBO;
    }

    /**
     * 生成文件
     * @param dataMap
     * @param genTemplate
     * @param outRootPath
     */
    public static void generateFile(Map<String, Object> dataMap, GenTemplateBO genTemplate, String outRootPath, Integer override){
        //替换文件名变量
        genTemplate.setFileName(genTemplate.getFileName().replace("${entityName}", (String)dataMap.get("entityName")));

        File outputFile = new File(outRootPath + genTemplate.getFilePath() + File.separator + genTemplate.getFileName());
        if(outputFile.exists() && Constant.BOOLEAN_NO == override){
            log.debug("文件路径【{}】已存在，未覆盖重新生成代码", outputFile.getPath());
            return;
        }
        FileUtils.createFileAndFolder(outputFile);
        try (
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)))
        ){
            genTemplate.getTemplate().process(dataMap, out);
            log.debug("生成文件成功！文件路径【{}】", outputFile.getPath() + outputFile.getName());
        } catch (FileNotFoundException e) {
            log.error("输出文件不存在", e);
        } catch (IOException e) {
            log.error("输出文件异常", e);
        } catch (TemplateException e) {
            log.error("生成代码失败", e);
        }
    }
}
