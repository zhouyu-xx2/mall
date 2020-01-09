package com.imooc.mall.controller;

import com.imooc.mall.pojo.User;
import com.imooc.mall.responseVo.ResponseVo;
import com.imooc.mall.service.IUserService;
import com.imooc.mall.userform.UserLoginForm;
import com.imooc.mall.userform.UserRegisterForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.imooc.mall.consts.MallConst.CURRENT_USER;
import static com.imooc.mall.enums.ResponseEnum.PARAM_ERROR;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private IUserService service;

    @PostMapping("/register")
    public ResponseVo<User> register(@Valid @RequestBody UserRegisterForm userRegisterForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.error("注册提交的参数有误，{} {}",
                    bindingResult.getFieldError().getField(),
                    bindingResult.getFieldError().getDefaultMessage());
            return ResponseVo.error(PARAM_ERROR, bindingResult);
        }
        // log.info("username{}=", userForm.getUsername());
        User user = new User();
        BeanUtils.copyProperties(userRegisterForm, user);
        return service.register(user);

    }

    @PostMapping("/user/login")
    public ResponseVo<User> login(@Valid @RequestBody UserLoginForm userLoginForm, BindingResult bindingResult,
                                  HttpSession session) {
        if (bindingResult.hasErrors()) {
            log.error("注册提交的参数有误，{} {}",
                    bindingResult.getFieldError().getField(),
                    bindingResult.getFieldError().getDefaultMessage());
            return ResponseVo.error(PARAM_ERROR, bindingResult);
        }
        ResponseVo<User> userResponseVo = service.login(userLoginForm.getUsername(), userLoginForm.getPassword());
        session.setAttribute(CURRENT_USER, userResponseVo.getData());
        return userResponseVo;
    }

    @GetMapping("/user")
    public ResponseVo<User> userInfo(HttpSession session) {
        log.info("/user getSessionId= {}",session.getId());
        User user = (User) session.getAttribute(CURRENT_USER);
        return ResponseVo.success(user);
    }

    @PostMapping("/user/logout")
    public ResponseVo<User> logout(HttpSession session) {
        log.info("/user/logout getSessionId= {}",session.getId());
        session.removeAttribute(CURRENT_USER);
        return ResponseVo.success();
    }
}
