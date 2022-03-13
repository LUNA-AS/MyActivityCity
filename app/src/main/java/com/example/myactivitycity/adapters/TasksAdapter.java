package com.example.myactivitycity.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myactivitycity.Models.Reward;
import com.example.myactivitycity.Models.TodoTask;
import com.example.myactivitycity.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.MyViewHolder> {
    RealmResults<TodoTask> tasks;
    Context context;

    public TasksAdapter(Context context, RealmResults<TodoTask> tasksList) {
        tasks = tasksList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_view, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.descriptionOutput.setText(getFilteredList().get(position).getDescription());
        holder.titleOutput.setText(getFilteredList().get(position).getTitle());
        holder.collectReward.setActivated(false);
        holder.collectReward.setVisibility(View.INVISIBLE);

        if (!getFilteredList().get(position).getDeadline().equals("")) {
            holder.timeOutput.setText("Deadline: " + getFilteredList().get(position).getDeadline());
        } else if (!getFilteredList().get(position).getScheduledDate().equals("")) {
            holder.timeOutput.setText("Scheduled: " + getFilteredList().get(position).getScheduledDate());
        } else {
            String formattedTime = DateFormat.getDateTimeInstance().format(getFilteredList().get(position).getTimeCreated());
            holder.timeOutput.setText("Created on: " + formattedTime);
        }
        if (getFilteredList().get(position).isComplete()) {
            holder.checkBox.setChecked(true);
            holder.card.setBackgroundResource(R.drawable.round_corner_card_filled_in);
            holder.collectReward.setActivated(true);
            holder.collectReward.setVisibility(View.VISIBLE);

        } else {
            holder.checkBox.setChecked(false);
            holder.card.setBackgroundResource(R.drawable.round_corner_card);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()) {
                    if (!getFilteredList().get(position).isComplete()) {
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        TodoTask taskToUpdate = null;
                        int index = 0;
                        for (int i = 0; i < tasks.size(); i++) {
                            if (getFilteredList().get(position).equals(tasks.get(i))) {
                                taskToUpdate = tasks.get(i);
                                index = i;
                            }
                        }
                        if (taskToUpdate != null) {
                            tasks.get(index).setComplete(true);
                            realm.commitTransaction();
                        } else {
                            realm.cancelTransaction();
                            System.out.println("Could not update task: " + getFilteredList().get(position).getTitle());
                        }
                    }
                } else {
                    if (getFilteredList().get(position).isComplete()) {
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        getFilteredList().get(position).setComplete(false);
                        realm.commitTransaction();
                    }
                }
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm.init(context);
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                TodoTask taskToUpdate = null;
                int index = 0;
                for (int i = 0; i < tasks.size(); i++) {
                    if (getFilteredList().get(position).equals(tasks.get(i))) {
                        taskToUpdate = tasks.get(i);
                        index = i;
                    }
                }
                if (taskToUpdate != null) {
                    tasks.get(index).deleteFromRealm();
                    realm.commitTransaction();
                    System.out.println("Deleted task");
                } else {
                    realm.cancelTransaction();
                    System.out.println("Could not delete task: " + getFilteredList().get(position).getTitle());
                }
                notifyDataSetChanged();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        holder.collectReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setMessage("After collecting your reward you will only be able to view this " +
                        "task in Goals. Do you want to collect your reward ? ")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Realm.init(context);
                                Realm realm = Realm.getDefaultInstance();
                                realm.beginTransaction();

                                int index = 0;
                                for (int i = 0; i < tasks.size(); i++) {
                                    if (tasks.get(i).equals(getFilteredList().get(position))) {
                                        index = i;
                                    }
                                }
                                tasks.get(index).setActive(false);


                                Reward reward = realm.createObject(Reward.class);
                                reward.setDescription("Completed task: " + tasks.get(index).getTitle());

                                realm.commitTransaction();
                            }
                        })
                        .setNegativeButton("Not yet", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });

                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Collect Reward");
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return getFilteredList().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleOutput;
        TextView descriptionOutput;
        TextView timeOutput;
        CheckBox checkBox;
        FrameLayout card;
        ImageButton deleteButton;
        ImageButton collectReward;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titleOutput = itemView.findViewById(R.id.titleOutput);
            descriptionOutput = itemView.findViewById(R.id.descriptionOutput);
            timeOutput = itemView.findViewById(R.id.timeOutput);
            checkBox = itemView.findViewById(R.id.taskCheckBox);
            card = itemView.findViewById(R.id.taskCard);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            collectReward = itemView.findViewById(R.id.collectRewardButton);
        }
    }

    private ArrayList<TodoTask> getFilteredList() {
        ArrayList<TodoTask> tasksList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy");

        String currentDate = sdf.format(System.currentTimeMillis());
        for (TodoTask task : tasks) {
            if (task.isActive()) {
                if (!task.getDeadline().equals("")) {
                    tasksList.add(task);
                } else if (task.getScheduledDate().equals(currentDate)) {
                    tasksList.add(task);
                } else {
                    if (task.getScheduledDate().equals("") && task.getDeadline().equals("")) {
                        tasksList.add(task);
                    }
                }
            }
        }
        return tasksList;
    }
}
