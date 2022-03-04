package com.example.myactivitycity.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myactivitycity.Models.TodoTask;
import com.example.myactivitycity.R;

import io.realm.Realm;

public class NewTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        // Get UI Elements
        EditText titleInput = findViewById(R.id.titleInput);
        EditText descriptionInput = findViewById(R.id.descriptionInput);
        Button saveBtn = findViewById(R.id.saveTask);
        CheckBox dateCheckbox = findViewById(R.id.scheduleCheckbox);
        CheckBox timeCheckbox = findViewById(R.id.addTimeCheckbox);
        RadioButton deadlineRadioButton = findViewById(R.id.deadlineRadioButton);
        RadioButton sceduleRadioButton = findViewById(R.id.scheduleRadioButton);
        DatePicker datePicker = findViewById(R.id.taskDatePicker);
        TimePicker timePicker = findViewById(R.id.taskTimePicker);

        deadlineRadioButton.setEnabled(false);
        sceduleRadioButton.setEnabled(false);
        timePicker.setEnabled(false);
        datePicker.setEnabled(false);
        timeCheckbox.setEnabled(false);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);


        dateCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                datePicker.setEnabled(b);
                deadlineRadioButton.setEnabled(b);
                sceduleRadioButton.setEnabled(b);
                if(!b){
                    timeCheckbox.setChecked(false);
                }
                timeCheckbox.setEnabled(b);
            }
        });
        timeCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                timePicker.setEnabled(b);
            }
        });


        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleInput.getText().toString();
                String description = descriptionInput.getText().toString();
                Long createdTime = System.currentTimeMillis();

                if(!title.equals("title")  && !title.equals("")){
                    boolean success = true;
                    realm.beginTransaction();
                    TodoTask task = realm.createObject(TodoTask.class);
                    task.setTitle(title);
                    task.setDescription(description);
                    task.setTimeCreated(createdTime);
                    if(dateCheckbox.isChecked()){
                        if(deadlineRadioButton.isChecked()){
                            task.setDeadline(datePicker.getDayOfMonth()+"/"+datePicker.getMonth()+"/"+datePicker.getYear());
                        }else if(sceduleRadioButton.isChecked()){
                            task.setScheduledDate(datePicker.getDayOfMonth()+"/"+datePicker.getMonth()+"/"+datePicker.getYear());
                        }else{
                            deadlineRadioButton.requestFocus();
                            sceduleRadioButton.requestFocus();
                            success = false;
                        }
                    }
                    if(success){
                        realm.commitTransaction();
                        Toast.makeText(getApplicationContext(),"Added task", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        realm.cancelTransaction();
                    }

                }else{
                    titleInput.requestFocus();
                    titleInput.setError("Title cannot be empty");
                    Toast.makeText(NewTaskActivity.this,"Invalid title",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}