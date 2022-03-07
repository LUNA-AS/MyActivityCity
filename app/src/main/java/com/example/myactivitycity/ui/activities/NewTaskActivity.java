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
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myactivitycity.Models.Goal;
import com.example.myactivitycity.Models.TodoTask;
import com.example.myactivitycity.R;
import com.example.myactivitycity.adapters.GoalsSpinnerAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class NewTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy");

        // Get UI Elements
        EditText titleInput = findViewById(R.id.goalTitleInput);
        EditText descriptionInput = findViewById(R.id.goalDescriptionInput);
        Button saveBtn = findViewById(R.id.saveGoal);
        CheckBox dateCheckbox = findViewById(R.id.scheduleCheckbox);
        CheckBox timeCheckbox = findViewById(R.id.addTimeCheckbox);
        RadioButton deadlineRadioButton = findViewById(R.id.deadlineRadioButton);
        RadioButton scheduleRadioButton = findViewById(R.id.scheduleRadioButton);
        DatePicker datePicker = findViewById(R.id.taskDatePicker);
        TimePicker timePicker = findViewById(R.id.taskTimePicker);

        if (getIntent().getExtras() != null) {
            dateCheckbox.setChecked(true);
            deadlineRadioButton.setEnabled(true);
            scheduleRadioButton.setEnabled(true);
            scheduleRadioButton.setChecked(true);
            SimpleDateFormat numeralDateFormat = new SimpleDateFormat("d MMM yyyy");
            try {
                Date date = numeralDateFormat.parse(getIntent().getExtras().get("Date").toString());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                datePicker.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            } catch (ParseException e) {
                e.printStackTrace();
            }


        } else {
            deadlineRadioButton.setEnabled(false);
            scheduleRadioButton.setEnabled(false);
            timePicker.setEnabled(false);
            datePicker.setEnabled(false);
            timeCheckbox.setEnabled(false);
        }
        datePicker.setMinDate(System.currentTimeMillis() - 1000);

        // Fill in spinner
        ArrayList<Goal> goals = new ArrayList<>();

        Realm.init(this);
        Realm realm1 = Realm.getDefaultInstance();

        RealmResults<Goal> realmGoals = realm1.where(Goal.class).findAll();
        for (Goal g : realmGoals) {
            goals.add(g);
        }
        if (goals.size() < 1) {
            goals.add(new Goal("default"));
        }
        Spinner goalsSpinner = (Spinner) findViewById(R.id.goalsSpinner);
        GoalsSpinnerAdapter spinnerAdapter = new GoalsSpinnerAdapter(this, R.layout.goal_spinner_item, goals);
        goalsSpinner.setAdapter(spinnerAdapter);


        dateCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                datePicker.setEnabled(b);
                deadlineRadioButton.setEnabled(b);
                scheduleRadioButton.setEnabled(b);
                if (!b) {
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

                if (!title.equals("title") && !title.equals("")) {
                    boolean success = true;
                    realm.beginTransaction();
                    TodoTask task = realm.createObject(TodoTask.class);
                    task.setTitle(title);
                    task.setDescription(description);
                    task.setTimeCreated(createdTime);
                    Goal selectedGoal = (Goal) goalsSpinner.getSelectedItem();
                    task.setGoal(selectedGoal.getName());
                    if (dateCheckbox.isChecked()) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                        long mills = calendar.getTimeInMillis();
                        String formattedDate = sdf.format(mills);
                        if (deadlineRadioButton.isChecked()) {
                            task.setDeadline(formattedDate);
                        } else if (scheduleRadioButton.isChecked()) {
                            task.setScheduledDate(formattedDate);
                        } else {
                            deadlineRadioButton.requestFocus();
                            scheduleRadioButton.requestFocus();
                            success = false;
                        }
                    }
                    if (success) {
                        realm.commitTransaction();
                        Toast.makeText(getApplicationContext(), "Added task", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        realm.cancelTransaction();
                    }

                } else {
                    titleInput.requestFocus();
                    titleInput.setError("Title cannot be empty");
                    Toast.makeText(NewTaskActivity.this, "Invalid title", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}