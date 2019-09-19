package com.zhengqing.utils;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import springfox.documentation.spring.web.json.Json;

/**
 * FastJsonHttpMessageConverterEx
 *
 * @author xiaojie
 * @date 2017/10/11
 */
public class FastJsonHttpMessageConverterEx extends FastJsonHttpMessageConverter {
    public FastJsonHttpMessageConverterEx() {
        super();
        this.getFastJsonConfig().getSerializeConfig().put(Json.class, SwaggerJsonSerializer.INSTANCE);
    }
}
