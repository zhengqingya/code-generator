package com.zhengqing.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.zhengqing.utils.FastJsonHttpMessageConverterEx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  <p> WebConfig </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/19 23:08
 */
@Slf4j
@Configuration()
public class WebConfig extends WebMvcConfigurerAdapter implements ServletContextInitializer {

    @Autowired
    MyProperties myProperties;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);

        FastJsonHttpMessageConverterEx fastConverter = new FastJsonHttpMessageConverterEx();

        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect
                , SerializerFeature.WriteNullListAsEmpty
                , SerializerFeature.WriteNullStringAsEmpty
                , SerializerFeature.WriteMapNullValue);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(fastConverter);
    }


    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        initProperties();
        loadAllUrlMapping(servletContext);
    }


    /**
     * 初始化
     */
    private void initProperties() {
        log.debug("============= 初始化系统配置参数 =============");
        Constants.IMG_DOMAIN = myProperties.getUpload().getStaticDomain() + "/";
    }

    /**
     * 读取URL映射
     *
     * @param servletContext
     */
    private void loadAllUrlMapping(ServletContext servletContext) {
        ApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        Map<String, HandlerMapping> requestMappings = BeanFactoryUtils.beansOfTypeIncludingAncestors(webApplicationContext, HandlerMapping.class, true, false);
        for (HandlerMapping handlerMapping : requestMappings.values()) {
            if (handlerMapping instanceof RequestMappingHandlerMapping) {
                RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) handlerMapping;
                Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
                for (RequestMappingInfo rmi : handlerMethods.keySet()) {
                    PatternsRequestCondition prc = rmi.getPatternsCondition();
                    HandlerMethod handlerMethod = handlerMethods.get(rmi);
                    Set<String> patterns = prc.getPatterns();
                    for (String uStr : patterns) {
                        Constants.URL_MAPPING_MAP.put(uStr, handlerMethod.getMethod().getDeclaringClass().getSimpleName() + "." + handlerMethod.getMethod().getName());
                    }
                }
            }
        }
    }
}
