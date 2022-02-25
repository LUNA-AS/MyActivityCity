package com.example.myactivitycity.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myactivitycity.Models.TodoTask;
import com.example.myactivitycity.MyAdapter;
import com.example.myactivitycity.R;
import com.example.myactivitycity.databinding.FragmentHomeBinding;

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

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        MyAdapter myAdapter = new MyAdapter(getContext(), tasks);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myAdapter.notifyDataSetChanged();

        tasks.addChangeListener(new RealmChangeListener<RealmResults<TodoTask>>() {
            @Override
            public void onChange(RealmResults<TodoTask> tasks) {
                System.out.println("List change detected");
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        myAdapter.notifyDataSetChanged();
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