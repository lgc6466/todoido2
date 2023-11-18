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

public class YearFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_year, container, false);
        LinearLayout layout = view.findViewById(R.id.yearlayout);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Year").child("items");

            for (int i = 1; i <= 10; i++) {
                View itemView = view.findViewById(getResources().getIdentifier("item" + i, "id", getActivity().getPackageName()));
                CheckBox checkBox = itemView.findViewById(R.id.checkBox1);
                EditText editText = itemView.findViewById(R.id.editText1);

                final String itemID = "item" + i;

                databaseRef.child(itemID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Boolean checked = dataSnapshot.child("checked").getValue(Boolean.class);
                        String text = dataSnapshot.child("text").getValue(String.class);
                        if (checked != null) checkBox.setChecked(checked);
                        if (text != null) editText.setText(text);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    databaseRef.child(itemID).child("checked").setValue(isChecked);
                });

                editText.setOnFocusChangeListener((v, hasFocus) -> {
                    if (!hasFocus) {
                        databaseRef.child(itemID).child("text").setValue(editText.getText().toString());
                    }
                });
            }
        }



        return view;
    }
}

