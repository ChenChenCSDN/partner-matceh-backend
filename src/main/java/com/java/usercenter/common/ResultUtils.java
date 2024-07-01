package com.java.usercenter.common;

/**
 * ClassName: ResultUtils
 * Package: com.java.usercenter.common
 * Description:
 *
 * @Author chen_sir
 * @Create 2024/6/24 10:49
 * @Version 1.0
 */
public class ResultUtils {
    /**
     * 成功
     *
     * @param: data 返回的数据
     * @return: BaseResponse<T> 统一返回包装类
     **/
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     *
     * @param: errorCode 错误码
     * @return: BaseResponse
     **/
    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse(errorCode);
    }

    public static BaseResponse error(ErrorCode errorCode, String description) {
        return new BaseResponse(errorCode.getCode(), null, description);
    }

    public static BaseResponse error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse(errorCode.getCode(), null, message, description);
    }
}
