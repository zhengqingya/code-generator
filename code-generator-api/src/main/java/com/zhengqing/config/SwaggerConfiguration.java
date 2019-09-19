package com.zhengqing.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.zhengqing.utils.JacksonAnnotationIntrospectorEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Springfox Swagger configuration.
 * <p>
 * Warning! When having a lot of REST endpoints, Springfox can become a performance issue. In that
 * case, you can use a specific Spring profile for this class, so that only front-end developers
 * have access to the Swagger view.
 */
@Configuration
@EnableSwagger2
@Import(springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {

    private final Logger LOG = LoggerFactory.getLogger(SwaggerConfiguration.class);

    public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";
//    public static final String DEFAULT_INCLUDE_PATTERN = "/.*";

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public ObjectMapper setObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        objectMapper.registerModule(module);
        objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospectorEx());
        return objectMapper;
    }

    /**
     * Swagger Springfox configuration.
     *
     * @param properties the properties of the application
     * @return the Swagger Springfox configuration
     */
    @Bean
    public Docket swaggerSpringfoxDocket(MyProperties properties) {
        LOG.debug("Starting Swagger");
        StopWatch watch = new StopWatch();
        watch.start();
        Contact contact = new Contact(
                properties.getSwagger().getContactName(),
                properties.getSwagger().getContactUrl(),
                properties.getSwagger().getContactEmail());

        ApiInfo apiInfo = new ApiInfo(
                properties.getSwagger().getTitle(),
                properties.getSwagger().getDescription(),
                properties.getSwagger().getVersion(),
                properties.getSwagger().getTermsOfServiceUrl(),
                contact,
                properties.getSwagger().getLicense(),
                properties.getSwagger().getLicenseUrl());

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .forCodeGeneration(true)
                .genericModelSubstitutes(ResponseEntity.class)
                .select()
                .paths(regex(DEFAULT_INCLUDE_PATTERN))
                .build();

        watch.stop();
        LOG.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
        return docket;
    }
}
