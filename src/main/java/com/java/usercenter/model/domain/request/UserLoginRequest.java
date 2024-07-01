package com.java.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * ClassName: UserLoginRequest
 * Package: com.java.usercenter.model.domain.request
 * Description:
 *
 * @Author chen_sir
 * @Create 2024/6/15 14:45
 * @Version 1.0
 */
@Data
public class UserLoginRequest implements Serializable {

    //序列化ID
    private static final long serialVersionUID = 6201523852355183111L;

    //账户名
    private String userAccount;

    //登录密码
    private String password;

}
