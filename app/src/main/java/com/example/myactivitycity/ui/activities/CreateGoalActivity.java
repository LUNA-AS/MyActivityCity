package com.example.myactivitycity.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myactivitycity.Models.Goal;
import com.example.myactivitycity.R;

import io.realm.Realm;

public class CreateGoalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);
        Button saveGoal = findViewById(R.id.saveGoal);
        EditText goalTitle = findViewById(R.id.goalTitleInput);
        EditText goalDescription = findViewById(R.id.goalDescriptionInput);

        saveGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!goalTitle.getText().toString().equals("")) {
                    String name = goalTitle.getText().toString();
                    Realm.init(getApplicationContext());
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    Goal goal = realm.createObject(Goal.class);
                    goal.setName(name);
                    if (!goalDescription.getText().toString().equals("")) {
                        String description = goalDescription.getText().toString();
                        goal.setDescription(description);
                    }
                    realm.commitTransaction();
                    Toast.makeText(CreateGoalActivity.this, "Added a new goal", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    goalTitle.requestFocus();
                    goalTitle.setError("Goal name cannot be empty");
                }
            }
        });
    }
}