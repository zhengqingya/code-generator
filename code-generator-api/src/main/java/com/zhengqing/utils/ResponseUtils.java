package com.zhengqing.utils;

import com.alibaba.fastjson.JSON;
import com.zhengqing.modules.common.dto.output.ApiResult;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletResponse;
import java.io.PrintWriter;

/**
 *  <p> 使用response输出JSON </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/26 10:10
 */
@Slf4j
public class ResponseUtils {

    public static void out(ServletResponse response, ApiResult result) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            out = response.getWriter();
            out.println(JSON.toJSONString(result));
        } catch (Exception e) {
            log.error(e + "输出JSON出错");
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

}
