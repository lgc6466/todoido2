package com.example.todoido.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);

        // RecyclerView와 CardAdapter 설정
        RecyclerView recyclerView = view.findViewById(R.id.monthRecyclerView);
        adapter = new CardAdapter(new ArrayList<>(), getContext());
        recyclerView.setAdapter(adapter);

        // '+' 버튼 설정
        ImageButton addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            adapter.addItem("");
            adapter.notifyDataSetChanged();
        });

        return view;
    }
}
