package com.imooc.mall.enums;

import lombok.Getter;

@Getter
public enum ProductEnum {
    ON_SALE(1),

    OFF_SALE(2),

    DELETE(3),
    ;
    private Integer code;

    ProductEnum(Integer code) {
        this.code = code;
    }
}
