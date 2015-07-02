package com.kentchiu.spring.base.domain;

public class Car {

    private String color;
    private String[] colors;

    @Option(value = {"white", "black", "red"})
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Options(value = {"white", "black", "red"})
    public String[] getColors() {
        return colors;
    }

    public void setColors(String[] colors) {
        this.colors = colors;
    }
}