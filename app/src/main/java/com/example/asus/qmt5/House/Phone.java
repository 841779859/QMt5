package com.example.asus.qmt5.House;

/**
 * Created by Administrator on 2018/1/6.
 */

public class Phone {
    private String name;

    private int imageId;

    public Phone(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
