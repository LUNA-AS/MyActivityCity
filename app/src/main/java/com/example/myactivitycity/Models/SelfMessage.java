package com.example.myactivitycity.Models;

import java.text.DateFormat;
import io.realm.RealmObject;

public class SelfMessage extends RealmObject {
    String body;
    Long timeCreated;

    public SelfMessage(String body) {
        this.body = body;
        this.timeCreated = System.currentTimeMillis();
    }

    public String getBody(){
        return body;
    }

    public String getTimeCreated(){
        return DateFormat.getDateTimeInstance().format(timeCreated);
    }
}
