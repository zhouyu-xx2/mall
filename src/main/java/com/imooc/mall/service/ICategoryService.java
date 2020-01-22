package com.imooc.mall.service;

import com.imooc.mall.responseVo.CategoryVo;
import com.imooc.mall.responseVo.ResponseVo;

import java.util.List;
import java.util.Set;

public interface ICategoryService {
    ResponseVo<List<CategoryVo>> selectAll();

    void findBySubCategoryId(Integer id, Set<Integer> resultSet);
}
