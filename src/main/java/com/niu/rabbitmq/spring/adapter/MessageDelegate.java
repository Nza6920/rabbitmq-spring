package com.niu.rabbitmq.spring.adapter;

import com.niu.rabbitmq.spring.entity.Order;
import com.niu.rabbitmq.spring.entity.Pack;

import java.io.File;

/**
 * 消息适配器
 *
 * @version 1.0 [2020/07/07 10:53]
 * @author [nza]
 * @createTime [2020/07/07 10:53]
 */
public class MessageDelegate {

    /**
     * json convert
     * @param messageBody 消息体
     */
//    public void consumeMessage(Map messageBody) {
//        System.out.println("json convert: " + messageBody);
//    }

    /**
     * json convert 支持类型装换
     * @param order 实体对象
     */
    public void consumeMessage(Order order) {
        System.out.println("order 对象: " + order.toString());
    }

    /**
     * json convert 支持类型装换
     * @param pack 实体对象
     */
    public void consumeMessage(Pack pack) {
        System.out.println("pack 对象: " + pack.toString());
    }

    /**
     * file convert
     * @param file 消息体
     */
    public void consumeMessage(File file) {
        System.out.println("file convert: " + file.getName());
    }

    public void handleMessage(byte[] messageBody) {
        System.out.println("默认方法, 消息内容: " + new String(messageBody));
    }

    public void handleMessage2(byte[] messageBody) {
        System.out.println("默认方法2, 消息内容: " + new String(messageBody));
    }

    public void handleMessage3(String messageBody) {
        System.out.println("默认方法3, 消息内容: " + messageBody);
    }

    public void handleMessage4(byte[] messageBody) {
        System.out.println("默认方法4, 消息内容: " + new String(messageBody));
    }

    public void handleMessage5(byte[] messageBody) {
        System.out.println("默认方法5, 消息内容: " + new String(messageBody));
    }
}
