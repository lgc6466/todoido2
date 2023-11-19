package com.example.todoido.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoido.R;

import java.util.ArrayList;
import java.util.Random;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private ArrayList<CardItem> items;
    private Context context;

    public CardAdapter(ArrayList<CardItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_month1, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardItem item = items.get(position);
        String content = item.getContent();

        holder.contentEditText.setText(content);
        holder.closeButton.setOnClickListener(v -> removeItem(position));
    }
    public static class CardItem {
        private String content;

        public CardItem(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }

    public void addItem(String content) {
        items.add(new CardItem(content));
        notifyItemInserted(items.size() - 1);
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyDataSetChanged();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        EditText contentEditText;
        ImageButton closeButton;

        CardViewHolder(View itemView) {
            super(itemView);
            contentEditText = itemView.findViewById(R.id.contentEditText);
            closeButton = itemView.findViewById(R.id.closeButton);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
