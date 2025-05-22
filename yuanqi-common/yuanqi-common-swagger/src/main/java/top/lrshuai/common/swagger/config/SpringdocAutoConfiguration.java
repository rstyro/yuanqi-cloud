package top.lrshuai.common.swagger.config;

import cn.hutool.core.net.NetUtil;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import top.lrshuai.common.core.constant.SecurityConst;
import top.lrshuai.common.core.factory.YmlPropertySourceFactory;

import java.util.Collections;


/**
 * swagger-ui 配置
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "springdoc.api-docs.enabled", matchIfMissing = true)
@Import({SpringdocProperties.class})
@PropertySource(value = "classpath:common-swagger.yml", factory = YmlPropertySourceFactory.class)
public class SpringdocAutoConfiguration {

    @Resource
    SpringdocProperties properties;

    @Value("${server.port}")
    private int port;


    @Value("${springdoc.swagger-ui.path}")
    private String path;

    @Bean
    public OpenAPI openAPI() {
        log.info("已启用swagger-ui,访问地址：http://{}:{}{}", NetUtil.getLocalhostStr(),port,path);
        return new OpenAPI().info(apiInfo())
                .servers(Collections.singletonList(new Server().url("http://localhost:" + port)))
                ;
    }


    /**
     * 自定义header参数
     * @return
     */
    @Bean
    public OperationCustomizer addGlobalHeader() {
        return (operation, handlerMethod) -> {
            operation.addParametersItem(buildHeaderParam(SecurityConst.TOKEN,"认证令牌","abc"));
            operation.addParametersItem(buildHeaderParam(SecurityConst.USER_ID,"用户身份ID","1"));
            operation.addParametersItem(buildHeaderParam(SecurityConst.PAGE_NO,"页码","1"));
            operation.addParametersItem(buildHeaderParam(SecurityConst.PAGE_SIZE,"页面大小","20"));
            return operation;
        };
    }

    // 提取公共方法构建 Header 参数
    private Parameter buildHeaderParam(String name, String description,String defaultValue) {
        return new Parameter()
                .in(ParameterIn.HEADER.toString())
                .name(name)
                .description(description)
                .required(false)
                .schema(new StringSchema().example(defaultValue)); // 添加示例值
    }

    /**
     * 添加文档标题等信息
     */
    private Info apiInfo() {
        return new Info()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .version(properties.getVersion())
                .contact(apiContact())
                .license(license())
                .termsOfService(properties.getTermsOfServiceUrl());
    }

    /**
     * 许可
     */
    private License license() {
        return new License()
                .name(properties.getLicense())
                .url(properties.getLicenseUrl());
    }


    /**
     * 联系人信息
     */
    private Contact apiContact() {
        return new Contact()
                .name(properties.getName())
                .url(properties.getAuthUrl())
                .email(properties.getEmail());
    }

}
