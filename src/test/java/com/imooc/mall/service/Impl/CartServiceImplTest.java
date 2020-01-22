package com.imooc.mall.service.Impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.form.CartUpdateForm;
import com.imooc.mall.responseVo.CartVo;
import com.imooc.mall.responseVo.ResponseVo;
import com.imooc.mall.service.ICartService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class CartServiceImplTest extends MallApplicationTests {
    @Autowired
    private ICartService iCartService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void add() {
        CartAddForm form = new CartAddForm();
        form.setProductId(29);
        form.setSelected(true);
        iCartService.add(1, form);
    }

    @Test
    public void testlist() {
        ResponseVo<CartVo> list = iCartService.list(1);
        log.info("list = {}", gson.toJson(list));
    }

    @Test
    public void update() {
        CartUpdateForm cartUpdateForm = new CartUpdateForm();
        cartUpdateForm.setQuantity(10);
        ResponseVo<CartVo> update = iCartService.update(1, 27, cartUpdateForm);
        log.info("update = {}", gson.toJson(update));
    }

    @Test
    public void delete() {
        ResponseVo<CartVo> delete = iCartService.delete(26, 5);
        log.info("delete = {}", gson.toJson(delete));
    }

    @Test
    public void selectAll() {
        ResponseVo<CartVo> selectAll = iCartService.selectAll(1);
        log.info("selectAll = {}", gson.toJson(selectAll));
    }

    @Test
    public void unselectAll() {
        ResponseVo<CartVo> unselectAll = iCartService.unselectAll(1);
        log.info("unselectAll = {}", gson.toJson(unselectAll));
    }

    @Test
    public void sum(){
        ResponseVo<Integer> sum = iCartService.sum(1);
        log.info("sum = {}",gson.toJson(sum));
    }
}