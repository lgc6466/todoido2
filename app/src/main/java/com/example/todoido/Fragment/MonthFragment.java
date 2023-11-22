package com.example.todoido.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.todoido.Adapter.CardAdapter;
import com.example.todoido.R;
import com.example.todoido.ViewModel.MonthViewModel;

import java.util.ArrayList;

public class MonthFragment extends Fragment {

    ArrayList<CardAdapter.CardItem> items = new ArrayList<>();
    CardAdapter adapter;
    private ViewPager2 monthRecyclerView;
    private ViewPager2 viewPager;
    private ActivityResultLauncher<Intent> mGetContent;
    MonthViewModel monthViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewModel 초기화
        monthViewModel = new ViewModelProvider(this).get(MonthViewModel.class);

        // 이미지 선택 Intent 결과 받기 위한 콜백 설정
        mGetContent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImage = data.getData();
                            // 선택한 이미지를 CardAdapter에 설정
                            adapter.setImageUri(selectedImage, viewPager.getCurrentItem());
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);

        // ViewPager2 초기화
        viewPager = view.findViewById(R.id.monthRecyclerView);
        // 페이지 전환 효과 설정
        viewPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float v = Math.abs(Math.abs(position) - 1);
                page.setScaleX(v / 2 + 0.5f);
                page.setScaleY(v / 2 + 0.5f);
            }
        });
        // CardAdapter 설정
        adapter = new CardAdapter(items, getContext(), viewPager, mGetContent, monthViewModel); // items를 인자로 전달
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setAdapter(adapter);

        // '+' 버튼 설정
        ImageButton addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            adapter.addItem("");
            adapter.notifyDataSetChanged();
        });

        // LiveData 관찰 시작
        monthViewModel.getItemList().observe(getViewLifecycleOwner(), monthItems -> {
            // LiveData가 변경되었을 때 리사이클러뷰 갱신
            items.clear();
            for (MonthViewModel.MonthItem monthItem : monthItems) {
                CardAdapter.CardItem cardItem = new CardAdapter.CardItem(monthItem.getContent(), monthItem.getViewType());
                cardItem.setId(monthItem.getId()); // id 필드 설정
                cardItem.setImageUri(monthItem.getImageUri()); // imageUri 필드 설정
                items.add(cardItem);
            }
            adapter.notifyDataSetChanged();
        });

        return view;
    }




}