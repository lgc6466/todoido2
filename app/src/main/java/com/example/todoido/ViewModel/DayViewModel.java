package com.example.todoido.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DayViewModel extends ViewModel {
    private MutableLiveData<List<DayTask>> taskList;
    private DatabaseReference databaseRef;

    public DayViewModel() {
        taskList = new MutableLiveData<>();
        taskList.setValue(new ArrayList<>());

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseRef = firebaseUser != null ? FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("day") : null;

        if (databaseRef != null) {
            databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<DayTask> newTaskList = new ArrayList<>();

                    for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                        DayTask task = taskSnapshot.getValue(DayTask.class);
                        newTaskList.add(task);
                    }

                    taskList.setValue(newTaskList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public MutableLiveData<List<DayTask>> getTaskList() {
        return taskList;
    }

    public void addTask(DayTask task) {
        if (databaseRef != null) {
            String id = databaseRef.push().getKey();
            task.setId(id);
            databaseRef.child(id).setValue(task);

            List<DayTask> currentList = taskList.getValue();
            currentList.add(task);
            taskList.setValue(currentList);
        }
    }

    public void updateTask(DayTask task) {
        if (databaseRef != null) {
            String id = task.getId();
            databaseRef.child(id).setValue(task);

            List<DayTask> currentList = taskList.getValue();
            for (int i = 0; i < currentList.size(); i++) {
                if (currentList.get(i).getId().equals(id)) {
                    currentList.set(i, task);
                    break;
                }
            }
            taskList.setValue(currentList);
        }
    }

}

