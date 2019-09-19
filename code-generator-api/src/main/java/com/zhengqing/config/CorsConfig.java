package com.zhengqing.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *  <p> 全局配置解决跨域 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/22 9:09
 */
@Configuration
public class CorsConfig {

    private CorsConfiguration config() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        // ① 设置你要允许的网站域名，如果全允许则设为 *
        corsConfiguration.addAllowedOrigin("*");
        // corsConfiguration.addAllowedOrigin("http://www.zhengqing520.com");
        // ② 如果要限制 HEADER 或 METHOD 请自行更改
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration( "/**", config() );
        return new CorsFilter(source);
    }

//    @Bean
//    public FilterRegistrationBean corsFilter2() {
//        FilterRegistrationBean bean = new FilterRegistrationBean( corsFilter() );
//        // 这个顺序很重要哦，为避免麻烦请设置在最前
//        bean.setOrder(0);
//        return bean;
//    }

}

//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        // /**: 表示本应用的所有方法都会去处理跨域请求
//        registry.addMapping("/**")
//                // ① 设置你要允许的网站域名，如果全允许则设为 *
////                .allowedOrigins("http://127.0.0.1:8080")
//                .allowedOrigins("*")
//                // ② allowedMethods表示允许通过的请求数
//                .allowedMethods("*")
//                // ③ allowedHeaders则表示允许的请求头
//                .allowedHeaders("*");
//    }
//}

