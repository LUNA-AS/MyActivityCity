package com.example.myactivitycity.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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
        holder.descriptionOutput.setText(tasks.get(position).getDescription());
        holder.titleOutput.setText(tasks.get(position).getTitle());

        if(!tasks.get(position).getDeadline().equals("")){
            holder.timeOutput.setText("Deadline: "+tasks.get(position).getDeadline());
        }else if (!tasks.get(position).getScheduledDate().equals("")){
            holder.timeOutput.setText("Scheduled: "+tasks.get(position).getScheduledDate());
        }else{
            String formattedTime = DateFormat.getDateTimeInstance().format(tasks.get(position).getTimeCreated());
            holder.timeOutput.setText("Created on: "+formattedTime);
        }
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
                if(holder.checkBox.isChecked()){
                    if(!tasks.get(position).isComplete()){
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        tasks.get(position).setComplete(true);
                        realm.commitTransaction();
                    }
                }else{
                    if(tasks.get(position).isComplete()){
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
                Realm.init(context);
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

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleOutput;
        TextView descriptionOutput;
        TextView timeOutput;
        CheckBox checkBox;
        FrameLayout card;
        ImageButton deleteButton;

        public MyViewHolder(@NonNull View itemView) {
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
