package com.example.myactivitycity.Models;

import java.text.DateFormat;
import java.util.ArrayList;

public class RepeatingTask extends Task{
    private int taskFrequency;
    private ArrayList<String> datesCompleted;
    enum timeUnites {
        Hour,
        Day,
        Week,
        Month
    }
    public void complete(){
        long currentTime = System.currentTimeMillis();
        String formattedTime = DateFormat.getDateTimeInstance().format(currentTime);
        if(datesCompleted == null){
            datesCompleted = new ArrayList<>();
        }
        datesCompleted.add(formattedTime);
    }

    public int getTaskFrequency() {
        return taskFrequency;
    }

    public void setTaskFrequency(int taskFrequency) {
        this.taskFrequency = taskFrequency;
    }

    public ArrayList<String> getDatesCompleted() {
        return datesCompleted;
    }
}
