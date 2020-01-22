package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.form.ShippingAddForm;
import com.imooc.mall.responseVo.ResponseVo;

import java.util.Map;

public interface IShippingService {
    ResponseVo<Map<String, Integer>> add(Integer uid, ShippingAddForm shippingAddForm);

    ResponseVo delete(Integer uid, Integer shippingId);

    ResponseVo update(Integer uid, Integer shippingId, ShippingAddForm shippingAddForm);

    ResponseVo<PageInfo> list(Integer pageNum, Integer pageSize, Integer uid);
}
