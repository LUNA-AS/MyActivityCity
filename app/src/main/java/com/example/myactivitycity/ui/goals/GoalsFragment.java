package com.example.myactivitycity.ui.goals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myactivitycity.GoalsAdapter;
import com.example.myactivitycity.Models.Goal;
import com.example.myactivitycity.Models.TodoTask;
import com.example.myactivitycity.R;
import com.example.myactivitycity.databinding.FragmentGoalsBinding;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class GoalsFragment extends Fragment {

    RealmResults<Goal> goals;
    HashMap<Goal, RealmResults<TodoTask>> goalContents;
    ArrayList<HashMap<Goal, RealmResults<TodoTask>>> hashMapArrayList;
    RealmResults<TodoTask> tasks;
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

        if (goals.size() > 0) {
            for (Goal goal : goals) {
                Realm.init(getContext());
                //realm.beginTransaction();
                tasks = realm.where(TodoTask.class).equalTo("title", goal.getName()).findAll();
                goalContents.put(goal, tasks);
                hashMapArrayList.add(goalContents);
                realm.close();
            }
        } else {
            Goal goal = new Goal();
            Realm.init(getContext());
            //realm.beginTransaction();
            RealmResults<TodoTask> tasks = realm.where(TodoTask.class).findAll();
            //realm.commitTransaction();
            goalContents.put(goal, tasks);
            realm.close();
        }
        ExpandableListView expandableListView = root.findViewById(R.id.goalsExpandableList);
        GoalsAdapter goalsAdapter = new GoalsAdapter(getContext(), goals, hashMapArrayList);
        expandableListView.setAdapter(goalsAdapter);
        goalsAdapter.notifyDataSetChanged();

        goals.addChangeListener(new RealmChangeListener<RealmResults<Goal>>() {
            @Override
            public void onChange(RealmResults<Goal> goals) {
                expandableListView.post(new Runnable() {
                    @Override
                    public void run() {
                        goalsAdapter.notifyDataSetChanged();
                    }
                });
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