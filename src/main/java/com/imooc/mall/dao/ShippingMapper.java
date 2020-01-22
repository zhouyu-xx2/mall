package com.imooc.mall.dao;

import com.imooc.mall.pojo.Shipping;
import com.imooc.mall.pojo.ShippingExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByIdAndUid(@Param("uid") Integer uid,
                         @Param("shippingId") Integer shippingId);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    Shipping selectByUidAndShippingId(@Param("uid") Integer uid,
                                      @Param("shippingId") Integer shippingId);

    int updateByExampleSelective(@Param("record") Shipping record, @Param("example") ShippingExample example);

    int updateByExample(@Param("record") Shipping record, @Param("example") ShippingExample example);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    List<Shipping> selectByUid(Integer uid);

    List<Shipping> selectByShippingIdSet(@Param("shippingIdSet") Set<Integer> shippingIdSet);
}