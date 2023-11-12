package com.example.todoido;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.todoido.Fragment.DayFragment;
import com.example.todoido.Fragment.MonthFragment;
import com.example.todoido.Fragment.WeekFragment;
import com.example.todoido.Fragment.YearFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    DayFragment dayFragment;
    MonthFragment monthFragment;
    WeekFragment weekFragment;
    YearFragment yearFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dayFragment = new DayFragment();
        monthFragment = new MonthFragment();
        weekFragment = new WeekFragment();
        yearFragment = new YearFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, dayFragment).commit();

        NavigationBarView navigationBarView = findViewById(R.id.navigation);
        navigationBarView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_day) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, dayFragment).commit();
                return true;
            } else if (itemId == R.id.nav_month) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, monthFragment).commit();
                return true;
            } else if (itemId == R.id.nav_week) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, weekFragment).commit();
                return true;
            } else if (itemId == R.id.nav_year) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, weekFragment).commit();
                return true;
            }
            return false;
        });
    }


}
