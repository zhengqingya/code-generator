package com.zhengqing.modules.common.dto.output;

import com.zhengqing.modules.common.enumeration.ResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *  <p> API返回参数 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/7/20 11:09
 */
@ApiModel(value = "API返回参数")
public class ApiResult {
    // 消息内容
    @ApiModelProperty(value = "响应消息", required = false)
    private String message;

    // 成功或有效为1，失败或无效为0，token过期
    @ApiModelProperty(value = "结果码（1：成功；0：失败；-1：token过期）", required = true)
//    private Integer code;
    private Integer code;

    /*
    SUCCESS( 200, "SUCCESS" ),//成功
    FAILURE( 4000, "FAILURE" ),//失败
    UNAUTHORIZED( 4001, "未认证或Token失效" ),//未认证（签名错误、token错误）
    USER_UNAUTHORIZED( 4002, "用户名或密码不正确" ),//未通过认证
    NOT_FOUND( 4004, "接口不存在" ),//接口不存在
    INTERNAL_SERVER_ERROR( 5000, "服务器内部错误" );//服务器内部错误
    */


    // 响应中的数据
    @ApiModelProperty(value = "响应数据", required = false)
    private Object data;

    public static ApiResult expired(String message) {
        return new ApiResult(-1, message, null);
    }

    public static ApiResult fail(String message) {
        return new ApiResult(ResultCode.FAILURE.getCode(), message, null);
    }

    /***
     * 自定义错误返回码
     *
     * @param code
     * @param message:
     * @return: com.zhengqing.modules.common.dto.output.ApiResult
     */
    public static ApiResult fail(Integer code, String message) {
        return new ApiResult(code, message, null);
    }

    public static ApiResult ok(String message) {
        return new ApiResult(ResultCode.SUCCESS.getCode(), message, null);
    }

    public static ApiResult ok() {
        return new ApiResult(ResultCode.SUCCESS.getCode(), "OK", null);
    }

    public static ApiResult build(Integer code, String msg, Object data) {
        return new ApiResult(ResultCode.SUCCESS.getCode(), msg, data);
    }

    public static ApiResult ok(String message, Object data) {
        return new ApiResult(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 自定义返回码
     */
    public static ApiResult ok(Integer code, String message) {
        return new ApiResult(code, message);
    }

    /**
     * 自定义
     *
     * @param code：验证码
     * @param message：返回消息内容
     * @param data：返回数据
     * @return: com.zhengqing.modules.common.dto.output.ApiResult
     */
    public static ApiResult ok(Integer code, String message, Object data) {
        return new ApiResult(code, message, data);
    }

    public ApiResult() { }

    public static ApiResult build(Integer code, String msg) {
        return new ApiResult(code, msg, null);
    }

    public ApiResult(Integer code, String msg, Object data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    public ApiResult(Object data) {
        this.code = ResultCode.SUCCESS.getCode();
        this.message = "OK";
        this.data = data;
    }

    public ApiResult(String message) {
        this(ResultCode.SUCCESS.getCode(), message, null);
    }

    public ApiResult(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public ApiResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
