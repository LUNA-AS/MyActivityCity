package com.example.myactivitycity.Models;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.example.myactivitycity.R;

import java.util.ArrayList;
import java.util.Random;

import io.realm.Realm;

public class Reward {
    Context context;
    String name, description;
    Drawable drawable;
    float x, y;
    private ArrayList<Integer> images;

    public Reward(String name, String description, float x, float y) {
        context = Realm.getApplicationContext();
        this.name = name;
        this.description = description;
        this.x = x;
        this.y = y;
    }

    public Reward() {
        context = Realm.getApplicationContext();
        images = new ArrayList<>();
        images.add(R.drawable.ic_baseline_location_city_24);
        images.add(R.drawable.ic_baseline_dashboard_24);
        images.add(R.drawable.ic_baseline_lightbulb_24);
        Random random = new Random();
        int imgIndex = random.nextInt(images.size() - 1);
        context.getResources().getDrawable(images.get(imgIndex));
        this.x = 200;
        this.y = 200;
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

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}

