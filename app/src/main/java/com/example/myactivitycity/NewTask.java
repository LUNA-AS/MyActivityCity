package com.example.myactivitycity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myactivitycity.Models.TodoTask;

import io.realm.Realm;

public class NewTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        EditText titleInput = findViewById(R.id.titleInput);
        EditText descriptionInput = findViewById(R.id.descriptionInput);
        Button saveBtn = findViewById(R.id.saveTask);

        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleInput.getText().toString();
                String description = descriptionInput.getText().toString();
                Long createdTime = System.currentTimeMillis();

                if(!title.equals("title")  && !title.equals("")){

                    realm.beginTransaction();
                    //Task task = new Task(title,description,createdTime);
                    TodoTask task = realm.createObject(TodoTask.class);
                    task.setTitle(title);
                    task.setDescription(description);
                    task.setTimeCreated(createdTime);
                    realm.commitTransaction();
                    Toast.makeText(getApplicationContext(),"Added task", Toast.LENGTH_SHORT).show();
                    finish();

                }else{
                    titleInput.requestFocus();
                    Toast.makeText(NewTask.this,"Invalid title",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}