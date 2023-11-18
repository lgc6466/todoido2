package com.example.todoido.WeekPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.todoido.R;



public class Week2Fragment extends Fragment {
    ViewPager viewPager;
    Button buttonPrev, buttonNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week2, container, false);

        viewPager = getActivity().findViewById(R.id.week_viewPager);

        buttonPrev = view.findViewById(R.id.week2_prev);
        buttonNext = view.findViewById(R.id.week2_next);

        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

        return view;
    }
}