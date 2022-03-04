package com.example.myactivitycity.ui.goals;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myactivitycity.adapters.GoalsAdapter;
import com.example.myactivitycity.Models.Goal;
import com.example.myactivitycity.Models.Task;
import com.example.myactivitycity.Models.TodoTask;
import com.example.myactivitycity.R;
import com.example.myactivitycity.databinding.FragmentGoalsBinding;
import com.example.myactivitycity.ui.activities.CreateGoalActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class GoalsFragment extends Fragment {

    RealmResults<Goal> goals;
    HashMap<String, RealmResults<TodoTask>> goalContents;
    ArrayList<HashMap<String, RealmResults<TodoTask>>> hashMapArrayList;
    RealmResults<TodoTask> tasks;
    GoalsAdapter goalsAdapter;
    private FragmentGoalsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GoalsViewModel goalsViewModel =
                new ViewModelProvider(this).get(GoalsViewModel.class);

        binding = FragmentGoalsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // initializing lists
        goalContents = new HashMap<>();
        hashMapArrayList = new ArrayList<>();

        // Getting goals saved in database
        Realm.init(getContext());
        Realm realm = Realm.getDefaultInstance();
        goals = realm.where(Goal.class).findAll();
        ArrayList<Task> unclassified = new ArrayList<>();
        ArrayList<Goal> goalsArrayList = new ArrayList<>();

        tasks = realm.where(TodoTask.class).equalTo("goal", "default").findAll();
        if (tasks.size() > 0) {
            goalsArrayList.add(new Goal("default"));
            goalContents.put("default", tasks);
            hashMapArrayList.add(goalContents);
        }
        if (goals.size() > 0) {
            for (Goal goal : goals) {
                Realm.init(getContext());
                tasks = realm.where(TodoTask.class).equalTo("goal", goal.getName()).findAll();
                if (tasks == null) {
                    System.out.println("null tasks list");
                }
                goalContents.put(goal.getName(), tasks);
                hashMapArrayList.add(goalContents);
                goalsArrayList.addAll(goals);
            }
            goalsAdapter = new GoalsAdapter(getContext(), goalsArrayList, hashMapArrayList);
        } else {
            Goal goal = new Goal();
            Realm.init(getContext());
            RealmResults<TodoTask> tasks = realm.where(TodoTask.class).findAll();
            System.out.println("Found " + tasks.size() + " tasks under default");
            goalContents.put(goal.getName(), tasks);
            System.out.println("Put to hash: " + goal + " " + tasks);
            realm.close();
            ArrayList<Goal> defaultGoalList = new ArrayList<>();
            defaultGoalList.add(goal);
            hashMapArrayList.add(goalContents);
            goalsAdapter = new GoalsAdapter(getContext(), defaultGoalList, hashMapArrayList);
            goalsAdapter.notifyDataSetChanged();
        }
        ExpandableListView expandableListView = root.findViewById(R.id.goalsExpandableList);

        expandableListView.setAdapter(goalsAdapter);
        goalsAdapter.notifyDataSetChanged();

        goals.addChangeListener(new RealmChangeListener<RealmResults<Goal>>() {
            @Override
            public void onChange(RealmResults<Goal> goals) {
                goalsArrayList.clear();
                hashMapArrayList.clear();

                goalsArrayList.add(new Goal("default"));
                tasks = realm.where(TodoTask.class).equalTo("goal", "default").findAll();
                goalContents.put("default", tasks);
                hashMapArrayList.add(goalContents);

                if (goals.size() > 0) {
                    for (Goal goal : goals) {
                        Realm.init(getContext());
                        tasks = realm.where(TodoTask.class).equalTo("goal", goal.getName()).findAll();
                        if (tasks == null) {
                            System.out.println("null tasks list");
                        }
                        goalContents.put(goal.getName(), tasks);
                        hashMapArrayList.add(goalContents);
                        goalsArrayList.addAll(goals);
                    }
                    goalsAdapter = new GoalsAdapter(getContext(), goalsArrayList, hashMapArrayList);
                } else {
                    Goal goal = new Goal();
                    Realm.init(getContext());
                    RealmResults<TodoTask> tasks = realm.where(TodoTask.class).findAll();
                    System.out.println("Found " + tasks.size() + " tasks under default");
                    goalContents.put(goal.getName(), tasks);
                    System.out.println("Put to hash: " + goal + " " + tasks);
                    realm.close();
                    ArrayList<Goal> defaultGoalList = new ArrayList<>();
                    defaultGoalList.add(goal);
                    hashMapArrayList.add(goalContents);
                }
                goalsAdapter.notifyDataSetChanged();
            }
        });
        FloatingActionButton addGoalButton = root.findViewById(R.id.addGoalFloatingButton);
        addGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CreateGoalActivity.class));
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}