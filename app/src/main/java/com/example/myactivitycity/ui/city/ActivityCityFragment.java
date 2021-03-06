package com.example.myactivitycity.ui.city;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.method.Touch;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.myactivitycity.Models.Reward;
import com.example.myactivitycity.NavigationDrawerActivity;
import com.example.myactivitycity.R;
import com.example.myactivitycity.TempDataHolder;
import com.example.myactivitycity.adapters.RewardImageMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ActivityCityFragment extends Fragment {
    // set width and height for all items
    int width, height;
    RealmResults<Reward> rewards;
    // Hash map to use image ids as keys to get their corresponding reward object through an index
    Hashtable<Integer, Integer> imageToIndexMap;

    public ActivityCityFragment() {
        width = 220;
        height = 220;
    }

    public static ActivityCityFragment newInstance() {
        ActivityCityFragment fragment = new ActivityCityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initiate images list to hold image views
        imageToIndexMap = new Hashtable<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_city, container, false);


        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Set the container for the rewards to be placed
        FrameLayout rewardContainer = view.findViewById(R.id.itemsContainer);


        // Create listeners for dragging and dropping items
        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData clipData = ClipData.newPlainText("", "");
                View.DragShadowBuilder builder = new View.DragShadowBuilder(view);
                view.startDrag(clipData, builder, view, 0);
                return true;
            }
        };
        View.OnDragListener dragListener = new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {

                int event = dragEvent.getAction();
                final View itemView = (View) dragEvent.getLocalState();

                switch (event) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        break;
                    case DragEvent.ACTION_DROP:
                        itemView.animate()
                                .x(dragEvent.getX() - width / 2)
                                .y(dragEvent.getY() - height / 2)
                                .setDuration(500)
                                .start();
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
                        params.setMargins((int) dragEvent.getX() - width / 2, (int) dragEvent.getY() - height / 2, 0, 0);
                        itemView.setLayoutParams(params);

                        // update database with new position
                        try {
                            int index = imageToIndexMap.get(itemView.getId());
                            System.out.println(imageToIndexMap);
                            System.out.println(itemView.getId());
                            System.out.println("modifying data for: " + rewards.get(index).getDescription());
                            System.out.println(view.getId());
                            System.out.println(index);
                            Realm.init(getContext());
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            rewards.get(index).setX((int) dragEvent.getX() - width / 2);
                            rewards.get(index).setY((int) dragEvent.getY() - height / 2);
                            realm.commitTransaction();
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Failed to map image id to index");
                        }
                        break;
                }
                return true;
            }
        };


        View.OnClickListener viewDescriptionListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = imageToIndexMap.get(view.getId());
                Reward r = rewards.get(index);
                Toast.makeText(getContext(), r.getDescription(), Toast.LENGTH_SHORT).show();
            }
        };

        // Retrieve rewards from database
        Realm.init(getContext());
        Realm realm = Realm.getDefaultInstance();
        rewards = realm.where(Reward.class).findAll();

        // Create image representation for rewards
        for (int i = 0; i < rewards.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(RewardImageMapper.getImageByName(rewards.get(i).getName()));
            final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
            params.setMargins(rewards.get(i).getX(), rewards.get(i).getY(), 0, 0);
            imageView.setLayoutParams(params);
            imageView.setId(View.generateViewId());
            rewardContainer.addView(imageView);
            imageToIndexMap.put(imageView.getId(), i);
            imageView.setOnLongClickListener(longClickListener);
            imageView.setOnClickListener(viewDescriptionListener);
        }


        view.setOnDragListener(dragListener);
        return view;
    }

}
