package com.java.usercenter.service;

import com.java.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 16528
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-06-13 10:05:36
*/

public interface UserService extends IService<User> {


    /**
     * 用户注册接口
     *
     * @param password      密码
     * @param checkPassword 校验密码
     * @param communityCode
     * @param: userAccount 用户账户
     * @return: long 用户id
     **/
    long userRegister(String userAccount, String password, String checkPassword,String communityCode);


    /***
     * 用户登录接口
     *
     * @param userAccount 用户账户
     * @param password 密码
     * @return 用户(脱敏）
     */
    User userLogin(String userAccount, String password, HttpServletRequest request);

    /**
     * 用户信息脱敏接口
     *
     * @param: originUser 原始用户信息
     * @return: User 脱敏后的用户信息
    **/
    User getSafetyUser(User originUser);

    /**
     * 查询用户接口
     *
     * @param: userName 待查询的用户名 null-查询所以 notnull-根据name查询
     * @return: List<User> 查询到的用户集合
    **/
    List<User> seletUsersByName(String userName);

    /**
     * 用户注销接口
     *
     * @param: request
     * @return: int 0-注销失败 1-注销成功
    **/
    int userLogout(HttpServletRequest request);
 }
