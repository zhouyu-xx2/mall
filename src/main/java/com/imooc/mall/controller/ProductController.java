package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.responseVo.ProductDetailVo;
import com.imooc.mall.responseVo.ResponseVo;
import com.imooc.mall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    @Autowired
    private IProductService iProductService;

    @GetMapping("/product")
    public ResponseVo<PageInfo> list(@RequestParam(required = false) Integer categoryId,
                                     @RequestParam(required = false) Integer pageNum,
                                     @RequestParam(required = false) Integer pageSize){
        return iProductService.list(categoryId,pageNum,pageSize);
    }
    @GetMapping("/products/{productId}")
    public ResponseVo<ProductDetailVo> detail(@PathVariable Integer productId){
        return iProductService.detail(productId);
    }

}
