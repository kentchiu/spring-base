package com.kentchiu.spring.base.domain;

public class Car {


    private String color;


    @Option(value = {"white", "black", "red"})
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}