package com.example.todoido.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoido.R;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private final ArrayList<String> items;

    public CardAdapter(ArrayList<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CardAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_month, parent, false);
        return new CardAdapter.CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.CardViewHolder holder, int position) {
        String item = items.get(position);
        holder.contentEditText.setText(item);
        holder.countTextView.setText(String.valueOf(position + 1));  // 위치에 따라 숫자를 설정

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

    static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView countTextView;
        EditText contentEditText;
        ImageButton closeButton;
        CardViewHolder(View itemView) {
            super(itemView);
            countTextView = itemView.findViewById(R.id.countTextView);
            contentEditText = itemView.findViewById(R.id.contentEditText);
            closeButton = itemView.findViewById(R.id.closeButton);
        }
    }

    // 아이템 추가 메소드
    public void addItem(String item) {
        items.add(item);
        notifyDataSetChanged();  // 어댑터 업데이트
    }

    // 아이템 삭제 메소드
    public void removeItem(int position) {
        items.remove(position);
        notifyDataSetChanged();  // 어댑터 업데이트
    }
}

