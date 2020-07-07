package com.niu.rabbitmq.spring.entity;

/**
 * 消息转换器
 *
 * @author [nza]
 * @version 1.0 [2020/07/07 11:07]
 * @createTime [2020/07/07 11:07]
 */
public class Order {

    private String id;

    private String name;

    private String content;

    public Order() {
    }

    public Order(String id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
