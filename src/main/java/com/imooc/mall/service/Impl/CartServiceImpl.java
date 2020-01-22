package com.imooc.mall.service.Impl;

import com.google.gson.Gson;
import com.imooc.mall.dao.ProductMapper;
import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.form.CartUpdateForm;
import com.imooc.mall.pojo.Cart;
import com.imooc.mall.pojo.Product;
import com.imooc.mall.responseVo.CartProductVo;
import com.imooc.mall.responseVo.CartVo;
import com.imooc.mall.responseVo.ResponseVo;
import com.imooc.mall.service.ICartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.imooc.mall.enums.ProductEnum.ON_SALE;
import static com.imooc.mall.enums.ResponseEnum.*;

@Service
@Slf4j
public class CartServiceImpl implements ICartService {
    private final static String CART_REDIS_KEY_TEMPLATE = "cart_%d";

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private Gson gson = new Gson();

    @Override
    public ResponseVo<CartVo> add(Integer uid, CartAddForm cartAddForm) {
        //商品是否存在
        Product product = productMapper.selectByPrimaryKey(cartAddForm.getProductId());
        if (product == null) {
            return ResponseVo.error(PRODUCT_NO_EXIST);
        }
        //商品是否是在售状态
        if (!product.getStatus().equals(ON_SALE.getCode())) {
            return ResponseVo.error(OFF_SALE_OR_DELETE);
        }
        //商品库存是否足够
        if (product.getStock() <= 0) {
            return ResponseVo.error(PRODUCT_STOCK_ERROR);
        }
        //写入redis
        //key 是自己定义的 cart_1
        Integer quantity = 1;
        Cart cart;
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String rediskey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        String value = opsForHash.get(rediskey, String.valueOf(product.getId()));
        if (StringUtils.isEmpty(value)) {
            //没有该商品 新增 写入redis
            cart = new Cart(product.getId(), quantity, cartAddForm.getSelected());
        } else {
            //已经有了 数量加1
            cart = gson.fromJson(value, Cart.class);
            cart.setQuantity(cart.getQuantity() + quantity);
        }
        opsForHash.put(rediskey,
                String.valueOf(product.getId()),
                gson.toJson(cart)
        );
        /*stringRedisTemplate.opsForValue().set(String.format(CART_REDIS_KEY_TEMPLATE, uid),
                gson.toJson(new Cart(product.getId(), quantity, cartAddForm.getSelected())));*/
        return list(uid);
        // HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();
    }

    /*
     * 购物车列表
     * */
    @Override
    public ResponseVo<CartVo> list(Integer uid) {
        //从redis中取出数据
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        //取出redis的id来得到redis的 key value
        String rediskey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        Map<String, String> entries = opsForHash.entries(rediskey);

        List<CartProductVo> cartProductVoList = new ArrayList<>();
        //cartProductVoList.add(cartProductVo);
        CartVo cartVo = new CartVo();
        Boolean selectedAll = true;
        //总价格设置为0
        BigDecimal cartTotalPrice = BigDecimal.ZERO;
        Integer cartTotalQuantity = 0;
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            Integer productId = Integer.valueOf(entry.getKey());
            Cart cart = gson.fromJson(entry.getValue(), Cart.class);

            Product product = productMapper.selectByPrimaryKey(productId);
            if (product != null) {
                CartProductVo cartProductVo = new CartProductVo(
                        productId,
                        cart.getQuantity(),
                        product.getName(),
                        product.getSubtitle(),
                        product.getMainImage(),
                        product.getPrice(),
                        product.getStatus(),
                        product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity() * cart.getQuantity())),
                        product.getStock(),
                        cart.getProductSelected()
                );
                cartProductVoList.add(cartProductVo);
                //如果没有全部都选中 则为false
                if (!cart.getProductSelected()) {
                    selectedAll = false;
                }
                //计算总价（只计算在购物车中被选中的）
                if (cart.getProductSelected()) {
                    cartTotalPrice = cartTotalPrice.add(cartProductVo.getProductTotalPrice());
                }
                cartTotalQuantity += cart.getQuantity();
            }

        }
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartTotalQuantity(cartTotalQuantity);
        cartVo.setSelectAll(selectedAll);
        return ResponseVo.success(cartVo);
    }

    @Override
    public ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm cartUpdateForm) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String rediskey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        String value = opsForHash.get(rediskey, String.valueOf(productId));
        if (StringUtils.isEmpty(value)) {
            //没有该商品 报错
            ResponseVo.error(CART_PRODUCT_NO_EXIST);
        }
        //已经有了 数量相加
        Cart cart = gson.fromJson(value, Cart.class);
        if (cartUpdateForm.getQuantity() != null
                && cartUpdateForm.getQuantity() >= 0) {
            cart.setQuantity(cartUpdateForm.getQuantity());
        }
        if (cartUpdateForm.getSelected() != null) {
            cart.setProductSelected(cartUpdateForm.getSelected());
        }
        opsForHash.put(rediskey, String.valueOf(productId), gson.toJson(cart));
        return list(uid);

    }

    @Override
    public ResponseVo<CartVo> delete(Integer productId, Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String rediskey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        String value = opsForHash.get(rediskey, String.valueOf(productId));
        if (StringUtils.isEmpty(value)) {
            //没有该商品 报错
            ResponseVo.error(CART_PRODUCT_NO_EXIST);
        }
        //有该商品 删除
        Cart cart = gson.fromJson(value, Cart.class);
        opsForHash.delete(rediskey, String.valueOf(productId), gson.toJson(cart));
        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> selectAll(Integer uid) {
        //从redis中取出数据
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        //取出redis的id来得到redis的 key value
        String rediskey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        Map<String, String> entries = opsForHash.entries(rediskey);

        for (Map.Entry<String, String> entry : entries.entrySet()) {
            Cart cart = gson.fromJson(entry.getValue(), Cart.class);
            cart.setProductSelected(true);
            opsForHash.put(rediskey,
                    String.valueOf(cart.getProductId()),
                    gson.toJson(cart)
            );
        }
        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> unselectAll(Integer uid) {
        //从redis中取出数据
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        //取出redis的id来得到redis的 key value
        String rediskey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        //Map<String, String> entries = opsForHash.entries(rediskey);

        List<Cart> cartList = listForCart(uid);
        for (Cart cart : cartList) {
            cart.setProductSelected(false);
            opsForHash.put(rediskey,
                    String.valueOf(cart.getProductId()),
                    gson.toJson(cart)
            );
        }

        /*for (Map.Entry<String, String> entry : entries.entrySet()) {
            Cart cart = gson.fromJson(entry.getValue(), Cart.class);
            cart.setProductSelected(false);
            opsForHash.put(rediskey,
                    String.valueOf(cart.getProductId()),
                    gson.toJson(cart)
            );
        }*/
        return list(uid);
    }

    @Override
    public ResponseVo<Integer> sum(Integer uid) {
        Integer sum = 0;
        for (Cart cart : listForCart(uid)) {
            sum += cart.getQuantity();
        }
       /* Integer sum = listForCart(uid).stream()
                .map(Cart::getQuantity)
                .reduce(0, Integer::sum);*/
        return ResponseVo.success(sum);
    }

    /*
     * 提取一个类
     * */
    public List<Cart> listForCart(Integer uid) {
        List<Cart> cartList = new ArrayList<>();
        //从redis中取出数据
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        //取出redis的id来得到redis的 key value
        String rediskey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        Map<String, String> entries = opsForHash.entries(rediskey);
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            Cart cart = gson.fromJson(entry.getValue(), Cart.class);
            cartList.add(cart);
        }
        return cartList;
    }
}
