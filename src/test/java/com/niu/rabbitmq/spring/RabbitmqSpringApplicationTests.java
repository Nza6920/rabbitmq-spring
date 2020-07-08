package com.niu.rabbitmq.spring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niu.rabbitmq.spring.entity.Order;
import com.niu.rabbitmq.spring.entity.Pack;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        // 可以自动转换对象
        rabbitTemplate.convertAndSend("topic001", "spring.mq.one.test1", "Hello Object!");
        rabbitTemplate.convertAndSend("topic001", "spring.mq.two.test1", "Hello Object2!");

        // 只能发送message
        rabbitTemplate.send("topic001", "spring.mq.one.test1", message);
    }

    /**
     * 测试json消息
     */
    @Test
    public void testJsonMessage() throws JsonProcessingException {
        Order order = new Order("666", "niu", "测试");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(order);
        System.out.println("order json: " + json);

        MessageProperties properties = new MessageProperties();
        // json消息必须设置内容类型为 application/json
        properties.setContentType("application/json");
        Message message = new Message(json.getBytes(), properties);

        rabbitTemplate.send("queue002", message);
    }

    /**
     * 测试json消息 支持实体类转换
     */
    @Test
    public void testSendJavaMessage() throws JsonProcessingException {
        Order order = new Order("111", "nza", "hhh");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(order);
        System.out.println("order json: " + json);

        MessageProperties properties = new MessageProperties();
        // json消息必须设置内容类型为 application/json
        properties.setContentType("application/json");
        properties.getHeaders().put("__TypeId__", "com.niu.rabbitmq.spring.entity.Order");
        Message message = new Message(json.getBytes(), properties);

        rabbitTemplate.send("queue002", message);
    }

    /**
     * 测试json消息 支持实体类转换
     */
    @Test
    public void testSendJavaMessage2() throws JsonProcessingException {
        Pack pack = new Pack("111", "nza", "hhh");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(pack);
        System.out.println("pack json: " + json);

        MessageProperties properties = new MessageProperties();
        // json消息必须设置内容类型为 application/json
        properties.setContentType("application/json");
        properties.getHeaders().put("__TypeId__", "com.niu.rabbitmq.spring.entity.Pack");
        Message message = new Message(json.getBytes(), properties);

        rabbitTemplate.send("queue002", message);
    }

    /**
     * 测试json消息 支持java多对象的映射
     */
    @Test
    public void testSendMappingMessage2() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // 初始化实体
        Pack pack = new Pack("222", "nza2", "aaa");
        String packJson = mapper.writeValueAsString(pack);
        System.out.println("pack json: " + packJson);

        Order order = new Order("111", "nza", "hhh");
        String orderJson = mapper.writeValueAsString(order);
        System.out.println("order json: " + orderJson);

        // 设置消息
        MessageProperties properties = new MessageProperties();
        // json消息必须设置内容类型为 application/json
        properties.setContentType("application/json");
        properties.getHeaders().put("__TypeId__", "pack");
        Message message = new Message(packJson.getBytes(), properties);

        MessageProperties properties2 = new MessageProperties();
        // json消息必须设置内容类型为 application/json
        properties2.setContentType("application/json");
        properties2.getHeaders().put("__TypeId__", "order");
        Message message2 = new Message(orderJson.getBytes(), properties2);

        // 发送消息
        rabbitTemplate.send("queue002", message);
        rabbitTemplate.send("queue002", message2);

    }

    /**
     * 测试 Convert Message
     */
    @Test
    public void testSendExtConvertMessage() throws IOException {
//        byte[] body = Files.readAllBytes(Paths.get("C:\\Users\\Administrator\\Pictures\\Saved Pictures\\my", "image_1.jpg"));
//        MessageProperties properties = new MessageProperties();
//        properties.setContentType("image/png");
//        properties.getHeaders().put("extName", "png");
//        Message message = new Message(body, properties);
//        rabbitTemplate.send("", "queue_image", message);

        byte[] body = Files.readAllBytes(Paths.get("C:\\Users\\Administrator\\Downloads\\Documents", "README.pdf"));
        MessageProperties properties = new MessageProperties();
        properties.setContentType("application/pdf");
        Message message = new Message(body, properties);
        rabbitTemplate.send("", "queue_pdf", message);
    }
}
