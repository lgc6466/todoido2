package com.example.todoido;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.example.todoido.Fragment.DayFragment;
import com.example.todoido.Fragment.MonthFragment;
import com.example.todoido.Fragment.SettingFragment;
import com.example.todoido.Fragment.WeekFragment;
import com.example.todoido.Fragment.YearFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    DayFragment dayFragment;
    MonthFragment monthFragment;
    WeekFragment weekFragment;
    YearFragment yearFragment;
    int prevSelectedTab = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createNotificationChannel();

        if (!areNotificationsEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                startActivity(intent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra("app_package", getPackageName());
                intent.putExtra("app_uid", getApplicationInfo().uid);
                startActivity(intent);
            }
        } else {
            doSomething();
        }

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
            databaseRef.child("theme").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String theme = dataSnapshot.getValue(String.class);
                    if (theme == null) {  // 테마 정보가 null인 경우 기본 테마를 설정
                        theme = "Theme1";
                    }
                    switch (theme) {
                        case "Theme1":
                            setTheme(R.style.Theme1);  // 테마1 적용
                            break;
                        case "Theme2":
                            setTheme(R.style.Theme2);  // 테마2 적용
                            break;
                        case "Theme3":
                            setTheme(R.style.Theme3);  // 테마3 적용
                            break;
                        default:
                            setTheme(R.style.Theme1);  // 기본 테마 적용
                            break;
                    }

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
                        public void onTabUnselected(TabLayout.Tab tab) {
                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 에러 처리
                }
            });
        } else {
            setTheme(R.style.Theme3);  // 기본 테마 적용
            setContentView(R.layout.activity_main);

        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_name";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private boolean areNotificationsEnabled() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        return notificationManager.areNotificationsEnabled();
    }

    private AtomicInteger notificationId = new AtomicInteger(0);

    private int getUniqueNotificationId() {
        return notificationId.incrementAndGet();
    }

    @SuppressLint("MissingPermission")
    private void sendNotification(String startTime, String text) {
        // 알림 클릭 시 실행할 인텐트 생성
        Intent intent = new Intent(this, MainActivity.class);  // YourActivity는 알림을 클릭하면 열리는 액티비티입니다.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // 알림 설정
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.logo)  // 알림 아이콘 설정
                .setContentTitle(startTime + "에 일정이 있어요!")  // 알림 제목 설정
                .setContentText(text)  // 알림 내용 설정
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // 알림 우선순위 설정
                .setContentIntent(pendingIntent)  // 알림 클릭 시 실행할 인텐트 설정
                .setAutoCancel(true);  // 알림 클릭 시 자동으로 알림 제거

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId는 알림을 업데이트하거나 취소할 때 사용합니다.
        int notificationId = getUniqueNotificationId();
        notificationManager.notify(notificationId, builder.build());
    }

    private void doSomething() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("Users").child(userId).child("day").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // 현재 시간
                    long now = System.currentTimeMillis();
                    Date nowDate = new Date(now);

                    for (DataSnapshot daySnapshot : dataSnapshot.getChildren()) {
                        String startTime = daySnapshot.child("startTime").getValue(String.class);
                        String text = daySnapshot.child("text").getValue(String.class);

                        // 일정의 시간을 파싱합니다. 이 코드는 startTime이 "HH:mm" 형식이라고 가정합니다.
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.KOREA);
                            Date startDate = sdf.parse(startTime);

                            // 현재 날짜 정보를 가져와 startTime에 추가합니다.
                            Calendar startCal = Calendar.getInstance();
                            startCal.setTime(startDate);
                            startCal.set(Calendar.YEAR, nowDate.getYear());
                            startCal.set(Calendar.MONTH, nowDate.getMonth());
                            startCal.set(Calendar.DATE, nowDate.getDate());

                            long eventTime = startCal.getTimeInMillis();

                            // 현재 시간과 일정의 시간을 비교합니다.
                            if (now < eventTime) {
                                // 일정의 시간이 현재 시간보다 미래라면 알림을 보냅니다.
                                if (text != null) {
                                    sendNotification(startTime, text);
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "loadData:onCancelled", databaseError.toException());
                }
            });
        }
    }
}