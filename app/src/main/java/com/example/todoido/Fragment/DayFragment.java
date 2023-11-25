package com.example.todoido.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoido.Adapter.DayTaskAdapter;
import com.example.todoido.R;
import com.example.todoido.ViewModel.DayTask;
import com.example.todoido.ViewModel.DayViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DayFragment extends Fragment {
    private BottomSheetBehavior bottomSheetBehavior;
    private FrameLayout bottomSheet;
    private Spinner spinner;

    private boolean isSheetVisible = false;

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private final DatabaseReference databaseRef = firebaseUser != null ? FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("day") : null;
    private int selectedTaskPosition = -1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        bottomSheet = view.findViewById(R.id.sheet_day);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        EditText day_txt = view.findViewById(R.id.day_txt);
        CheckBox smartNotification = view.findViewById(R.id.smart_notification);

        spinner = view.findViewById(R.id.spinner);

        String[] items = new String[]{"선택 안 함", "5분 전", "10분 전", "30분 전", "1시간 전", "3시간 전"};

        ArrayAdapter<String> spinnerAdapter;
        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(spinnerAdapter);

        Button timePickerButton = view.findViewById(R.id.timePickerButton);
        Button timePickerButton2 = view.findViewById(R.id.timePickerButton2);
        MaterialButton addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSheetVisible) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    isSheetVisible = false;
                    selectedTaskPosition = -1;  // BottomSheet가 닫힐 때마다 selectedTaskPosition 초기화
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    isSheetVisible = true;

                    // 아이템 선택이 아닌 addButton을 통해 BottomSheet를 열 경우, 기존 내용 초기화
                    if (selectedTaskPosition == -1) {
                        timePickerButton.setText("시작 시간");
                        timePickerButton2.setText("종료 시간");
                        day_txt.setText("");
                        spinner.setSelection(0);
                        smartNotification.setChecked(false);
                    }

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedItem = parent.getItemAtPosition(position).toString();
                            Toast.makeText(getActivity(), selectedItem + " 선택됨", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }
        });


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

        DayViewModel dayViewModel = new ViewModelProvider(requireActivity()).get(DayViewModel.class);
        RecyclerView recyclerView = view.findViewById(R.id.dayRecyclerView);
        DayTaskAdapter adapter = new DayTaskAdapter(new ArrayList<>(), null);
        adapter.setOnItemClickListener(new DayTaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DayTask task) {
                // 항목 클릭 이벤트를 처리합니다.
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                isSheetVisible = true;
                timePickerButton.setText(task.getStartTime());
                timePickerButton2.setText(task.getEndTime());
                day_txt.setText(task.getText());
                spinner.setSelection(((ArrayAdapter<String>)spinner.getAdapter()).getPosition(task.getSpinnerSelection()));
                smartNotification.setChecked(task.isChecked());
                selectedTaskPosition = adapter.getTaskList().indexOf(task);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dayViewModel.getTaskList().observe(getViewLifecycleOwner(), tasks -> {
            adapter.setTaskList(tasks);
            adapter.notifyDataSetChanged();
        });

        Button submitButton = view.findViewById(R.id.submit_btn);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startTime = timePickerButton.getText().toString();
                String endTime = timePickerButton2.getText().toString();
                String text = day_txt.getText().toString();
                String spinnerSelection = spinner.getSelectedItem().toString();
                boolean isChecked = smartNotification.isChecked();

                DayTask task = new DayTask(startTime, endTime, text, spinnerSelection, isChecked);

                if (selectedTaskPosition != -1) {
                    task.setId(adapter.getTaskList().get(selectedTaskPosition).getId());
                    dayViewModel.updateTask(task);

                    selectedTaskPosition = -1;
                } else {
                    dayViewModel.addTask(task);
                }
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                isSheetVisible = false;

                timePickerButton.setText("00:00");
                timePickerButton2.setText("00:00");
                day_txt.setText("");
                spinner.setSelection(0);
                smartNotification.setChecked(false);
            }
        });


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

            }
        });

        return view;
    }
}
