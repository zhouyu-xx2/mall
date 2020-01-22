package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.responseVo.ProductDetailVo;
import com.imooc.mall.responseVo.ResponseVo;

public interface IProductService {
    ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize);

    ResponseVo<ProductDetailVo> detail(Integer productId);
}
