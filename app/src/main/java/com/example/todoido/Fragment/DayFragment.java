package com.example.todoido.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.todoido.Adapter.DayAdapter;
import com.example.todoido.R;

import java.util.ArrayList;

public class DayFragment extends Fragment {

    ArrayList<String> items = new ArrayList<>();
    DayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        // RecyclerView와 어댑터 초기화
        RecyclerView recyclerView = view.findViewById(R.id.dayRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DayAdapter(items);
        recyclerView.setAdapter(adapter);

        // 이미지 버튼에 클릭 리스너 설정
        ImageButton addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 어댑터에 데이터가 변경되었음을 알림
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}

