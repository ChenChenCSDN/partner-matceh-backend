package com.java.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * ClassName: UserLoginRequest
 * Package: com.java.usercenter.model.domain.request
 * Description:
 * 用户登录请求实体
 *
 * @Author chen_sir
 * @Create 2024/6/15 14:13
 * @Version 1.0
 */
@Data
public class UserRegisterRequest implements Serializable {
    //序列化ID
    private static final long serialVersionUID = -8954415858205083718L;

    //账户
    private String userAccount;

    //密码
    private String password;

    //校验密码
    private String checkPassword;

    //社区id编号
    private String communityCode;
}
