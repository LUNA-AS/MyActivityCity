package com.example.myactivitycity.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myactivitycity.Models.TodoTask;
import com.example.myactivitycity.adapters.TasksAdapter;
import com.example.myactivitycity.R;
import com.example.myactivitycity.databinding.FragmentHomeBinding;
import com.example.myactivitycity.ui.activities.NewTaskActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Realm.init(getContext());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TodoTask> tasks = realm.where(TodoTask.class).findAll();
        ArrayList<TodoTask> tasksList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy");

        String currentDate = sdf.format(System.currentTimeMillis());
        for (TodoTask task : tasks) {
            if (task.getDeadline().equals(currentDate)) {
                tasksList.add(task);
            } else if (task.getScheduledDate().equals(currentDate)) {
                tasksList.add(task);
            } else {
                if (task.getScheduledDate().equals("") && task.getDeadline().equals("")) {
                    tasksList.add(task);
                }
            }
        }


        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        TasksAdapter tasksAdapter = new TasksAdapter(getContext(), tasksList);
        recyclerView.setAdapter(tasksAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tasksAdapter.notifyDataSetChanged();

        tasks.addChangeListener(new RealmChangeListener<RealmResults<TodoTask>>() {
            @Override
            public void onChange(RealmResults<TodoTask> tasks) {
                System.out.println("List change detected");
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        tasksList.clear();
                        for (TodoTask task : tasks) {
                            if (task.getDeadline().equals(currentDate)) {
                                tasksList.add(task);
                            } else if (task.getScheduledDate().equals(currentDate)) {
                                tasksList.add(task);
                            } else {
                                if (task.getScheduledDate().equals("") && task.getDeadline().equals("")) {
                                    tasksList.add(task);
                                }
                            }
                        }
                        tasksAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        FloatingActionButton addTaskButton = root.findViewById(R.id.addTaskFloatingButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), NewTaskActivity.class));
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