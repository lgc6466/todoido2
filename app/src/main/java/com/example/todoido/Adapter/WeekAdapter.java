package com.example.todoido.Adapter;

import android.annotation.SuppressLint;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoido.R;
import com.example.todoido.ViewModel.WeekViewModel;

import java.util.ArrayList;
import java.util.List;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.ViewHolder> {
    private ArrayList<String> items = new ArrayList<>();
    private WeekViewModel weekViewModel;
    private String weekID;

    public WeekAdapter(WeekViewModel weekViewModel, String weekID) {
        this.weekViewModel = weekViewModel;
        this.weekID = weekID;
        items.add("");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.week_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String text = items.get(position);
        holder.weektxt.setText(text);

        holder.heart.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart, 0, 0, 0); // 모든 아이템에 하트 이미지를 추가합니다.

        holder.weektxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE && event == null) {
                    String inputText = holder.weektxt.getText().toString();
                    if (!inputText.isEmpty()) { // 입력된 텍스트가 빈 문자열이 아닐 때만 아이템 추가
                        items.add(inputText);
                        notifyItemInserted(items.size() - 1);
                        holder.weektxt.setText(""); // 현재 EditText를 빈 문자열로 설정
                    }

                    // Firebase에 아이템 추가를 반영합니다.
                    weekViewModel.updateGoals(weekID, new ArrayList<>(items));

                    return true;
                }
                return false;
            }
        });


        holder.weektxt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                int position = holder.getAdapterPosition();
                if (keyCode == KeyEvent.KEYCODE_DEL && holder.weektxt.getText().toString().isEmpty() && items.size() > 1) {
                    items.remove(position);
                    notifyItemRemoved(position);

                    // Firebase에 아이템 삭제를 반영합니다.
                    weekViewModel.updateGoals(weekID, new ArrayList<>(items));

                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(String text) {
        items.add(text);
    }

    public void setSelectedEmoji(String selectedEmoji) {
        weekViewModel.updateSelectedEmoji(weekID, selectedEmoji);
    }

    public void setGoals(List<String> goals) {
        items = new ArrayList<>(goals);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        EditText weektxt;
        TextView heart;

        ViewHolder(View itemView) {
            super(itemView);
            weektxt = itemView.findViewById(R.id.weektxt);
            heart = itemView.findViewById(R.id.heart);
        }
    }
}
