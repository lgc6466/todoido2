package com.example.todoido;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.todoido.Fragment.DayFragment;
import com.example.todoido.Fragment.MonthFragment;
import com.example.todoido.Fragment.SettingFragment;
import com.example.todoido.Fragment.WeekFragment;
import com.example.todoido.Fragment.YearFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    DayFragment dayFragment;
    MonthFragment monthFragment;
    WeekFragment weekFragment;
    YearFragment yearFragment;
    int prevSelectedTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme3);  // 테마 적용
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dayFragment = new DayFragment();
        monthFragment = new MonthFragment();
        weekFragment = new WeekFragment();
        yearFragment = new YearFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, dayFragment).commit();

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Day"));
        tabLayout.addTab(tabLayout.newTab().setText("Week"));
        tabLayout.addTab(tabLayout.newTab().setText("Month"));
        tabLayout.addTab(tabLayout.newTab().setText("Year"));
        tabLayout.addTab(tabLayout.newTab().setText("Set"));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() != 4) {
                    prevSelectedTab = tab.getPosition();
                }

                switch (tab.getPosition()) {
                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, dayFragment).commit();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, weekFragment).commit();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, monthFragment).commit();
                        break;
                    case 3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, yearFragment).commit();
                        break;
                    case 4:
                        SettingFragment settingFragment = new SettingFragment(tabLayout, prevSelectedTab);
                        getSupportFragmentManager().beginTransaction().replace(R.id.root_view_id, settingFragment).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

    }
}
