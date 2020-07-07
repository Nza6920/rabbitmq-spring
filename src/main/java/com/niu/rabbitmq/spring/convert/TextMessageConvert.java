package com.niu.rabbitmq.spring.convert;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * 消息转换器
 *
 * @author [nza]
 * @version 1.0 [2020/07/07 11:07]
 * @createTime [2020/07/07 11:07]
 */
public class TextMessageConvert implements MessageConverter {
    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        return new Message(object.toString().getBytes(), messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        String contentType = message.getMessageProperties().getContentType();
        if (contentType != null && contentType.contains("text")) {
            return new String(message.getBody());
        }
        return message.getBody();
    }
}
