package com.imooc.mall.responseVo;

import lombok.Data;

import java.math.BigDecimal;

/*
 * 购物车
 * */
@Data
public class CartProductVo {
    private Integer productId;

    private Integer quantity;

    private String productName;

    private String productSubtitle;

    private String mainImage;

    private BigDecimal productPrice;

    private Integer productStatus;

    private BigDecimal productTotalPrice;

    private Integer productStock;

    /*商品是否被选中*/
    private Boolean productSelected;

    public CartProductVo(Integer productId, Integer quantity, String productName, String productSubtitle, String mainImage, BigDecimal productPrice, Integer productStatus, BigDecimal productTotalPrice, Integer productStock, Boolean productSelected) {
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.productSubtitle = productSubtitle;
        this.mainImage = mainImage;
        this.productPrice = productPrice;
        this.productStatus = productStatus;
        this.productTotalPrice = productTotalPrice;
        this.productStock = productStock;
        this.productSelected = productSelected;
    }

}
