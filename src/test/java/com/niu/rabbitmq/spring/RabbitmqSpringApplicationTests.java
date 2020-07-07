package com.niu.rabbitmq.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitmqSpringApplicationTests {
    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

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

    @Test
    public void testSendMessage() throws Exception {

        // 1. 创建消息
        // 消息 properties
        MessageProperties properties = new MessageProperties();
        properties.getHeaders().put("desc", "信息描述");
        properties.getHeaders().put("type", "自定义消息类型");
        // 消息
        Message message = new Message("Hello Rabbitmq".getBytes(), properties);

        // 2. 发送消息
        rabbitTemplate.convertAndSend("topic001", "spring.mq.one.test1", message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                System.out.println("添加额外设置");
                message.getMessageProperties().getHeaders().put("desc1", "额外添加的信息描述");
                return message;
            }
        });
    }

    @Test
    public void testSendMessage2() throws Exception {

        // 1. 创建消息
        // 消息 properties
        MessageProperties properties = new MessageProperties();
        properties.setContentEncoding("UTF-8");
        properties.setContentType("text/plain");
        // 消息
        Message message = new Message("Hello Rabbitmq 1234".getBytes(), properties);

        // 2. 发送消息
        rabbitTemplate.convertAndSend("topic001", "spring.mq.one.test1", "Hello Object!");

        rabbitTemplate.send("topic001", "spring.mq.one.test1", message);
    }
}
