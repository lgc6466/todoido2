package com.example.todoido.Adapter;

import android.content.Context;
import android.util.TypedValue;
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

public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<CardItem> items;
    public static final int BUTTON_VIEW_TYPE = 3;

    // context를 멤버 변수로 선언
    private Context context;

    public CardAdapter(ArrayList<CardItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_month1, parent, false);
                return new ViewHolder1(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_month2, parent, false);
                return new ViewHolder2(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_month3, parent, false);
                return new ViewHolder3(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CardItem item = items.get(position);
        String content = item.getContent();

        if (holder instanceof ViewHolder1) {
            ViewHolder1 viewHolder1 = (ViewHolder1) holder;
            viewHolder1.contentEditText.setText(content);
            viewHolder1.closeButton.setOnClickListener(v -> removeItem(holder.getAdapterPosition()));
        } else if (holder instanceof ViewHolder2) {
            ViewHolder2 viewHolder2 = (ViewHolder2) holder;
            viewHolder2.contentEditText.setText(content);
            viewHolder2.closeButton.setOnClickListener(v -> removeItem(holder.getAdapterPosition()));
        } else if (holder instanceof ViewHolder3) {
            ViewHolder3 viewHolder3 = (ViewHolder3) holder;
            viewHolder3.contentEditText.setText(content);
            viewHolder3.closeButton.setOnClickListener(v -> removeItem(holder.getAdapterPosition()));
        }
    }

    public static class CardItem {
        private String content;
        private int viewType;

        public CardItem(String content, int viewType) {
            this.content = content;
            this.viewType = viewType;
        }

        public String getContent() {
            return content;
        }

        public int getViewType() {
            return viewType;
        }
    }

    // 아이템 추가 메소드
    public void addItem(String content) {
        int viewType = new Random().nextInt(3);
        items.add(new CardItem(content, viewType));
        notifyItemInserted(items.size() - 1);
    }

    // 아이템 삭제 메소드
    public void removeItem(int position) {
        items.remove(position);
        notifyDataSetChanged();
    }

    static class ViewHolder1 extends RecyclerView.ViewHolder {
        EditText contentEditText;
        ImageButton closeButton;

        ViewHolder1(View itemView) {
            super(itemView);
            contentEditText = itemView.findViewById(R.id.contentEditText);
            closeButton = itemView.findViewById(R.id.closeButton);
        }
    }

    static class ViewHolder2 extends RecyclerView.ViewHolder {
        EditText contentEditText;
        ImageButton closeButton;

        ViewHolder2(View itemView) {
            super(itemView);
            contentEditText = itemView.findViewById(R.id.contentEditText);
            closeButton = itemView.findViewById(R.id.closeButton);
        }
    }

    static class ViewHolder3 extends RecyclerView.ViewHolder {
        EditText contentEditText;
        ImageButton closeButton;

        ViewHolder3(View itemView) {
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