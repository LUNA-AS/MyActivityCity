package com.example.myactivitycity.adapters;

import com.example.myactivitycity.R;

public class RewardImageMapper {
    public static int getImageByName(String name) {
        int id = 0;
        switch (name) {
            case "building1":
                id = R.drawable.house1;
                break;
            case "building2":
                id = R.drawable.house2;
                break;
            case "tree1":
                id = R.drawable.tree1;
                break;
            case "building3":
                id = R.drawable.house3;
                break;
            default:
                id = R.drawable.ic_baseline_location_city_24;
        }
        return id;
    }
}
