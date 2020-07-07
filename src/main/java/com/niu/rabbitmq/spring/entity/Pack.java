package com.niu.rabbitmq.spring.entity;

/**
 * 消息转换器
 *
 * @author [nza]
 * @version 1.0 [2020/07/07 11:07]
 * @createTime [2020/07/07 11:07]
 */
public class Pack {

    private String id;

    private String name;

    private String description;

    public Pack() {
    }

    public Pack(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Packaged{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
