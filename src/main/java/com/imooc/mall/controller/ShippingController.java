package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.form.ShippingAddForm;
import com.imooc.mall.pojo.User;
import com.imooc.mall.responseVo.ResponseVo;
import com.imooc.mall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

import static com.imooc.mall.consts.MallConst.CURRENT_USER;

@RestController
public class ShippingController {
    @Autowired
    private IShippingService iShippingService;

    @PostMapping("/shippings")
    public ResponseVo<Map<String, Integer>> add(@Valid @RequestBody ShippingAddForm shippingAddForm,
                                                HttpSession session) {
        User user = (User) session.getAttribute(CURRENT_USER);
        return iShippingService.add(user.getId(), shippingAddForm);
    }

    @DeleteMapping("/shippings/{shippingId}")
    public ResponseVo delete(@PathVariable Integer shippingId,
                             HttpSession session) {
        User user = (User) session.getAttribute(CURRENT_USER);
        return iShippingService.delete(user.getId(), shippingId);
    }

    @PutMapping("/shippings/{shippingId}")
    public ResponseVo update(@PathVariable Integer shippingId,
                             @Valid @RequestBody ShippingAddForm shippingAddForm,
                             HttpSession session) {
        User user = (User) session.getAttribute(CURRENT_USER);
        return iShippingService.update(user.getId(), shippingId, shippingAddForm);
    }

    @GetMapping("/shippings")
    public ResponseVo<PageInfo> list(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                     HttpSession session) {
        User user = (User) session.getAttribute(CURRENT_USER);
        return iShippingService.list(pageNum, pageSize, user.getId());

    }

}
