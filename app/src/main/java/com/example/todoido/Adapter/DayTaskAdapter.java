package com.example.todoido.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoido.R;
import com.example.todoido.ViewModel.DayTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DayTaskAdapter extends RecyclerView.Adapter<DayTaskAdapter.ViewHolder> {

    private List<DayTask> taskList;
    private OnItemClickListener listener;

    public void setTaskList(List<DayTask> tasks) {
        this.taskList = tasks;
        notifyDataSetChanged();
    }

    public List<DayTask> getTaskList() {
        return this.taskList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(DayTask task);
    }

    public DayTaskAdapter(List<DayTask> taskList, OnItemClickListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DayTask task = taskList.get(position);
        holder.startTime.setText(task.getStartTime());
        holder.endTime.setText(task.getEndTime());
        holder.contentText.setText(task.getText());
        if (listener != null) { // null 체크를 추가했습니다.
            holder.itemView.setOnClickListener(v -> listener.onItemClick(task));
        }

        holder.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // taskList에서 제거합니다.
                taskList.remove(task);
                notifyItemRemoved(position);

                // Firebase 데이터베이스에서 제거합니다.
                DatabaseReference taskRef = FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("day")
                        .child(task.getId()); // 이 부분은 task가 Firebase에 저장된 ID를 가지고 있다고 가정합니다.
                taskRef.removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView startTime, endTime, contentText;
        ImageButton closeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
            contentText = itemView.findViewById(R.id.contentText);
            closeButton = itemView.findViewById(R.id.closeButton);
        }
    }

}


