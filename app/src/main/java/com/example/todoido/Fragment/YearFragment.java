package com.example.todoido.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.todoido.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.todoido.R;
import com.example.todoido.ViewModel.YearViewModel;

public class YearFragment extends Fragment {
    private YearViewModel yearViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_year, container, false);
        LinearLayout layout = view.findViewById(R.id.yearlayout);

        yearViewModel = new ViewModelProvider(this).get(YearViewModel.class);

        for (int i = 1; i <= 10; i++) {
            View itemView = view.findViewById(getResources().getIdentifier("item" + i, "id", getActivity().getPackageName()));
            CheckBox checkBox = itemView.findViewById(R.id.checkBox1);
            EditText editText = itemView.findViewById(R.id.editText1);

            final String itemID = "item" + i;

            yearViewModel.getItemList().observe(getViewLifecycleOwner(), items -> {
                for (YearViewModel.Item item : items) {
                    if (item.getId().equals(itemID)) {
                        checkBox.setChecked(item.getChecked());
                        editText.setText(item.getText());
                    }
                }
            });

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                yearViewModel.updateItemCheckStatus(itemID, isChecked);
            });

            editText.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    yearViewModel.updateItemText(itemID, editText.getText().toString());
                }
            });
        }

        return view;
    }
}

