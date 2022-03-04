package com.example.myactivitycity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myactivitycity.Models.Goal;
import com.example.myactivitycity.R;

import java.util.ArrayList;

public class GoalsSpinnerAdapter extends ArrayAdapter<Goal> {
    LayoutInflater layoutInflater;
    ArrayList<Goal> goals;

    public GoalsSpinnerAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Goal> goals) {
        super(context, resource, goals);
        this.goals = goals;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.goal_spinner_item, null, true);
        TextView goalName = view.findViewById(R.id.spinnerGoalName);
        goalName.setText(goals.get(position).getName());
        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.goal_spinner_item, parent, false);
        }
        TextView goalName = convertView.findViewById(R.id.spinnerGoalName);
        goalName.setText(goals.get(position).getName());
        return convertView;
    }
}
