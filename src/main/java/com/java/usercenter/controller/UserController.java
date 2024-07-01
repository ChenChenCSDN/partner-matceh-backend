package com.java.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.usercenter.common.BaseResponse;
import com.java.usercenter.common.ErrorCode;
import com.java.usercenter.common.ResultUtils;
import com.java.usercenter.exception.BusinessException;
import com.java.usercenter.model.domain.User;
import com.java.usercenter.model.domain.request.UserLoginRequest;
import com.java.usercenter.model.domain.request.UserRegisterRequest;
import com.java.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.java.usercenter.constant.UserConstant.DEFAULT_ROLE;
import static com.java.usercenter.constant.UserConstant.USER_LOGIN_STATA;

/**
 * ClassName: UserController
 * Package: com.java.usercenter.controller
 * Description:用户接口
 *
 * @Author chen_sir
 * @Create 2024/6/15 14:00
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
//@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param: userLoginRequest 用户注册请求实体
     * @return: Long
     **/
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userLoginRequest) {
        //注册参数校验
        String userAccount = userLoginRequest.getUserAccount();
        String password = userLoginRequest.getPassword();
        String checkPassword = userLoginRequest.getCheckPassword();
        String communityCode = userLoginRequest.getCommunityCode();
        if (StringUtils.isAnyBlank(userAccount, password, checkPassword, communityCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户信息非法");
        }
        long l = userService.userRegister(userAccount, password, checkPassword, communityCode);
        //统一返回包装类
        return ResultUtils.success(l);
    }


    /**
     * 用户登录
     *
     * @param request
     * @param: userLoginRequest 用户登录请求实体
     * @return: BaseResponse<User> 统一返回包装类
     **/
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {

        //登录账户参数校验
        String userAccount = userLoginRequest.getUserAccount();
        String password = userLoginRequest.getPassword();
        if (StringUtils.isAnyBlank(userAccount, password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        User user = userService.userLogin(userAccount, password, request);
        //统一返回包装类
        return ResultUtils.success(user);
    }


    /**
     * 获取当前登录态
     *
     * @param: request
     * @return: BaseResponse<User> 统一返回包装类
     **/
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        //判断用户是否登录
        Object objUser = request.getSession().getAttribute(USER_LOGIN_STATA);
        User curentUser = (User) objUser;
        if (curentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "该用户未登录");
        }
        //查询数据库，更新用户信息
        User user = userService.getById(curentUser.getId());
        User safetyUser = userService.getSafetyUser(user);
        //统一返回包装类
        return ResultUtils.success(safetyUser);
    }

    /**
     * 根据用户名查询用户
     *
     * @param request
     * @param: userName 用户名称 Null-查询所有 notNull-模糊查询
     * @return: BaseResponse<List < User>> 统一返回包装类
     **/
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        //操作权限判定
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "该用户无管理员操作权限");
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNoneBlank(username)) {
            queryWrapper.like("username", username);
        }

        //查询用户
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());


        //判断用户是否查询成功
        if (list == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户未注册");
        }
        //统一返回包装类
        return ResultUtils.success(list);
    }

    /**
     * 根据id删除用户
     *
     * @param: id 用户id
     * @return: BaseResponse<Boolean> 统一返回包装类 Boolean true-删除成功 false-删除失败
     **/
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        //操作权限判定
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "该用户无管理员操作权限");
        }

        //id有效性判定
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户id非法");
        }

        //根据用户id删除用户
        boolean isDelete = userService.removeById(id);
        //统一返回包装类
        return ResultUtils.success(isDelete);
    }

    /**
     * 用户注销
     *
     * @param: request
     * @return: BaseResponse<Integer> 0-注销失败 1-注销成功
     **/
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        int isLogOut = userService.userLogout(request);
        //统一返回包装类
        return ResultUtils.success(isLogOut);
    }

    /**
     * 是否为管理员
     *
     * @param: request
     * @return: boolean true-是 false-否
     **/
    private boolean isAdmin(HttpServletRequest request) {
        //获取用户登录态
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATA);
        User user = (User) userObj;
        if (user == null) {
            return false;
        }
        if (user.getUserRole().equals(DEFAULT_ROLE)) {
            return false;
        }
        return true;
    }
}
