package com.example.todoido.WeekPage;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.todoido.R;


public abstract class BaseWeekFragment extends Fragment {

    protected void setupDialog(final ImageView mainImageView) {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.emoiton_popup);

        ImageView happyIcon = dialog.findViewById(R.id.happy);
        happyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainImageView.setImageResource(R.drawable.happy);
                dialog.dismiss();
            }
        });

        ImageView smileIcon = dialog.findViewById(R.id.smile);
        smileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainImageView.setImageResource(R.drawable.smile);
                dialog.dismiss();
            }
        });

        ImageView sosoIcon = dialog.findViewById(R.id.soso);
        sosoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainImageView.setImageResource(R.drawable.soso);
                dialog.dismiss();
            }
        });

        ImageView badIcon = dialog.findViewById(R.id.bad);
        badIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainImageView.setImageResource(R.drawable.bad);
                dialog.dismiss();
            }
        });

        ImageView angryIcon = dialog.findViewById(R.id.angry);
        angryIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainImageView.setImageResource(R.drawable.angry);
                dialog.dismiss();
            }
        });

        mainImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
