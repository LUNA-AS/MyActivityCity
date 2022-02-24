package com.example.myactivitycity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myactivitycity.Models.TodoTask;
import java.text.DateFormat;
import io.realm.Realm;
import io.realm.RealmResults;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    RealmResults<TodoTask> tasks;
    Context context;

    public MyAdapter(Context context, RealmResults<TodoTask> tasksList) {
        tasks = tasksList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_view, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.descriptionOutput.setText(tasks.get(position).getDescription());
        holder.titleOutput.setText(tasks.get(position).getTitle());
        String formattedTime = DateFormat.getDateTimeInstance().format(tasks.get(position).getTimeCreated());
        holder.timeOutput.setText(formattedTime);
        if(tasks.get(position).isComplete()){
            holder.checkBox.setChecked(true);
            holder.card.setBackgroundResource(R.drawable.round_corner_card_filled_in);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                if (b) {
                    holder.card.setBackgroundResource(R.drawable.round_corner_card_filled_in);
                    tasks.get(position).setComplete(true);
                    realm.commitTransaction();
                } else {
                    holder.card.setBackgroundResource(R.drawable.round_corner_card);
                    tasks.get(position).setComplete(false);
                    realm.commitTransaction();
                }
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titleOutput = itemView.findViewById(R.id.titleOutput);
            descriptionOutput = itemView.findViewById(R.id.descriptionOutput);
            timeOutput = itemView.findViewById(R.id.timeOutput);
            checkBox = itemView.findViewById(R.id.taskCheckBox);
            card = itemView.findViewById(R.id.taskCard);
        }
    }
}
