package com.example.myactivitycity.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.myactivitycity.Models.Goal;
import com.example.myactivitycity.Models.TodoTask;
import com.example.myactivitycity.R;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.RealmResults;

public class GoalsAdapter extends BaseExpandableListAdapter {

    Context context;
    RealmResults<Goal> goals;
    ArrayList<HashMap<String, RealmResults<TodoTask>>> goalsWithContents;

    public GoalsAdapter(Context context, RealmResults<Goal> goals, ArrayList<HashMap<String, RealmResults<TodoTask>>> goalsWithContents) {
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
        int count = 1;
        /*try {
            count = goalsWithContents.get(i).get(goals.get(i)).size();
        } catch (Exception e) {
            count = 1;
            e.printStackTrace();
            System.out.println("Empty tasks list for goal: " + goals.get(i).getName());
        }*/

        count = goals.get(i).getTasks().size();
        if (count < 1) {
            count = 1;
        }

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
            TextView completionText = view.findViewById(R.id.completionText);

            goalNameView.setText(goal);
            completionText.setText(goals.get(i).getCompletionPercentage());
            if (goals.get(i).getCompletionPercentageFloat() > 50) {
                view.setBackgroundResource(R.color.darker_green);
            }

        } catch (Exception e) {
            view = new View(context);
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        try {
            TodoTask task = goals.get(i).getTasks().get(i1);
            String taskName = task.getTitle();
            System.out.println("found task with title: " + taskName);
            if (view == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.goal_task_item, null);
            }
            TextView taskNameView = view.findViewById(R.id.childTask);
            FrameLayout holder = view.findViewById(R.id.childHolder);
            taskNameView.setText(taskName);
            if(task.isActive()){
                if (!task.isComplete()) {
                    holder.setBackgroundResource(R.drawable.round_corner_card);
                } else {
                    holder.setBackgroundResource(R.drawable.round_corner_card_filled_in);
                }
            }else{
                holder.setBackgroundResource(R.drawable.inactive_task_card);
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
