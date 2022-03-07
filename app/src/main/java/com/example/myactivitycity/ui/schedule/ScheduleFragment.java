package com.example.myactivitycity.ui.schedule;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myactivitycity.Models.TodoTask;
import com.example.myactivitycity.R;
import com.example.myactivitycity.adapters.ScheduleTasksAdapter;
import com.example.myactivitycity.databinding.FragmentScheduleBinding;
import com.example.myactivitycity.ui.activities.NewTaskActivity;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ScheduleFragment extends Fragment {

    private FragmentScheduleBinding binding;
    CompactCalendarView compactCalendar;
    SimpleDateFormat dateFormat;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ScheduleViewModel scheduleViewModel =
                new ViewModelProvider(this).get(ScheduleViewModel.class);

        binding = FragmentScheduleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dateFormat = new SimpleDateFormat("d MMM yyyy");
        TextView dateTextView = root.findViewById(R.id.calendarDateTextView);
        ImageButton nextMonth = root.findViewById(R.id.nextMonth);
        ImageButton previousMonth = root.findViewById(R.id.previousMonth);
        compactCalendar = (CompactCalendarView) root.findViewById(R.id.scheduleCalendarView);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        dateTextView.setText(dateFormat.format(System.currentTimeMillis()));

        // Set up calendar controls

        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendar.scrollRight();
            }
        });
        previousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendar.scrollLeft();
            }
        });

        // Load events
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TodoTask> tasks = realm.where(TodoTask.class).findAll();
        ArrayList<TodoTask> scheduledTasks = new ArrayList<>();
        ArrayList<TodoTask> datesTasks = new ArrayList<>();
        ArrayList<Event> events = new ArrayList<>();
        for (TodoTask task : tasks) {
            if (!task.getDeadline().equals("")) {
                scheduledTasks.add(task);
                try {
                    Date date = dateFormat.parse(task.getDeadline());
                    long mills = date.getTime();
                    events.add(new Event(Color.RED, mills, task.getTitle()));
                    System.out.println("added event: " + task.getTitle());
                } catch (ParseException e) {
                    e.printStackTrace();
                    System.out.println("failed to add task: " + task.getTitle() + " (Scheduled)");
                }
            }
            if (!task.getScheduledDate().equals("")) {
                scheduledTasks.add(task);
                try {
                    Date date = dateFormat.parse(task.getScheduledDate());
                    long mills = date.getTime();
                    events.add(new Event(Color.RED, mills, task.getTitle() + " (Deadline)"));
                    System.out.println("added event: " + task.getTitle());
                } catch (ParseException e) {
                    e.printStackTrace();
                    System.out.println("failed to add task: " + task.getTitle());
                }
            }
        }
        compactCalendar.addEvents(events);

        // View events for selected date
        datesTasks.clear();
        for (TodoTask task : scheduledTasks) {
            if (task.getScheduledDate().equals(dateTextView.getText().toString())) {
                datesTasks.add(task);
            }
            if (task.getDeadline().equals(dateTextView.getText().toString())) {
                datesTasks.add(task);
            }
        }
        ScheduleTasksAdapter adapter = new ScheduleTasksAdapter(datesTasks);
        RecyclerView tasksRecyclerView = root.findViewById(R.id.scheduleRecycler);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tasksRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // Change tasks displayed on changing selection

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                dateTextView.setText(dateFormat.format(dateClicked));
                datesTasks.clear();
                for (TodoTask task : scheduledTasks) {
                    if (task.getScheduledDate().equals(dateTextView.getText().toString())) {
                        datesTasks.add(task);
                    }
                    if (task.getDeadline().equals(dateTextView.getText().toString())) {
                        datesTasks.add(task);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                dateTextView.setText(dateFormat.format(firstDayOfNewMonth));
                datesTasks.clear();
                for (TodoTask task : scheduledTasks) {
                    if (task.getScheduledDate().equals(dateTextView.getText().toString())) {
                        datesTasks.add(task);
                    }
                    if (task.getDeadline().equals(dateTextView.getText().toString())) {
                        datesTasks.add(task);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        tasks.addChangeListener(new RealmChangeListener<RealmResults<TodoTask>>() {
            @Override
            public void onChange(RealmResults<TodoTask> todoTasks) {
                scheduledTasks.clear();
                events.clear();
                for (TodoTask task : tasks) {
                    if (!task.getDeadline().equals("")) {
                        scheduledTasks.add(task);
                        try {
                            Date date = dateFormat.parse(task.getDeadline());
                            long mills = date.getTime();
                            events.add(new Event(Color.RED, mills, task.getTitle()));
                            System.out.println("added event: " + task.getTitle());
                        } catch (ParseException e) {
                            e.printStackTrace();
                            System.out.println("failed to add task: " + task.getTitle() + " (Scheduled)");
                        }
                    }
                    if (!task.getScheduledDate().equals("")) {
                        scheduledTasks.add(task);
                        try {
                            Date date = dateFormat.parse(task.getScheduledDate());
                            long mills = date.getTime();
                            events.add(new Event(Color.RED, mills, task.getTitle() + " (Deadline)"));
                            System.out.println("added event: " + task.getTitle());
                        } catch (ParseException e) {
                            e.printStackTrace();
                            System.out.println("failed to add task: " + task.getTitle());
                        }
                    }
                }
                compactCalendar.addEvents(events);
                adapter.notifyDataSetChanged();
            }
        });

        FloatingActionButton addTask = root.findViewById(R.id.scheduleFloatingButton);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewTaskActivity.class);
                intent.putExtra("Date", dateTextView.getText().toString());
                startActivity(intent);
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