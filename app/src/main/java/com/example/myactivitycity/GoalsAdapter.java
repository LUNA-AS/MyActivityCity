package com.example.myactivitycity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
        System.out.println("Count of children: " + count);
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
        try {
            String goal = goals.get(i).getName();
            if (view == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.goal_item, null);
            }
            TextView goalNameView = view.findViewById(R.id.parentGoal);
            goalNameView.setText(goal);

        } catch (Exception e) {
            /*if (view == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.goal_item, null);
            }*/
            view = new View(context);
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        try {
            TodoTask task = goalsWithContents.get(i).get(goals.get(i).getName()).get(i1);
            String taskName = task.getTitle();
            System.out.println("found task with title: " + taskName);
            if (view == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.goal_task_item, null);
            }
            TextView taskNameView = view.findViewById(R.id.childTask);
            FrameLayout holder = view.findViewById(R.id.childHolder);
            taskNameView.setText(taskName);
            if (!task.isComplete()) {
                holder.setBackgroundResource(R.drawable.round_corner_card);
            } else {
                holder.setBackgroundResource(R.drawable.round_corner_card_filled_in);
            }
        } catch (Exception e) {
            System.out.println("tasks in selected goal could not be loaded");
            if (view == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.goal_task_item, null);
            }
            TextView taskNameView = view.findViewById(R.id.childTask);
            taskNameView.setText("No tasks here yet...");
        }


        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
