package com.example.myactivitycity.Models;


import java.util.ArrayList;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmObject;

public class Reward extends RealmObject {

    String name, description;
    int x, y;

    public Reward(String name, String description, int x, int y) {

        this.name = name;
        this.description = description;
        this.x = x;
        this.y = y;
    }

    public Reward() {
        this.name = generateRandomName();
        this.x = 0;
        this.y = 0;
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    private String generateRandomName() {
        String[] names = {"building1", "building2", "tree1", "building3", ""};
        Random random = new Random();
        int i = random.nextInt(names.length - 1);

        String name = names[i];

        return name;

    }
}

