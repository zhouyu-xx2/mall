package com.imooc.mall.enums;

import lombok.Getter;

@Getter
public enum PaymentTypeEnum {
    PAY_ONLINE(1, "在线支付"),
    ;

    private Integer code;
    private String msg;

    PaymentTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}