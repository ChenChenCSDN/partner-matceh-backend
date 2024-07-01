package com.java.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.usercenter.common.ErrorCode;
import com.java.usercenter.exception.BusinessException;
import com.java.usercenter.mapper.UserMapper;
import com.java.usercenter.model.domain.User;
import com.java.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static com.java.usercenter.constant.UserConstant.USER_LOGIN_STATA;

/**
 * @author 16528
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-06-13 10:05:36
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    //密码加密的’盐‘
    private static final String SALT = "SALT";


    /**
     * 用户注册
     *
     * @param password      密码
     * @param checkPassword 校验码
     * @param communityCode
     * @param: userAccount 账户名称
     * @return: long
     **/
    @Override
    public long userRegister(String userAccount, String password, String checkPassword, String communityCode) {
        //1.账户信息校验
        //账户、密码、校验码、社区id为空
        if (StringUtils.isAnyBlank(userAccount, password, checkPassword, communityCode))
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户信息非法");
        //密码和校验密码不同
        if (!password.equals(checkPassword))
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码和校验码不同");
        //账户长度不小于4位，密码长度不小于8位
        if (userAccount.length() < 4 || password.length() < 8)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户或密码长度非法");
        //社区id长度大于5
        if (communityCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"社区id长度大于5");
        }
        //账户由数字字母和中文组成
        String regex = "^[a-zA-Z0-9\u4e00-\u9fa5]+$";
        if (!userAccount.matches(regex)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名称包含非法字符");
        }
        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"该用户已存在");
        }
        //社区id不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("communityCode", communityCode);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"社区id重复");
        }
        //2.密码加密
        String handlePassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());

        //3.存入数据库
        User user = new User();
        user.setUserAccount(userAccount);
        //将userName置为userAccount
        user.setUsername(userAccount);
        user.setUserPassword(handlePassword);
        user.setCommunityCode(communityCode);
        boolean save = this.save(user);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"系统保存用户信息异常");
        }
        return user.getId();
    }


    /**
     * 用户登录
     *
     * @param password 密码
     * @param request
     * @param: userAccount 账户名称
     * @return:User 登陆的用户
     **/
    @Override
    public User userLogin(String userAccount, String password, HttpServletRequest request) {
        //校验用户账户和密码是否合法
        if (userAccount == null || password == null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户信息非法");
        //账户长度不小于4位，密码长度不小于8位
        if (userAccount.length() < 4 || password.length() < 8)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户或密码长度非法");
        //账户由数字字母和中文组成
        String regex = "^[a-zA-Z0-9\u4e00-\u9fa5]+$";
        if (!userAccount.matches(regex)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码包含非法字符");
        }

        //2.密码加密
        String handlePassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());

        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassWord", handlePassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"该用户不存在,请检查账号和密码是否输入正确");
        }
        //3.用户脱敏
        User safetyUser = getSafetyUser(user);

        //4.记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATA, safetyUser);
        return user;
    }

    /**
     * 用户脱敏
     *
     * @param: originUser 待脱敏的用户
     * @return: User 脱敏后的用户
     **/
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
//        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUsername(originUser.getUserAccount());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setCommunityCode(originUser.getCommunityCode());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUpdateTime(originUser.getUpdateTime());
        return safetyUser;
    }

    /**
     * 根据姓名查询用户
     *
     * @param: userName 用户姓名
     * @return: List<User> 查询到的用户集合
     **/
    @Override
    public List<User> seletUsersByName(String userName) {
        //根据姓名查询用户
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.like("userAccount", userName);
//        userQueryWrapper.eq("userAccount",userName);
        List<User> userList = userMapper.selectList(userQueryWrapper);
        return userList;
    }

    /**
     * 用户注销接口
     *
     * @param: request
     * @return: int 0-注销失败 1-注销成功
     **/
    @Override
    public int userLogout(HttpServletRequest request) {
        //移除用户登录态
        request.getSession().removeAttribute(USER_LOGIN_STATA);
        return 1;
    }
}




