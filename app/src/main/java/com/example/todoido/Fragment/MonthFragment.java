package com.example.todoido.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.todoido.Adapter.CardAdapter;
import com.example.todoido.R;

import java.util.ArrayList;
import java.util.Random;

public class MonthFragment extends Fragment {

    ArrayList<CardAdapter.CardItem> items = new ArrayList<>();
    CardAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.monthRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // '버튼' 아이템을 아이템 리스트에 추가
        items.add(new CardAdapter.CardItem("", CardAdapter.BUTTON_VIEW_TYPE));

        // CardAdapter의 생성자에 Context를 전달
        adapter = new CardAdapter(items, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }
}
