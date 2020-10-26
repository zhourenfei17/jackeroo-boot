package cn.hub.jackeroo.online.config;

import cn.hub.jackeroo.utils.factory.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author alex
 * @date 2020/10/23
 */
@Component
@PropertySource(value = "classpath:/config.yml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "config")
@Data
public class Config {
    /**
     * mysql字段类型对应java变量类型的mapping关系
     */
    private Map<String, String> mysqlToJavaMapping;
}
