package com.example.myactivitycity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        EditText titleInput = findViewById(R.id.titleInput);
        EditText descriptionInput = findViewById(R.id.descriptionInput);
        Button saveBtn = findViewById(R.id.saveTask);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleInput.getText().toString();
                String description = descriptionInput.getText().toString();

                if(!title.equals("title")  && !title.equals("")){

                }else{
                    titleInput.requestFocus();
                    Toast.makeText(NewTask.this,"Invalid title",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}