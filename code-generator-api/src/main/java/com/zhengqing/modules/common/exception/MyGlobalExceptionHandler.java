package com.zhengqing.modules.common.exception;

import com.zhengqing.modules.common.dto.output.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

/**
 *  <p> 全局异常处理器 </p>
 *
 * @description: 在spring 3.2中，新增了@ControllerAdvice 注解，可以用于定义@ExceptionHandler、@InitBinder、@ModelAttribute，并应用到所有@RequestMapping中
 * @author: zhengqing
 * @date: 2019/8/25 0025 18:56
 */
@Slf4j
@RestControllerAdvice
public class MyGlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MyGlobalExceptionHandler.class);

    /**
     * 自定义异常处理
     */
    @ExceptionHandler(value = MyException.class)
    public ApiResult myException(MyException be) {
        log.error("自定义异常：", be);
        if(be.getCode() != null){
            return ApiResult.fail(be.getCode(), be.getMessage());
        }
        return ApiResult.fail( be.getMessage() );
    }

    // 参数校验异常处理 ===========================================================================
    // MethodArgumentNotValidException是springBoot中进行绑定参数校验时的异常,需要在springBoot中处理,其他需要处理ConstraintViolationException异常进行处理.

    /**
     * 方法参数校验
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult handleMethodArgumentNotValidException( MethodArgumentNotValidException e ) {
        log.error( "方法参数校验:" + e.getMessage(), e );
        return ApiResult.fail( e.getBindingResult().getFieldError().getDefaultMessage() );
    }

    /**
     * ValidationException
     */
    @ExceptionHandler(ValidationException.class)
    public ApiResult handleValidationException(ValidationException e) {
        log.error( "ValidationException:", e );
        return ApiResult.fail( e.getCause().getMessage() );
    }

    /**
     * ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResult handleConstraintViolationException(ConstraintViolationException e) {
        log.error( "ValidationException:" + e.getMessage(), e );
        return ApiResult.fail( e.getMessage() );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResult handlerNoFoundException(Exception e) {
        return ApiResult.fail( 404, "路径不存在，请检查路径是否正确" );
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ApiResult handleDuplicateKeyException(DuplicateKeyException e) {
        return ApiResult.fail( "数据重复，请检查后提交" );
    }









    //    ===============================================

    @ExceptionHandler(RuntimeException.class)
    public ApiResult handleRuntimeException(RuntimeException e) {
        LOG.error("系统异常:", e);
        return ApiResult.fail("系统异常，操作失败");
    }

    /**
     * 自定义Shiro异常 - 未授权异常
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ApiResult handleUnauthorizedException(UnauthorizedException e) {
        LOG.error("权限不足:", e);
        return ApiResult.fail(403,"未授权，请联系管理员获得权限！");
    }

    /**
     * 空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public ApiResult nullPointerExceptionHandler(NullPointerException ex) {
        log.error("空指针异常:", ex);
        return ApiResult.fail("空指针异常!");
    }

    /**
     * 类型转换异常
     */
    @ExceptionHandler(ClassCastException.class)
    public ApiResult classCastExceptionHandler(ClassCastException ex) {
        log.error("类型转换异常:", ex);
        return ApiResult.fail("类型转换异常!");
    }


    /**
     * 数组越界异常
     */
    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public ApiResult ArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException ex) {
        log.error("数组越界异常:", ex);
        return ApiResult.fail("数组越界异常!");
    }

    /**
     * 其他错误
     */
    @ExceptionHandler({Exception.class})
    public ApiResult exception(Exception ex) {
        log.error("其他错误:", ex);
        return ApiResult.fail( 500, "其他错误："+ ex );
    }

}
