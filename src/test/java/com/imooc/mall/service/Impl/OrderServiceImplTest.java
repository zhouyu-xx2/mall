package com.imooc.mall.service.Impl;

import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.responseVo.OrderVo;
import com.imooc.mall.responseVo.ResponseVo;
import com.imooc.mall.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

@Slf4j
public class OrderServiceImplTest extends MallApplicationTests {
    @Autowired
    private IOrderService orderService;

    @Test
    public void create() {
        ResponseVo<OrderVo> responseVo = orderService.create(5, 5);
        log.info("create = {}", responseVo);
    }
}