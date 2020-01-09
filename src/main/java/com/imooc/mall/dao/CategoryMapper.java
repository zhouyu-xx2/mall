package com.imooc.mall.dao;

import com.imooc.mall.pojo.Category;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


//@Mapper
public interface CategoryMapper {
    //根据注解方式查询数据库
    @Select("select * from mall_category where id = #{id}")
    Category findById(@Param("id") Integer id);
    //根据xml 查询数据库
    Category queryById(Integer id);
}
