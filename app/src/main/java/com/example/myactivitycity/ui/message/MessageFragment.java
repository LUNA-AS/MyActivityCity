package com.example.myactivitycity.ui.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myactivitycity.Models.SelfMessage;
import com.example.myactivitycity.R;
import com.example.myactivitycity.databinding.FragmentMessageBinding;

import java.util.ArrayList;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MessageFragment extends Fragment {

    private FragmentMessageBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MessageViewModel messageViewModel =
                new ViewModelProvider(this).get(MessageViewModel.class);

        binding = FragmentMessageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Handle new messages
        Button saveButton = root.findViewById(R.id.saveMessage);
        EditText messageInput = root.findViewById(R.id.messageTextInput);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageInput.getText().toString();
                if (message.equals("")) {
                    messageInput.requestFocus();
                    messageInput.setError("Message cannot be empty!");
                } else {
                    try {
                        Realm.init(getContext());
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        SelfMessage selfMessage = realm.createObject(SelfMessage.class);
                        selfMessage.setBody(message);
                        selfMessage.setTimeCreated(System.currentTimeMillis());
                        realm.commitTransaction();
                        messageInput.setText("");
                    } catch (Exception e) {
                        System.out.println("Failed to save message object in realm");
                        messageInput.setError("Error saving the message. Please try again.");
                    }
                }
            }
        });

        // Get Past messages

        TextView messageBody = root.findViewById(R.id.messageBody);
        TextView messageDate = root.findViewById(R.id.messageDate);
        TextView indicator = root.findViewById(R.id.indicator);
        Button previous = root.findViewById(R.id.previousMessage);
        Button next = root.findViewById(R.id.nextMessage);
        Realm.init(getContext());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<SelfMessage> messages = realm.where(SelfMessage.class).findAll().sort("timeCreated");
        final int[] index = {0};

        changeCurrentMessage(index[0],messages,messageDate,messageBody,indicator);

        messages.addChangeListener(new RealmChangeListener<RealmResults<SelfMessage>>() {
            @Override
            public void onChange(RealmResults<SelfMessage> selfMessages) {
                changeCurrentMessage(index[0],messages,messageDate,messageBody,indicator);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index[0] - 1 < 0) {
                    index[0] = messages.size() - 1;
                } else {
                    index[0] -= 1;
                }
                changeCurrentMessage(index[0],messages,messageDate,messageBody,indicator);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(index[0] +1 >= messages.size()){
                    index[0] = 0;
                }else{
                    index[0] += 1;
                }
                changeCurrentMessage(index[0],messages,messageDate,messageBody,indicator);
            }
        });

        // Hint section
        TextView hint = root.findViewById(R.id.randomHint);
        ImageButton refresh = root.findViewById(R.id.refreshHint);
        changeHint(hint);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeHint(hint);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void changeCurrentMessage(int index, RealmResults<SelfMessage> messages, TextView date, TextView body, TextView indicator){
        if (messages.size() < 1) {
            date.setText("");
            body.setText("No messages to display yet.");
        } else {
            date.setText(messages.get(index).getTimeCreated());
            body.setText(messages.get(index).getBody());
            indicator.setText(index +1 + "/" + messages.size());
        }
        body.setText(messages.get(index).getBody());
        date.setText(messages.get(index).getTimeCreated());
        indicator.setText(index +1 + "/" + messages.size());
    }
    private void changeHint(TextView textView){
        String[] hints = {
                "Talk about how you felt after completing a hard task.",
                "Where would you like to be a year from now?",
                "What is something you like about yourself?",
                "Talk about something you did that you are proud of."
        };
        Random random = new Random(System.currentTimeMillis());
        textView.setText(hints[random.nextInt(hints.length)]);
    }
}