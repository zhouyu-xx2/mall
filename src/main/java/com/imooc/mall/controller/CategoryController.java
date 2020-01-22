package com.imooc.mall.controller;

import com.imooc.mall.responseVo.CategoryVo;
import com.imooc.mall.responseVo.ResponseVo;
import com.imooc.mall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @RequestMapping("/category")
    @ResponseBody
    public ResponseVo<List<CategoryVo>> category() {
        return categoryService.selectAll();
    }
}
