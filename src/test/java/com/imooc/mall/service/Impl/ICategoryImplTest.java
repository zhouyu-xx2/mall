package com.imooc.mall.service.Impl;

import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.responseVo.CategoryVo;
import com.imooc.mall.responseVo.ResponseVo;
import com.imooc.mall.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class ICategoryImplTest extends MallApplicationTests {

    @Autowired
    private ICategoryService iCategoryService;

    @Test
    public void selectAll() {
        ResponseVo<List<CategoryVo>> listResponseVo = iCategoryService.selectAll();
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), listResponseVo.getStatus());
    }

    @Test
    public void findBySubCategoryId() {
        Set<Integer> set = new HashSet<>();
        iCategoryService.findBySubCategoryId(100001, set);
        log.info(" set = {}", set);
    }
}