package com.example.myactivitycity.Models;

public class Building {
    String name, description;
    int image;
    float x, y;

    public Building(String name, String description, int image, float x, float y) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.x = x;
        this.y = y;
    }

    public Building() {
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
