package com.imooc.mall.enums;

import lombok.Getter;

@Getter
public enum ResponseEnum {
    ERROR(-1, "服务端错误"),

    SUCCESS(0, "成功"),

    PASSWORD_ERROR(1, "密码错误"),

    USERNAME_EXIST(2, "用户名已存在"),

    USERNAME_OR_PASSWORD_ERROR(11, "用户名或密码错误"),

    EMAIL_EXIST(4, "邮箱已存在"),

    PARAM_ERROR(3, "参数错误"),

    NEED_LOGIN(10, "用户未登陆，请重新登陆"),

    OFF_SALE_OR_DELETE(12, "商品下架或删除"),

    PRODUCT_NO_EXIST(13, "商品不存在"),

    PRODUCT_STOCK_ERROR(14, "商品库存不足"),

    CART_PRODUCT_NO_EXIST(15, "该商品不存在"),

    DELETE_SHIPP_SUCCESS(16, "删除收货地址成功"),

    DELETE_SHIPP_FAIL(17, "删除收货地址失败"),

    UPDATE_SHIPP_FAIL(18, "更新收货地址失败"),

    UPDATE_SHIPP_SUCCESS(19, "更新收货地址成功"),

    CART_SELECTED_IS_EMOTY(20, "没有该商品"),

    ORDER_NOT_EXIST(21, "订单不存在"),

    ORDER_PAID(22, "此订单已付款，无法被取消");

    Integer code;

    String desc;

    ResponseEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
