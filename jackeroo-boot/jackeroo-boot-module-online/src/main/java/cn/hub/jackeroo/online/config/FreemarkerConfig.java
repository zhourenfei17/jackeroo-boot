package cn.hub.jackeroo.online.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

/**
 * @author alex
 * @date 2020/09/27
 */
@Configuration
@Lazy
public class FreemarkerConfig {
    @Autowired
    private GenerateConfig generateConfig;

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer(){
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setTemplateLoaderPath(generateConfig.getTemplateRootPath());
        configurer.setDefaultEncoding(generateConfig.getEncoding());

        return configurer;
    }
}
