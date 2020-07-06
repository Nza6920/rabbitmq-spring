package com.niu.rabbitmq.spring;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
     * @author nza
     * @createTime 2020/7/6 17:04
     * @return    {@link org.springframework.amqp.rabbit.core.RabbitAdmin}
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 必须设置为true， 否则容器不会加载rabbitAdmin
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }
}
