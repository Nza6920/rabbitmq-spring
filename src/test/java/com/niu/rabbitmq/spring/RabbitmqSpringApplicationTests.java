package com.niu.rabbitmq.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitmqSpringApplicationTests {
    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testRabbitAdmin() {

          // 声明交换机
//        rabbitAdmin.declareExchange(new DirectExchange("test.rabbit.admin.direct.exchange", false, false));
//        rabbitAdmin.declareExchange(new TopicExchange("test.rabbit.admin.topic.exchange", false, false));
//        rabbitAdmin.declareExchange(new FanoutExchange("test.rabbit.admin.fanout.exchange", false, false));

          // 声明队列
//        rabbitAdmin.declareQueue(new Queue("test.rabbit.admin.direct.queue", false));
//        rabbitAdmin.declareQueue(new Queue("test.rabbit.admin.topic.queue", false));
//        rabbitAdmin.declareQueue(new Queue("test.rabbit.admin.fanout.queue", false));
//
          // 绑定
//        rabbitAdmin.declareBinding(new Binding("test.rabbit.admin.direct.queue",
//                Binding.DestinationType.QUEUE,
//                "test.rabbit.admin.direct",
//                "rabbit.admin.direct.#",
//                new HashMap<>()));

          // 绑定 2
//        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("test.rabbit.admin.topic.queue", false)) // 创建队列
//                .to(new TopicExchange("test.rabbit.admin.topic.exchange", false, false)) // 创建交换机
//                .with("rabbit.admin.topic.#"));     // 建立关联关系
//
//        // 绑定 3
//        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("test.rabbit.admin.fanout.queue", false)) // 创建队列
//                .to(new FanoutExchange("test.rabbit.admin.fanout.exchange", false, false)));   // 创建交换机

        // 清空队列
        rabbitAdmin.purgeQueue("test.rabbit.admin.topic.queue", false);
    }
}
