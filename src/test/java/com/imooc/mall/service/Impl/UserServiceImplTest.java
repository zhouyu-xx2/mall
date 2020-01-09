package com.imooc.mall.service.Impl;

import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.pojo.User;
import com.imooc.mall.responseVo.ResponseVo;
import com.imooc.mall.service.IUserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImplTest extends MallApplicationTests {
    public static final String USERNAME = "xxx233xx";
    public static final String PASSWORD = "123";
    @Autowired
    private IUserService service;


    @Before
    public void register() {
        User user = new User(USERNAME, PASSWORD, "12312@qq.com", 1);
        service.register(user);

    }

    @Test
    public void login() {
        ResponseVo<User> responseVo = service.login(USERNAME, PASSWORD);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }
}