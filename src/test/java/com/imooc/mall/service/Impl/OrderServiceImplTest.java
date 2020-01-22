package com.imooc.mall.service.Impl;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.responseVo.OrderVo;
import com.imooc.mall.responseVo.ResponseVo;
import com.imooc.mall.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class OrderServiceImplTest extends MallApplicationTests {
    @Autowired
    private IOrderService orderService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();


    @Test
    public void create() {
        ResponseVo<OrderVo> created = created();
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), created.getStatus());
    }

    private ResponseVo<OrderVo> created() {
        ResponseVo<OrderVo> responseVo = orderService.create(5, 5);
        log.info("create = {}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
        return responseVo;
    }

    @Test
    public void list() {
        ResponseVo<PageInfo> list = orderService.list(5, 1, 1);
        log.info("list = {}", gson.toJson(list));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), list.getStatus());
    }

    @Test
    public void detail() {
        ResponseVo<OrderVo> detail = orderService.detail(5, created().getData().getOrderNo());
        log.info("detail = {}", gson.toJson(detail));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), detail.getStatus());
    }
}