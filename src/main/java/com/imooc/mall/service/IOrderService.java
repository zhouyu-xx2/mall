package com.imooc.mall.service;

import com.imooc.mall.responseVo.OrderVo;
import com.imooc.mall.responseVo.ResponseVo;

public interface IOrderService {

    ResponseVo<OrderVo> create(Integer uid,Integer shipingId);
}
