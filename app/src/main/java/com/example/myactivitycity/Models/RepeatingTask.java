package com.example.myactivitycity.Models;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;

public class RepeatingTask extends RealmObject {
    private int taskFrequency;
    private String datesCompleted;
    enum timeUnites {
        Hour,
        Day,
        Week,
        Month
    }
    String title, description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void complete(){
        long currentTime = System.currentTimeMillis();
        String formattedTime = DateFormat.getDateTimeInstance().format(currentTime);
        datesCompleted += ", "+(formattedTime);
    }

    public int getTaskFrequency() {
        return taskFrequency;
    }

    public void setTaskFrequency(int taskFrequency) {
        this.taskFrequency = taskFrequency;
    }

    public String getDatesCompleted() {
        return datesCompleted;
    }
}
