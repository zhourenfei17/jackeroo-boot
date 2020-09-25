package cn.hub.jackeroo.online.utils;

import cn.hub.jackeroo.utils.FileUtils;
import cn.hub.jackeroo.utils.FreeMarkers;
import cn.hub.jackeroo.utils.Global;
import cn.hub.jackeroo.utils.PathUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
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
}
