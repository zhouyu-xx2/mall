package com.imooc.mall.dao;

import com.imooc.mall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectcategoryIdSet(@Param("categoryIdSet") Set<Integer> categoryIdSet);

    List<Product> selectproductIdSet(@Param("productIdSet") Set<Integer> productIdSet);

}