package com.imooc.mall.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {
    CUSTOMER(0,"普通用户"),

    ADMIN(1,"管理员")
    ;

    private Integer code;
    private String msg;

    RoleEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
