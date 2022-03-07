package com.example.myactivitycity.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myactivitycity.Models.TodoTask;
import com.example.myactivitycity.R;

import java.text.DateFormat;
import java.util.ArrayList;

import io.realm.Realm;

public class ScheduleTasksAdapter extends RecyclerView.Adapter<ScheduleTasksAdapter.ViewHolder> {
    ArrayList<TodoTask> tasks;

    public ScheduleTasksAdapter(ArrayList<TodoTask> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.descriptionOutput.setText(tasks.get(position).getDescription());
        holder.titleOutput.setText(tasks.get(position).getTitle());
        String formattedTime = DateFormat.getDateTimeInstance().format(tasks.get(position).getTimeCreated());
        holder.timeOutput.setText(formattedTime);
        if (tasks.get(position).isComplete()) {
            holder.checkBox.setChecked(true);
            holder.card.setBackgroundResource(R.drawable.round_corner_card_filled_in);
        } else {
            holder.checkBox.setChecked(false);
            holder.card.setBackgroundResource(R.drawable.round_corner_card);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()) {
                    if (!tasks.get(position).isComplete()) {
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        tasks.get(position).setComplete(true);
                        realm.commitTransaction();
                    }
                } else {
                    if (tasks.get(position).isComplete()) {
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        tasks.get(position).setComplete(false);
                        realm.commitTransaction();
                    }
                }
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm.init(Realm.getApplicationContext());
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                if (tasks.get(position) != null) {
                    tasks.get(position).deleteFromRealm();
                    System.out.println("delete called");
                }
                realm.commitTransaction();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleOutput;
        TextView descriptionOutput;
        TextView timeOutput;
        CheckBox checkBox;
        FrameLayout card;
        ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleOutput = itemView.findViewById(R.id.titleOutput);
            descriptionOutput = itemView.findViewById(R.id.descriptionOutput);
            timeOutput = itemView.findViewById(R.id.timeOutput);
            checkBox = itemView.findViewById(R.id.taskCheckBox);
            card = itemView.findViewById(R.id.taskCard);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
