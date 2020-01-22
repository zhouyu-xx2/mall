package com.imooc.mall.service.Impl;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.responseVo.ResponseVo;
import com.imooc.mall.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ProductServiceImplTest extends MallApplicationTests {
    @Autowired
    private IProductService iProductService;

    @Test
    public void list() {
        ResponseVo<PageInfo> list = iProductService.list(null, 1, 1);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), list.getStatus());
        log.info("list = {}", list);


    }
}