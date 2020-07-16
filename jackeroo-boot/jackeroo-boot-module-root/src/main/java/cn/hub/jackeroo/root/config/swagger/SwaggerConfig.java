package cn.hub.jackeroo.root.config.swagger;

import cn.hub.jackeroo.constant.ParamType;
import cn.hub.jackeroo.enums.ResultStatusCode;
import cn.hub.jackeroo.utils.annotation.ApiModule;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * swagger2配置
 * @author alex
 * @date 2020/06/04
 */
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
@EnableWebMvc
@Slf4j
public class SwaggerConfig implements InitializingBean {

    @Autowired
    private ApplicationContext applicationContext;

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("jackeroo-boot接口文档")
                .description("jackeroo-boot致力打造一个快速开发的java平台")
                .termsOfServiceUrl("http://localhost:8081/jackeroo/api")
                .contact(new Contact("alex", "http://boot.jackeroo-hub.cn","zhourenei17@126.com"))
                .version("1.0")
                .build();
    }

    /**
     * 全局请求参数定义
     * @return
     */
    private List<Parameter> parameters(){
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new ParameterBuilder()
                .name("Access-Token")
                .description("认证token")
                .modelRef(new ModelRef("string"))
                .parameterType(ParamType.HEADER)
                .required(true)
                .build());

        return parameters;
    }

    /**
     * 全局错误码定义
     * @return
     */
    private List<ResponseMessage> responseCode(){
        List<ResponseMessage> responseMessageList = new ArrayList<>();
        for (ResultStatusCode status : ResultStatusCode.values()) {
            responseMessageList.add(new ResponseMessageBuilder().code(status.getCode()).message(status.getMsg()).build());
        }
        return responseMessageList;
    }

    private Class[] initIgnorableTypes() {
        Set<Class> ignored = new HashSet();
        ignored.add(ServletRequest.class);
        ignored.add(Class.class);
        ignored.add(Void.class);
        ignored.add(Void.TYPE);
        ignored.add(HttpServletRequest.class);
        ignored.add(HttpServletResponse.class);
        ignored.add(HttpHeaders.class);
        ignored.add(BindingResult.class);
        ignored.add(ServletContext.class);
        ignored.add(UriComponentsBuilder.class);
        ignored.add(Page.class);
        ignored.add(ApiIgnore.class); //Used to ignore parameters

        Class[] array = ignored.toArray(new Class[0]);
        return array;
    }

    private Docket buildDocketWithGroupName(String groupName) {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName(groupName)
                .globalOperationParameters(parameters())
                .globalResponseMessage(RequestMethod.GET, responseCode())
                .globalResponseMessage(RequestMethod.POST, responseCode())
                .globalResponseMessage(RequestMethod.PUT, responseCode())
                .globalResponseMessage(RequestMethod.DELETE, responseCode())
                .select()
                .apis(input -> {
                    if (input.getHandlerMethod().hasMethodAnnotation(ApiModule.class)) {
                        ApiModule apiModule = input.getHandlerMethod().getMethodAnnotation(ApiModule.class);
                        if (apiModule.moduleName().equals(groupName)) {
                            return true;
                        }

                    }
                    ApiModule clazzApiModule = input.getHandlerMethod().getBeanType().getAnnotation(ApiModule.class);
                    if (clazzApiModule != null) {
                        if (clazzApiModule.moduleName().equals(groupName)) {
                            return true;
                        }
                    }
                    return false;
                })//controller路径
                .paths(PathSelectors.any())
                .build()
                .ignoredParameterTypes(initIgnorableTypes());
    }

    /**
     * 自动扫描所有@ApiModule的类，根据一级模块分组，并生成swagger文档
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Object> map = applicationContext.getBeansWithAnnotation(ApiModule.class);
        Map<String, ApiModule> apiVersionMap = new HashMap();
        for (String key : map.keySet()) {
            ApiModule apiModule = map.get(key).getClass().getAnnotation(ApiModule.class);

            apiVersionMap.put(apiModule.moduleName(), apiModule);
        }

        AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        if(autowireCapableBeanFactory instanceof DefaultListableBeanFactory){
            DefaultListableBeanFactory capableBeanFactory = (DefaultListableBeanFactory) autowireCapableBeanFactory;
            for (String key : apiVersionMap.keySet()) {
                AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition().setFactoryMethodOnBean("buildDocketWithGroupName", "swaggerConfig")
                        .addConstructorArgValue(apiVersionMap.get(key).moduleName()).getBeanDefinition();
                capableBeanFactory.registerBeanDefinition(apiVersionMap.get(key).moduleName(), beanDefinition);
            }
        }
    }
}
