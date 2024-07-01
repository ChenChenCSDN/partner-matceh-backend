package com.java.usercenter.exception;

import com.java.usercenter.common.ErrorCode;

/**
 * ClassName: BusinessException
 * Package: com.java.usercenter.exception
 * Description:
 *
 * @Author chen_sir
 * @Create 2024/6/24 11:59
 * @Version 1.0
 */
public class BusinessException extends RuntimeException {
    //异常码
    private final int code;

    //异常描述
    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
