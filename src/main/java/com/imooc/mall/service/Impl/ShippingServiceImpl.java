package com.imooc.mall.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.dao.ShippingMapper;
import com.imooc.mall.form.ShippingAddForm;
import com.imooc.mall.pojo.Shipping;
import com.imooc.mall.responseVo.ResponseVo;
import com.imooc.mall.service.IShippingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.imooc.mall.enums.ResponseEnum.*;

@Slf4j
@Service
public class ShippingServiceImpl implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ResponseVo<Map<String, Integer>> add(Integer uid, ShippingAddForm shippingAddForm) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(shippingAddForm, shipping);
        shipping.setUserId(uid);
        int selective = shippingMapper.insertSelective(shipping);
        if (selective == 0) {
            return ResponseVo.error(ERROR);
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("shippingId", shipping.getId());

        return ResponseVo.success(map);
    }

    @Override
    public ResponseVo delete(Integer uid, Integer shippingId) {
        int i = shippingMapper.deleteByIdAndUid(uid, shippingId);
        if (i == 0) {
            return ResponseVo.error(DELETE_SHIPP_FAIL);
        }
        return ResponseVo.success(DELETE_SHIPP_SUCCESS);
    }

    @Override
    public ResponseVo update(Integer uid, Integer shippingId, ShippingAddForm shippingAddForm) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(shippingAddForm, shipping);
        shipping.setUserId(uid);
        shipping.setId(shippingId);
        int selective = shippingMapper.updateByPrimaryKeySelective(shipping);
        if (selective == 0) {
            return ResponseVo.error(ERROR);
        }
        return ResponseVo.success(UPDATE_SHIPP_SUCCESS);
    }

    @Override
    public ResponseVo<PageInfo> list(Integer pageNum, Integer pageSize, Integer uid) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippings = shippingMapper.selectByUid(uid);
        PageInfo pageInfo = new PageInfo(shippings);
        return ResponseVo.success(pageInfo);
    }
}
