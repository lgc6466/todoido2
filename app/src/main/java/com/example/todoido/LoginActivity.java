package com.example.todoido;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // activity_login은 레이아웃 XML 파일의 이름이어야 합니다.

        EditText editText = findViewById(R.id.ed_id);
        MaterialCardView cardView = findViewById(R.id.id_cardview);

        cardView.setStrokeColor(Color.parseColor("#FFDA29"));
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cardView.setStrokeWidth(3);
                } else {
                    cardView.setStrokeWidth(1);
                }
            }
        });

        EditText passwordEditText = findViewById(R.id.et_pass);
        MaterialCardView passwordCardView = findViewById(R.id.pw_cardview);

        passwordCardView.setStrokeColor(Color.parseColor("#FFDA29"));
        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    passwordCardView.setStrokeWidth(3);
                } else {
                    passwordCardView.setStrokeWidth(1);
                }
            }
        });
    }
}

