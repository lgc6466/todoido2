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

    // + 버튼이 처음 눌렸는지를 저장하는 변수
    private boolean isFirstClick = true;

    // context를 멤버 변수로 선언
    private Context context;

    public CardAdapter(ArrayList<CardItem> items, Context context) {
        this.items = items;
        this.context = context;

        // '+' 버튼이 이미 있는지 확인하고, 없으면 추가
        boolean isButtonPresent = false;
        for (CardItem item : items) {
            if (item.getViewType() == BUTTON_VIEW_TYPE) {
                isButtonPresent = true;
                break;
            }
        }
        if (!isButtonPresent) {
            items.add(new CardItem("", BUTTON_VIEW_TYPE));
        }
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
            case BUTTON_VIEW_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_button, parent, false);
                return new ButtonViewHolder(view);
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
        } else if (holder instanceof ButtonViewHolder) {
            ButtonViewHolder buttonHolder = (ButtonViewHolder) holder;

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) buttonHolder.itemView.getLayoutParams();
            // + 버튼이 처음 눌렸을 경우에만 마진을 설정
            params.topMargin = isFirstClick ? 0 : (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics());
            buttonHolder.itemView.setLayoutParams(params);

            buttonHolder.addButton.setOnClickListener(v -> {
                addItem("");
                moveButtonToEnd();
                isFirstClick = false;
            });
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

    // '버튼' 아이템을 리스트의 마지막으로 이동하는 메소드
    public void moveButtonToEnd() {
        CardItem buttonItem = items.remove(items.size() - 2);
        items.add(buttonItem);
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

    static class ButtonViewHolder extends RecyclerView.ViewHolder {
        ImageButton addButton;

        ButtonViewHolder(View itemView) {
            super(itemView);
            addButton = itemView.findViewById(R.id.addButton);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}