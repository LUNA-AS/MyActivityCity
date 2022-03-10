package com.example.myactivitycity.ui.city;

import android.content.ClipData;
import android.content.pm.ActivityInfo;
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

import com.example.myactivitycity.Models.Reward;
import com.example.myactivitycity.R;

public class ActivityCityFragment extends Fragment {
    float currentX, currentY;

    public ActivityCityFragment() {
        // Required empty public constructor
        currentX = 0;
        currentY = 0;
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
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_city, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        FrameLayout rewardContainer = view.findViewById(R.id.itemsContainer);
        ImageView imageView = new ImageView(getContext());
        Reward reward = new Reward();
        imageView.setImageResource(R.drawable.ic_baseline_location_city_24);
        final FrameLayout.LayoutParams[] params = {new FrameLayout.LayoutParams(400, 400)};
        params[0].setMargins(0, 10, 0, 10);
        imageView.setLayoutParams(params[0]);
        rewardContainer.addView(imageView);

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
                        currentX = dragEvent.getX();
                        currentY = dragEvent.getY();
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        break;
                    case DragEvent.ACTION_DROP:
                        itemView.animate()
                                .x(dragEvent.getX() - 200)
                                .y(dragEvent.getY() - 200)
                                .setDuration(500)
                                .start();
                        params[0] = new FrameLayout.LayoutParams(400, 400);
                        params[0].setMargins((int) dragEvent.getX() - 200, (int) dragEvent.getY() - 200, 0, 0);
                        itemView.setLayoutParams(params[0]);


                        break;
                }

                return true;
            }
        };

        imageView.setOnLongClickListener(longClickListener);
        view.setOnDragListener(dragListener);
        return view;
    }

    public class MyDragShadowBuilder extends View.DragShadowBuilder {

        @Override
        public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {
            outShadowSize.set(1, 1);
            outShadowTouchPoint.set(0, 0);
        }
    }
}
