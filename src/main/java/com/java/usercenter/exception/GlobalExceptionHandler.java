package com.java.usercenter.exception;

import com.java.usercenter.common.BaseResponse;
import com.java.usercenter.common.ErrorCode;
import com.java.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ClassName: GlobalExceptionHandler
 * Package: com.java.usercenter.exception
 * Description:
 *
 * @Author chen_sir
 * @Create 2024/6/24 16:14
 * @Version 1.0
 */
@RestControllerAdvice
//@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse gloablException(BusinessException e) {
        log.error("businessException： " + e.getMessage(), e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), e.getDescription());
    }

    public BaseResponse<?> runtinmeExceptionHandler(RuntimeException e){
        log.error("runtimeException",e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"");
    }
}
