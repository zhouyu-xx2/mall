package com.imooc.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/*
* 添加商品
* */
@Data
public class CartAddForm {

    @NotNull
    private Integer productId;
    //商品是否被选中 true为选中
    private Boolean selected = true;

}
