package com.niu.rabbitmq.spring;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
        container.setQueues(queue002());

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
                System.out.println("message listener2: " + msg);
            }
        });
        return container;
    }
}
