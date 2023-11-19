package com.example.todoido.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.todoido.Adapter.CardAdapter;
import com.example.todoido.R;

import java.util.ArrayList;

public class MonthFragment extends Fragment {

    ArrayList<CardAdapter.CardItem> items = new ArrayList<>();
    CardAdapter adapter;
    private ViewPager2 monthRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);

        monthRecyclerView = view.findViewById(R.id.monthRecyclerView);

        monthRecyclerView.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float v = Math.abs(Math.abs(position) - 1);
                page.setScaleX(v / 2 + 0.5f);
                page.setScaleY(v / 2 + 0.5f);
            }
        });

        // ViewPager2와 CardAdapter 설정
        ViewPager2 viewPager = view.findViewById(R.id.monthRecyclerView);
        adapter = new CardAdapter(new ArrayList<>(), getContext());
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setAdapter(adapter);

        // '+' 버튼 설정
        ImageButton addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            adapter.addItem("");
            adapter.notifyDataSetChanged();
        });

        return view;
    }
}