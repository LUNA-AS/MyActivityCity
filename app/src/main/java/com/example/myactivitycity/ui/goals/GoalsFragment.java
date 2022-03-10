package com.example.myactivitycity.ui.goals;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // initializing lists
        goalContents = new HashMap<>();
        hashMapArrayList = new ArrayList<>();

        // Getting goals saved in database
        Realm.init(getContext());
        Realm realm = Realm.getDefaultInstance();
        goals = realm.where(Goal.class).findAll();
        if (goals.size() < 1) {
            realm.beginTransaction();
            Goal defaultGoal = realm.createObject(Goal.class);
            realm.commitTransaction();
        }
        goals = realm.where(Goal.class).findAll();
        for(Goal goal: goals){
            tasks = realm.where(TodoTask.class).equalTo("goal", goal.getName()).findAll();
            goalContents.put(goal.getName(),tasks);
            hashMapArrayList.add(goalContents);
        }
        goalsAdapter = new GoalsAdapter(getContext(),goals,hashMapArrayList);
        ExpandableListView expandableGoalsView = root.findViewById(R.id.goalsExpandableList);
        expandableGoalsView.setAdapter(goalsAdapter);
        goalsAdapter.notifyDataSetChanged();

        goals.addChangeListener(new RealmChangeListener<RealmResults<Goal>>() {
            @Override
            public void onChange(RealmResults<Goal> goals) {
                goalsAdapter.notifyDataSetChanged();
            }
        });
        tasks.addChangeListener(new RealmChangeListener<RealmResults<TodoTask>>() {
            @Override
            public void onChange(RealmResults<TodoTask> todoTasks) {
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