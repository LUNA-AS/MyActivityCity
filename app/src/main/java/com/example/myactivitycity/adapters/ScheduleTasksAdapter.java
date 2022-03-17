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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myactivitycity.Models.Reward;
import com.example.myactivitycity.Models.TodoTask;
import com.example.myactivitycity.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import io.realm.Realm;
import io.realm.RealmResults;

public class ScheduleTasksAdapter extends RecyclerView.Adapter<ScheduleTasksAdapter.ViewHolder> {
    RealmResults<TodoTask> tasks;
    String currentDate;
    Context context;

    public ScheduleTasksAdapter(Context context, RealmResults<TodoTask> tasks, String currentDate) {
        this.tasks = tasks;
        this.currentDate = currentDate;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            holder.descriptionOutput.setText(getFilteredList().get(position).getDescription());
            holder.titleOutput.setText(getFilteredList().get(position).getTitle());
            if (!getFilteredList().get(position).getDeadline().equals("")) {
                holder.timeOutput.setText("Deadline: " + getFilteredList().get(position).getDeadline());
            } else if (!getFilteredList().get(position).getScheduledDate().equals("")) {
                holder.timeOutput.setText("Scheduled: " + getFilteredList().get(position).getScheduledDate());
            } else {
                String formattedTime = DateFormat.getDateTimeInstance().format(getFilteredList().get(position).getTimeCreated());
                holder.timeOutput.setText("Created on: " + formattedTime);
            }
            if (getFilteredList().get(position).isActive()) {
                if (getFilteredList().get(position).isComplete()) {
                    holder.checkBox.setChecked(true);
                    holder.card.setBackgroundResource(R.drawable.round_corner_card_filled_in);
                    holder.collectReward.setVisibility(View.VISIBLE);
                    holder.collectReward.setActivated(true);
                } else {
                    holder.checkBox.setChecked(false);
                    holder.card.setBackgroundResource(R.drawable.round_corner_card);
                    holder.collectReward.setVisibility(View.INVISIBLE);
                    holder.collectReward.setActivated(false);
                }
            } else {
                holder.card.setBackgroundResource(R.drawable.inactive_task_card);
                holder.collectReward.setVisibility(View.INVISIBLE);
                holder.collectReward.setActivated(false);
            }

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.checkBox.isChecked()) {
                        if (!getFilteredList().get(position).isComplete()) {
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            getFilteredList().get(position).setComplete(true);
                            realm.commitTransaction();
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

            holder.checkBox.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    builder.setMessage("Are you sure you want to delete this task? ")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    int index = -1;
                                    for (int i = 0; i < tasks.size(); i++) {
                                        if (tasks.get(i).equals(getFilteredList().get(position))) {
                                            index = i;
                                        }
                                    }
                                    if (index > -1) {
                                        Realm.init(Realm.getApplicationContext());
                                        Realm realm = Realm.getDefaultInstance();
                                        realm.beginTransaction();
                                        if (tasks.get(index) != null) {
                                            tasks.get(index).deleteFromRealm();
                                            System.out.println("delete called");
                                        }
                                        realm.commitTransaction();
                                        notifyDataSetChanged();
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Delete Task");
                    alert.show();
                }
            });


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
        } catch (Exception e) {
            holder.checkBox.setVisibility(View.INVISIBLE);
            holder.deleteButton.setVisibility(View.INVISIBLE);
            holder.collectReward.setVisibility(View.INVISIBLE);
            holder.descriptionOutput.setText("No tasks scheduled for the selected date");
            holder.titleOutput.setText("");
            holder.timeOutput.setText("");
            holder.card.setBackgroundResource(R.drawable.rounded_corners);

        }

    }

    @Override
    public int getItemCount() {
        int count = getFilteredList().size();
        if (count < 1) {
            count = 1;
        }
        return count;
    }

    private ArrayList<TodoTask> getFilteredList() {
        ArrayList<TodoTask> tasksList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy");

        for (TodoTask task : tasks) {
            if (task.isActive()) {
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
        }
        System.out.println(currentDate);
        System.out.println(tasksList.size() + " tasks found");
        return tasksList;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleOutput;
        TextView descriptionOutput;
        TextView timeOutput;
        CheckBox checkBox;
        FrameLayout card;
        ImageButton deleteButton;
        ImageButton collectReward;

        public ViewHolder(@NonNull View itemView) {
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
}
