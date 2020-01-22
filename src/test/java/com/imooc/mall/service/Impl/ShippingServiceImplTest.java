package com.imooc.mall.service.Impl;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.form.ShippingAddForm;
import com.imooc.mall.pojo.Shipping;
import com.imooc.mall.responseVo.ResponseVo;
import com.imooc.mall.service.IShippingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Slf4j
public class ShippingServiceImplTest extends MallApplicationTests {
    @Autowired
    private IShippingService iShippingService;

    @Test
    public void add() {
        ShippingAddForm shippingAddForm = new ShippingAddForm();
        shippingAddForm.setReceiverMobile("110");
        shippingAddForm.setReceiverAddress("安徽省");
        shippingAddForm.setReceiverName("宿州市");
        ResponseVo<Map<String, Integer>> add = iShippingService.add(5, shippingAddForm);
        log.info("add = {}", add);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), add.getStatus());
    }

    @Test
    public void delete() {
        ResponseVo<Shipping> delete = iShippingService.delete(5, 1);
        log.info("delete = {}", delete);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), delete.getStatus());
    }

    @Test
    public void update() {
        ShippingAddForm shippingAddForm = new ShippingAddForm();
        shippingAddForm.setReceiverMobile("112");
        shippingAddForm.setReceiverAddress("安徽省");
        shippingAddForm.setReceiverName("宿州市");
        ResponseVo update = iShippingService.update(5, 2, shippingAddForm);
        log.info("update = {}", update);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), update.getStatus());
    }

    @Test
    public void list() {
        ResponseVo<PageInfo> list = iShippingService.list(1, 10, 5);
        log.info("list = {}", list);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), list.getStatus());

    }
}