package com.niu.rabbitmq.spring;

import com.niu.rabbitmq.spring.adapter.MessageDelegate;
import com.niu.rabbitmq.spring.convert.ImageMessageConverter;
import com.niu.rabbitmq.spring.convert.PdfMessageConverter;
import com.niu.rabbitmq.spring.convert.TextMessageConvert;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.UUID;

/**
 * rabbitmq config
 *
 * @author [nza]
 * @version 1.0 [2020/07/06 16:58]
 * @createTime [2020/07/06 16:58]
 */
@Configuration
@ComponentScan({"com.niu.rabbitmq.*"})
public class RabbitmqConfig {

    /**
     * 注入连接工厂
     *
     * @return {@link org.springframework.amqp.rabbit.connection.ConnectionFactory}
     * @author nza
     * @createTime 2020/7/6 17:04
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setAddresses("106.15.121.10:5672");
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");

        return factory;
    }

    /**
     * 注入 rabbitAdmin
     *
     * @param connectionFactory 连接工厂
     * @return {@link org.springframework.amqp.rabbit.core.RabbitAdmin}
     * @author nza
     * @createTime 2020/7/6 17:04
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 必须设置为true， 否则容器不会加载rabbitAdmin
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    /**
     * 声明交换机
     *
     * @return TopicExchange
     */
    @Bean
    public TopicExchange exchange001() {
        return new TopicExchange("topic001", true, false);
    }

    /**
     * 声明队列
     *
     * @return Queue
     */
    @Bean
    public Queue queue001() {
        return new Queue("queue001", true);
    }

    @Bean
    public Queue queue002() {
        return new Queue("queue002", true);
    }

    @Bean
    public Queue queue003() {
        return new Queue("queue003", true);
    }

    @Bean
    public Queue queue004() {
        return new Queue("queue004", true);
    }

    /**
     * 绑定关系
     *
     * @return Binding
     */
    @Bean
    public Binding binding001() {
        return BindingBuilder.bind(queue001()).to(exchange001()).with("spring.mq.one.*");
    }

    @Bean
    public Binding binding002() {
        return BindingBuilder.bind(queue002()).to(exchange001()).with("spring.mq.two.*");
    }

    @Bean
    public Binding binding003() {
        return BindingBuilder.bind(queue003()).to(exchange001()).with("spring.mq.three.*");
    }

    @Bean
    public Binding binding004() {
        return BindingBuilder.bind(queue004()).to(exchange001()).with("spring.mq.four.*");
    }

    @Bean
    public Queue queueImage() {
        return new Queue("queue_image", true);
    }

    @Bean
    public Queue queuePdf() {
        return new Queue("queue_pdf", true);
    }

    /**
     * 注入RabbitTemplate
     *
     * @param connectionFactory 连接工厂
     * @return RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

    /**
     * 注入 SimpleMessageListenerContainer
     *
     * @return SimpleMessageListenerContainer
     */
    @Bean
    public SimpleMessageListenerContainer messageListenerContainer1(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);

        // 监听队列
        container.setQueues(queue001());

        // 设置消费者数量
        container.setConcurrentConsumers(1);
        // 设置最大消费者数量
        container.setMaxConcurrentConsumers(5);
        // 设置失败消息是否重回队列
        container.setDefaultRequeueRejected(false);
        // 设置签收模式
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        // 设置消费者标签策略
        container.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String queue) {
                return queue + "_" + UUID.randomUUID().toString();
            }
        });
        // 设置消息监听
        container.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                String msg = new String(message.getBody());
                System.out.println("message listener1: " + msg);
            }
        });
        return container;
    }

    /**
     * 注入 SimpleMessageListenerContainer
     *
     * @return SimpleMessageListenerContainer
     */
    @Bean
    public SimpleMessageListenerContainer messageListenerContainer2(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);

        // 监听队列
        container.setQueues(queue002(), queueImage(), queuePdf());

        // 设置消费者数量
        container.setConcurrentConsumers(1);
        // 设置最大消费者数量
        container.setMaxConcurrentConsumers(5);
        // 设置失败消息是否重回队列
        container.setDefaultRequeueRejected(false);
        // 设置签收模式
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        // 设置消费者标签策略
        container.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String queue) {
                return queue + "_" + UUID.randomUUID().toString();
            }
        });

//        // 设置消息适配器
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        // 适配方式1 自定义方法名
//        adapter.setDefaultListenerMethod("handleMessage3");
//        adapter.setMessageConverter(new TextMessageConvert());
//        container.setMessageListener(adapter);

        // 1. 设置json格式的转换器
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
//        adapter.setMessageConverter(jackson2JsonMessageConverter);
//        container.setMessageListener(adapter);

        // 2. 设置json格式的转换器并支持java类型的转换
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
//        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
//        jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
//        adapter.setMessageConverter(jackson2JsonMessageConverter);
//        container.setMessageListener(adapter);

        // 3. 设置json格式的转换器并支持java对象多映射转换
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
//        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
//
//        Map<String, Class<?>> idClassMapping = new HashMap<>();
//        idClassMapping.put("order", Order.class);
//        idClassMapping.put("pack", Pack.class);
//
//        javaTypeMapper.setIdClassMapping(idClassMapping);
//        jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
//        adapter.setMessageConverter(jackson2JsonMessageConverter);
//        container.setMessageListener(adapter);

        // 4. 多类型转换
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");
        ContentTypeDelegatingMessageConverter converter = new ContentTypeDelegatingMessageConverter();

        TextMessageConvert textMessageConvert = new TextMessageConvert();
        converter.addDelegate("text", textMessageConvert);
        converter.addDelegate("html/text", textMessageConvert);
        converter.addDelegate("xml/text", textMessageConvert);
        converter.addDelegate("text/plain", textMessageConvert);

        Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter();
        converter.addDelegate("json", jsonMessageConverter);
        converter.addDelegate("application/json", jsonMessageConverter);

        ImageMessageConverter imageMessageConverter = new ImageMessageConverter();
        converter.addDelegate("image/png", imageMessageConverter);
        converter.addDelegate("image", imageMessageConverter);

        PdfMessageConverter pdfMessageConverter = new PdfMessageConverter();
        converter.addDelegate("application/pdf", pdfMessageConverter);

        adapter.setMessageConverter(converter);
        container.setMessageListener(adapter);
        return container;
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer3(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);

        // 监听队列
        container.setQueues(queue003(), queue004());

        // 设置消费者数量
        container.setConcurrentConsumers(1);
        // 设置最大消费者数量
        container.setMaxConcurrentConsumers(5);
        // 设置失败消息是否重回队列
        container.setDefaultRequeueRejected(false);
        // 设置签收模式
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        // 设置消费者标签策略
        container.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String queue) {
                return queue + "_" + UUID.randomUUID().toString();
            }
        });

        // 设置消息适配器
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        // 适配方式2 队列名称和方法名称进行一一匹配
        HashMap<String, String> queueAndMethod = new HashMap<>(2);
        queueAndMethod.put("queue003", "handleMessage4");
        queueAndMethod.put("queue004", "handleMessage5");
        adapter.setQueueOrTagToMethodName(queueAndMethod);
        container.setMessageListener(adapter);

        return container;
    }
}
