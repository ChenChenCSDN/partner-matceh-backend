package com.java.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * ClassName: BaseResponse
 * Package: com.java.usercenter.common
 * Description:
 *
 * @Author chen_sir
 * @Create 2024/6/24 10:46
 * @Version 1.0
 */
@Data
public class BaseResponse<T> implements Serializable {

    //状态码
    private int code;
    //返回值
    private T data;
    //返回信息
    private String msg;
    //状态描述
    private String description;

    public BaseResponse(int code, T data, String msg, String description) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.description = description;
    }


    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");
    }

    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMsg(), errorCode.getDescription());
    }

    //序列化ID
    private static final long serialVersionUID = 2656238943159859475L;
}
