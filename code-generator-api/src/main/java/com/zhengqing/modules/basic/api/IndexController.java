package com.zhengqing.modules.basic.api;

import com.zhengqing.modules.basic.dto.output.Server;
import com.zhengqing.modules.common.api.BaseController;
import com.zhengqing.modules.common.dto.output.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  <p> 首页 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/19 13:49
 */
@RestController
@RequestMapping("/api")
@Api(description = "首页-接口")
public class IndexController extends BaseController {

    @Value(value = "${spring.application.name}")
    private String applicationName;

    @GetMapping("/")
    public void index(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append( "<html>" )
                .append( "<body>" )
                .append( "<h2 style=\"margin: 50px 0 0 80px;\">" )
                .append( "您好，欢迎访问【" + applicationName + "】" )
                .append( "</h2>" )
                .append( "</body>" )
                .append( "</html>" );
        response.setHeader( "content-type", "text/html;charset=UTF-8" );
        response.getWriter().write( builder.toString() );
    }

    @GetMapping(value = "/server", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "获取服务器信息", httpMethod = "GET", response = ApiResult.class, notes = "获取服务器信息")
    public ApiResult server() throws Exception {
        Server server = new Server();
        server.copyTo();
        return ApiResult.ok("获取服务器信息成功", server);
    }

    /**
     * 解决微信验证js时填写域名问题 TODO 需修改，还未成功 </p>
     * @return
     * @throws Exception
     */
    @GetMapping("/MP_verify_eRiMMTWC9HkTsWSv.txt")
    public String getMP_verify_eRiMMTWC9HkTsWSv() {
        return "eRiMMTWC9HkTsWSv";
    }

}
