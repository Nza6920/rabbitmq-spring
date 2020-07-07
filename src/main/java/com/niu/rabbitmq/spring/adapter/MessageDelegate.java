package com.niu.rabbitmq.spring.adapter;

/**
 * 消息适配器
 *
 * @version 1.0 [2020/07/07 10:53]
 * @author [nza]
 * @createTime [2020/07/07 10:53]
 */
public class MessageDelegate {

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
