package com.example.todoido.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.todoido.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

public class DayFragment extends Fragment {
    private BottomSheetBehavior bottomSheetBehavior;
    private FrameLayout bottomSheet;
    private Spinner spinner;

    private boolean isSheetVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        bottomSheet = view.findViewById(R.id.sheet_day);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        spinner = view.findViewById(R.id.spinner);

        ImageButton addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSheetVisible) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    isSheetVisible = false;
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    isSheetVisible = true;

                    // Spinner에 표시할 항목들
                    String[] items = new String[]{"선택 안 함", "5분 전", "10분 전", "30분 전", "1시간 전", "3시간 전"};

                    // Adapter 생성 및 설정
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
                    spinner.setAdapter(adapter);

                    // 항목 선택 이벤트 처리
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // 항목이 선택되었을 때 처리할 작업
                            String selectedItem = parent.getItemAtPosition(position).toString();
                            Toast.makeText(getActivity(), selectedItem + " 선택됨", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // 아무 항목도 선택되지 않았을 때 처리할 작업
                        }
                    });
                }
            }
        });
        Button timePickerButton = view.findViewById(R.id.timePickerButton);
        Button timePickerButton2 = view.findViewById(R.id.timePickerButton2);

        View.OnClickListener timePickerClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;

                MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                        .setTheme(R.style.CustomTimePickerTheme)
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(00)
                        .setMinute(00)
                        .build();

                materialTimePicker.show(getChildFragmentManager(), "TIME_PICKER");

                materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int hour = materialTimePicker.getHour();
                        int minute = materialTimePicker.getMinute();
                        clickedButton.setText(String.format("%02d:%02d", hour, minute));
                        clickedButton.setTextColor(getResources().getColor(R.color.selected_tab_text_color));
                    }
                });
            }
        };

        timePickerButton.setOnClickListener(timePickerClickListener);
        timePickerButton2.setOnClickListener(timePickerClickListener);


        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheet.setClickable(false);
                    bottomSheet.setClipToOutline(false);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheet.setClickable(true);
                    bottomSheet.setClipToOutline(true);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // 슬라이드 중인 경우 추가적인 작업이 필요하다면 여기에 구현할 수 있습니다.
            }
        });

        return view;
    }
}
