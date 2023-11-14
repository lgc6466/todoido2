package com.example.todoido.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoido.R;

import java.util.ArrayList;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {

    private final ArrayList<String> items;

    public DayAdapter(ArrayList<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        String item = items.get(position);
        holder.timeEditText.setText(item);
        holder.contentEditText.setText(item);

        holder.closeButton.setOnClickListener(new View.OnClickListener() {
            //x버튼
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    items.remove(position);
                    notifyItemRemoved(position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {

        EditText timeEditText;
        EditText contentEditText;
        ImageButton closeButton;

        DayViewHolder(View itemView) {
            super(itemView);
            timeEditText = itemView.findViewById(R.id.timeEditText);
            contentEditText = itemView.findViewById(R.id.contentEditText);
            closeButton = itemView.findViewById(R.id.closeButton);
        }
    }
}

