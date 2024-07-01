package com.java.usercenter.service;
import java.util.Date;

import com.java.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * ClassName: serviceTest
 * Package: com.java.usercenter.service
 * Description:
 *
 * @Author chen_sir
 * @Create 2024/6/13 10:15
 * @Version 1.0
 */
@SpringBootTest
public class serviceTest {
    @Resource
    private UserService userService;

    @Test
    public void testAdduser(){
        //使用generator进行自动填充user实例


    }
}
