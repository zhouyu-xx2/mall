package com.imooc.mall.dao;

import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.pojo.Order;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class OrderMapperTest extends MallApplicationTests {
    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void countByExample() {
    }

    @Test
    public void deleteByExample() {
    }

    @Test
    public void deleteByPrimaryKey() {
    }

    @Test
    public void insert() {
    }

    @Test
    public void insertSelective() {
    }

    @Test
    public void selectByExample() {
    }

    @Test
    public void selectByPrimaryKey() {
        Order order = orderMapper.selectByPrimaryKey(1);
        System.out.println(order.toString());
    }

    @Test
    public void updateByExampleSelective() {
    }

    @Test
    public void updateByExample() {
    }

    @Test
    public void updateByPrimaryKeySelective() {
    }

    @Test
    public void updateByPrimaryKey() {
    }
}