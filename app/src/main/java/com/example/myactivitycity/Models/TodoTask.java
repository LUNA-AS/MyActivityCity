package com.example.myactivitycity.Models;

import io.realm.RealmObject;

public class TodoTask extends RealmObject {
    String title;
    String description;
    long timeCreated;
    boolean isComplete;


    public TodoTask(String title, String description, long timeCreated) {
        this.title = title;
        this.description = description;
        this.timeCreated = timeCreated;
        isComplete = false;
    }

    public TodoTask() {
        isComplete = false;
    }

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

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
