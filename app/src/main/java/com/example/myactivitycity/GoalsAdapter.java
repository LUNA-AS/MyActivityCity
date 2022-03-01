package com.example.myactivitycity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.myactivitycity.Models.Goal;
import com.example.myactivitycity.Models.TodoTask;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.RealmResults;

public class GoalsAdapter extends BaseExpandableListAdapter {

    Context context;
    ArrayList<Goal> goals;
    ArrayList<HashMap<String, RealmResults<TodoTask>>> goalsWithContents;

    public GoalsAdapter(Context context, ArrayList<Goal> goals, ArrayList<HashMap<String, RealmResults<TodoTask>>> goalsWithContents) {
        this.context = context;
        this.goals = goals;
        this.goalsWithContents = goalsWithContents;
    }

    @Override
    public int getGroupCount() {
        return goalsWithContents.size();
    }

    @Override
    public int getChildrenCount(int i) {
        int count = 0;
        try {
            count = goalsWithContents.get(i).get("default").size();
        } catch (Exception e) {
            e.printStackTrace();
            count = 0;
        }
        System.out.println("Count of children: " + goalsWithContents.get(i).get(goals.get(i).getName()));
        return count;
    }

    @Override
    public Object getGroup(int i) {
        return goalsWithContents.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return goalsWithContents.get(i).get(goals.get(i).getName()).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String goal = goals.get(i).getName();
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.goal_item, null);
        }
        TextView goalNameView = view.findViewById(R.id.parentGoal);
        goalNameView.setText(goal);

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        TodoTask task = goalsWithContents.get(i).get(goals.get(i).getName()).get(i1);
        String taskName = task.getTitle();
        System.out.println("found task with title: " + taskName);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.goal_task_item, null);
        }
        TextView taskNameView = view.findViewById(R.id.childTask);
        taskNameView.setText(taskName);
        if (task.isComplete()) {
            taskNameView.setBackgroundResource(R.drawable.round_corner_card);
        } else {
            taskNameView.setBackgroundResource(R.drawable.round_corner_card_filled_in);
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
