package com.jing.blogs.domain;

public class UserBasic {

    private String name;

    private String picUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Override
    public String toString() {
        return "UserBasic{" +
                "name='" + name + '\'' +
                ", picUrl='" + picUrl + '\'' +
                '}';
    }
}
