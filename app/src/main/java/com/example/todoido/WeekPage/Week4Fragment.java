package com.example.todoido.WeekPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.todoido.R;

public class Week4Fragment extends Fragment {

    ViewPager viewPager;
    Button buttonPrev, buttonNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week4, container, false);

        viewPager = getActivity().findViewById(R.id.week_viewPager);

        buttonPrev = view.findViewById(R.id.week4_prev);
        buttonNext = view.findViewById(R.id.week4_next);

        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        return view;
    }

}