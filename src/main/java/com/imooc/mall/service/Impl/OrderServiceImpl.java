package com.imooc.mall.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.dao.OrderItemMapper;
import com.imooc.mall.dao.OrderMapper;
import com.imooc.mall.dao.ProductMapper;
import com.imooc.mall.dao.ShippingMapper;
import com.imooc.mall.enums.ProductEnum;
import com.imooc.mall.pojo.*;
import com.imooc.mall.responseVo.OrderItemVo;
import com.imooc.mall.responseVo.OrderVo;
import com.imooc.mall.responseVo.ResponseVo;
import com.imooc.mall.service.ICartService;
import com.imooc.mall.service.IOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.imooc.mall.enums.OrderStatusEnum.CANCELED;
import static com.imooc.mall.enums.OrderStatusEnum.NO_PAY;
import static com.imooc.mall.enums.PaymentTypeEnum.PAY_ONLINE;
import static com.imooc.mall.enums.ResponseEnum.*;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private ICartService cartService;

    @Override
    @Transactional
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

        List<OrderItem> orderItemList = new ArrayList<>();
        //订单号
        Long orderNO = generateOrderNo();
        //这个商品是否存在
        for (Cart cart : cartList) {
            Product product = map.get(cart.getProductId());
            //判断是否有这个商品
            if (product == null) {
                return ResponseVo.error(PRODUCT_NO_EXIST,
                        "商品不存在，productID = " + cart.getProductId());
            }
            //判断商品上下架状态
            if (!ProductEnum.ON_SALE.getCode().equals(product.getStatus())) {
                return ResponseVo.error(OFF_SALE_OR_DELETE,
                        "商品下架或者删除 " + product.getName());
            }
            //判断这个商品的库存是否充足
            if (product.getStock() < cart.getQuantity()) {
                return ResponseVo.error(PRODUCT_STOCK_ERROR,
                        "库存不足!" + product.getName());
            }

            OrderItem orderItem = buildOrderItem(uid, orderNO, cart.getQuantity(), product);
            orderItemList.add(orderItem);

            //减库存
            product.setStock(product.getStock() - cart.getQuantity());
            int selective = productMapper.updateByPrimaryKeySelective(product);
            if (selective <= 0) {
                return ResponseVo.error(ERROR);
            }
        }
        //生成订单 与数据库交互 order 与 orderItem 同时写入
        Order order = buildOrder(uid, orderNO, shipingId, orderItemList);
        //写入order表
        int selective = orderMapper.insertSelective(order);
        if (selective <= 0) {
            return ResponseVo.error(ERROR);
        }
        //写入orderItem表

        int bathInsert = orderItemMapper.bathInsert(orderItemList);
        if (bathInsert <= 0) {
            return ResponseVo.error(ERROR);
        }
        //计算总价 只计算被选中的商品

        //更新购物车
        //
        //构造返回体 orderVo
        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);
        return ResponseVo.success(orderVo);
    }

    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectByUid(uid);

        Set<Long> orderNoSet = orderList.stream()
                .map(Order::getOrderNo)
                .collect(Collectors.toSet());
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);

        Set<Integer> shippingIdSet = orderList.stream()
                .map(Order::getShippingId)
                .collect(Collectors.toSet());
        List<Shipping> shippingList = shippingMapper.selectByShippingIdSet(shippingIdSet);

        //从order表中找到orderId 用orderId找到orderItem 中的数据
        // 可能一个orderID 对应几个orderItem 用map
        Map<Long, List<OrderItem>> orderItemMap = orderItemList.stream()
                .collect(Collectors.groupingBy(OrderItem::getOrderNo));

        Map<Integer, Shipping> shippingMap = shippingList.stream()
                .collect(Collectors.toMap(Shipping::getId, shipping -> shipping));
        List<OrderVo> orderVoList = new ArrayList<>();
        for (Order order : orderList) {
            OrderVo orderVo = buildOrderVo(order,
                    orderItemMap.get(order.getOrderNo()),
                    shippingMap.get(order.getShippingId()));
            orderVoList.add(orderVo);
        }

        PageInfo pageInfo = new PageInfo(orderList);
        pageInfo.setList(orderList);
        return ResponseVo.success(pageInfo);
    }

    @Override
    public ResponseVo<OrderVo> detail(Integer uid, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || !order.getUserId().equals(uid)) {
            return ResponseVo.error(ORDER_NOT_EXIST);
        }
        Set<Long> orderNoSet = new HashSet<Long>();
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);
        return ResponseVo.success(orderVo);
    }

    @Override
    public ResponseVo cancel(Integer uid, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || !order.getUserId().equals(uid)) {
            return ResponseVo.error(ORDER_NOT_EXIST);
        }
        if (!order.getStatus().equals(NO_PAY.getCode())) {
            return ResponseVo.error(ORDER_PAID);
        }
        order.setStatus(CANCELED.getCode());
        order.setCloseTime(new Date());
        int selective = orderMapper.updateByPrimaryKeySelective(order);
        if (selective <= 0) {
            return ResponseVo.error(ERROR);
        }
        return ResponseVo.success();
    }

    private OrderVo buildOrderVo(Order order, List<OrderItem> orderItemList, Shipping shipping) {
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order, orderVo);
        List<OrderItemVo> orderItemVoList = orderItemList.stream().map(orderItem -> {
            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(orderItem, orderItemVo);
            return orderItemVo;
        }).collect(Collectors.toList());
        orderVo.setOrderItemVoList(orderItemVoList);
        if (shipping != null) {
            orderVo.setShippingId(shipping.getId());
            orderVo.setShippingVo(shipping);
        }
        return orderVo;
    }

    private Order buildOrder(Integer uid, Long orderNO, Integer shippingId,
                             List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setOrderNo(orderNO);
        order.setUserId(uid);
        order.setShippingId(shippingId);
        //支付金额
        BigDecimal payment = orderItemList.stream().map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setPayment(payment);
        //支付方式 在线支付或者 货到付款
        order.setPaymentType(PAY_ONLINE.getCode());
        //运费
        order.setPostage(0);
        //订单状态
        order.setStatus(NO_PAY.getCode());
        return order;
    }

    //生成一个唯一的订单号
    private Long generateOrderNo() {
        return System.currentTimeMillis() + new Random().nextInt(999);
    }

    private OrderItem buildOrderItem(Integer uid, Long orderNo, Integer quantity, Product product) {
        OrderItem item = new OrderItem();
        item.setUserId(uid);
        item.setOrderNo(orderNo);
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductImage(product.getMainImage());
        item.setCurrentUnitPrice(product.getPrice());
        item.setQuantity(quantity);
        item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return item;

    }
}
