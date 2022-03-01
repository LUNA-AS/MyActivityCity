package com.example.myactivitycity.Models;

import io.realm.RealmObject;

public class Goal extends RealmObject {
    String name;
    int totalTasks, completedTasks;

    public Goal(String name) {
        this.name = name;
        this.completedTasks = 0;
        this.totalTasks = 0;
    }
    public Goal() {
        name = "default";
        this.completedTasks = 0;
        this.totalTasks = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public int getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(int completedTasks) {
        this.completedTasks = completedTasks;
    }
}
