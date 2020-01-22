package com.imooc.mall.service.Impl;

import com.imooc.mall.dao.OrderMapper;
import com.imooc.mall.dao.ProductMapper;
import com.imooc.mall.dao.ShippingMapper;
import com.imooc.mall.pojo.Cart;
import com.imooc.mall.pojo.Product;
import com.imooc.mall.pojo.Shipping;
import com.imooc.mall.responseVo.OrderVo;
import com.imooc.mall.responseVo.ResponseVo;
import com.imooc.mall.service.ICartService;
import com.imooc.mall.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.imooc.mall.enums.ResponseEnum.*;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private ICartService cartService;

    @Override
    public ResponseVo<OrderVo> create(Integer uid, Integer shipingId) {
        //创建一个订单
        //查出来收货地址
        Shipping shipping = shippingMapper.selectByUidAndShippingId(uid, shipingId);
        //把购物车数据查出来
        List<Cart> cartList = cartService.listForCart(uid)
                .stream()
                .filter(Cart::getProductSelected)
                .collect(Collectors.toList());
        //判断集合是否为空 空返回 没有该商品被选中
        if (CollectionUtils.isEmpty(cartList)) {
            return ResponseVo.error(CART_SELECTED_IS_EMOTY);
        }
        //获取cartList中的productIds 放入set集合中
        Set<Integer> productIdSet = cartList.stream()
                .map(Cart::getProductId).collect(Collectors.toSet());
        List<Product> productList = productMapper.selectproductIdSet(productIdSet);
        //把商品放入一个map集合 一个ID对应一个商品
        Map<Integer, Product> map = productList
                .stream()
                .collect(Collectors.toMap(Product::getId, product -> product));
        //这个商品是否存在
        for (Cart cart : cartList) {
            Product product = map.get(cart.getProductId());
            //判断是否有这个商品
            if (product == null) {
                return ResponseVo.error(PRODUCT_NO_EXIST,
                        "商品不存在，productID = " + cart.getProductId());
            }
            //判断这个商品的库存是否充足
            if (product.getStock() < cart.getQuantity()) {
                return ResponseVo.error(PRODUCT_STOCK_ERROR,
                        "库存不足!" + product.getName());
            }
            //判断商品上下架状态
        }


        return null;
    }
}
