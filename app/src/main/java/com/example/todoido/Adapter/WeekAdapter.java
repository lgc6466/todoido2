package com.example.todoido.Adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todoido.R;

import java.util.ArrayList;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.ViewHolder> {
    private ArrayList<String> items = new ArrayList<>();

    public WeekAdapter() {
        // Initially add one empty item
        items.add("");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.week_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        EditText weektxt;
        ImageView heart;

        ViewHolder(View itemView) {
            super(itemView);
            weektxt = itemView.findViewById(R.id.weektxt);
            heart = itemView.findViewById(R.id.heart);

            weektxt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (getAdapterPosition() == items.size() - 1 && !s.toString().isEmpty()) {
                        items.add("");
                        notifyItemInserted(items.size() - 1);
                    }
                }
            });
        }

        void bind(String text) {
            weektxt.setText(text);
        }
    }
}
