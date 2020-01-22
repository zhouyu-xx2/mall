package com.imooc.mall.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.dao.ProductMapper;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.pojo.Product;
import com.imooc.mall.responseVo.ProductDetailVo;
import com.imooc.mall.responseVo.ProductVo;
import com.imooc.mall.responseVo.ResponseVo;
import com.imooc.mall.service.ICategoryService;
import com.imooc.mall.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.imooc.mall.enums.ProductEnum.DELETE;
import static com.imooc.mall.enums.ProductEnum.OFF_SALE;

@Service
@Slf4j
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ICategoryService iCategoryService;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize) {
        //商品ID传到set集合中保存
        Set<Integer> categoryIdSet = new HashSet<>();
        //用商品ID查询出来子类集合 放入set集合中
        if (categoryId != null) {
            iCategoryService.findBySubCategoryId(categoryId, categoryIdSet);
            //把传进来的商品ID也要放入set集合中保存
            categoryIdSet.add(categoryId);
        }

        //分页 用插件pagehelper
        PageHelper.startPage(pageNum,pageSize);
        //根据商品ID以及子类ID查询出来商品列表信息
        List<Product> products = productMapper.selectcategoryIdSet(categoryIdSet);
        //lamda表达式的方式得到结果集
        List<ProductVo> productVoList = products.stream()
                .map(product -> {
                    ProductVo productVo = new ProductVo();
                    BeanUtils.copyProperties(product, productVo);
                    return productVo;
                }).collect(Collectors.toList());
        PageInfo pageInfo = new PageInfo<>(products);
        pageInfo.setList(productVoList);
        //for 循环的方式得到结果集
        /*List<ProductVo> productVoList = new ArrayList<>();
        for (Product product : products) {
            ProductVo productVo = new ProductVo();
            BeanUtils.copyProperties(product,productVo);
            productVoList.add(productVo);
        }*/
        return ResponseVo.success(pageInfo);
    }

    @Override
    public ResponseVo<ProductDetailVo> detail(Integer productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product.getStatus().equals(OFF_SALE.getCode())
                || product.getStatus().equals(DELETE.getCode())){
            return ResponseVo.error(ResponseEnum.OFF_SALE_OR_DELETE);
        }
        ProductDetailVo productDetailVo = new ProductDetailVo();
        BeanUtils.copyProperties(product,productDetailVo);
        return ResponseVo.success(productDetailVo);
    }
}
