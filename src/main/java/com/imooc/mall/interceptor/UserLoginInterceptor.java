package com.imooc.mall.interceptor;

import com.imooc.mall.exception.UserLoginException;
import com.imooc.mall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.imooc.mall.consts.MallConst.CURRENT_USER;

@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {
    /*
    * 拦截器 返回值为true false
    * 如果能够拿到session 捕获成功 返回true
    * 没有拿到session则抛出一个异常————>（需要登陆）
    * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle....");
        User user = (User) request.getSession().getAttribute(CURRENT_USER);
        if (user == null) {
            log.info("user=null");
           throw new UserLoginException();
        }
        return true;
    }
}
