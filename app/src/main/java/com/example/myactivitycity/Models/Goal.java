package com.example.myactivitycity.Models;


import android.graphics.Color;

import java.text.DecimalFormat;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class Goal extends RealmObject {
    String name, description;
    int totalTasks, completedTasks;

    public Goal(String name) {
        this.name = name;
        this.completedTasks = 0;
        this.totalTasks = 0;
    }
    public Goal() {
        name = "default";
        description = "";
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompletionPercentage(){
        String result = "0 % Completed";
        float totalTasks = 0;
        float completedTasks = 0;
        float percentage = 0;
        DecimalFormat decimalFormat = new DecimalFormat("###.#");

        Realm.init(Realm.getApplicationContext());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TodoTask> tasks = realm.where(TodoTask.class).equalTo("goal", name).findAll();
        totalTasks = tasks.size();
        for (TodoTask task : tasks){
            if(task.isComplete()){
                completedTasks ++;
            }
        }
        if(totalTasks != 0){
            percentage = completedTasks/totalTasks;
            percentage *= 100;
            result = decimalFormat.format(percentage)+ " % Completed";
        }

        return result;
    }

    public ArrayList<TodoTask> getTasks(){
        ArrayList<TodoTask> tasks = new ArrayList<>();
        Realm.init(Realm.getApplicationContext());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TodoTask> realmResults = realm.where(TodoTask.class).equalTo("goal", name).findAll();
        tasks.addAll(realmResults);

        return  tasks;
    }
    public float getCompletionPercentageFloat(){
        float totalTasks = 0;
        float completedTasks = 0;
        float percentage = 0;

        Realm.init(Realm.getApplicationContext());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TodoTask> tasks = realm.where(TodoTask.class).equalTo("goal", name).findAll();
        totalTasks = tasks.size();
        for (TodoTask task : tasks){
            if(task.isComplete()){
                completedTasks ++;
            }
        }
        if(totalTasks != 0){
            percentage = completedTasks/totalTasks;
            percentage *= 100;
        }
        return percentage;
    }
}
