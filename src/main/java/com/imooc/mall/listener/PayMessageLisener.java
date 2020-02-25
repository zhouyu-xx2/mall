package com.imooc.mall.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "payNotify")
@Slf4j
public class PayMessageLisener {

    @RabbitHandler
    public void process(String msg) {
        log.info("[接受到了消息  ---> {} ]", msg);
    }
}
