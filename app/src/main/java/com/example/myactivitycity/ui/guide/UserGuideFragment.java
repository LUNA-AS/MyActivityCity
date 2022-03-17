package com.example.myactivitycity.ui.guide;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myactivitycity.R;

public class UserGuideFragment extends Fragment {


    public UserGuideFragment() {
        // Required empty public constructor
    }

    public static UserGuideFragment newInstance() {
        UserGuideFragment fragment = new UserGuideFragment();
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
        return inflater.inflate(R.layout.fragment_user_guide, container, false);
    }
}