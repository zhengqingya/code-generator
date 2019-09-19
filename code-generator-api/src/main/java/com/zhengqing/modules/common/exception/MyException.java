package com.zhengqing.modules.common.exception;

/**
 *  <p> 自定义异常类 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/26 15:11
 */
public class MyException extends RuntimeException {

    /**
     * 异常状态码
     */
    private Integer code;

    public MyException(Throwable cause) {
        super(cause);
    }

    public MyException(String message) {
        super(message);
    }

    public MyException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public MyException(String message, Throwable cause) {
        super(message, cause);
    }

    public Integer getCode() {
        return code;
    }

}
