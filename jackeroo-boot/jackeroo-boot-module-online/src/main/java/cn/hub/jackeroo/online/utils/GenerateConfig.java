package cn.hub.jackeroo.online.utils;

import cn.hub.jackeroo.utils.factory.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 代码生成配置类
 * @author alex
 * @date 2020/09/25
 */
@Component
@PropertySource(value = "classpath:/template/generateConfig.yml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "generate")
@Data
public class GenerateConfig {
    private String sourceRootPackage;
    private String encoding;
    private String templateRootPath;
    private Map<String, String> javaType;
}
