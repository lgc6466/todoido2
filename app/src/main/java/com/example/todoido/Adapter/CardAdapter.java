package com.example.todoido.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.todoido.R;

import java.util.ArrayList;
import java.util.Random;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private ArrayList<CardItem> items;
    private Context context;
    private ViewPager2 viewPager;
    private ActivityResultLauncher<Intent> mGetContent;

    public CardAdapter(ArrayList<CardItem> items, Context context, ViewPager2 viewPager, ActivityResultLauncher<Intent> mGetContent) {
        this.items = items;
        this.context = context;
        this.viewPager = viewPager;
        this.mGetContent = mGetContent;
    }

    // 이미지를 설정할 메소드
    public void setImageUri(Uri uri, int position) {
        CardItem item = items.get(position);
        if (item.getImageUri() == null) {  // 이미지가 설정되어 있지 않은 경우에만 이미지 설정
            item.setImageUri(uri);
            notifyItemChanged(position);
        }
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_month1, parent, false);
                break;
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_month2, parent, false);
                break;
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_month3, parent, false);
                break;
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardItem item = items.get(position);
        String content = item.getContent();

        holder.contentEditText.setText(content);
        holder.closeButton.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                removeItem(currentPosition, () -> viewPager.setCurrentItem(viewPager.getCurrentItem(), false));
            }
        });

        // 이미지가 설정되어 있는 경우 ImageView에 이미지 설정
        if (item.getImageUri() != null) {
            Glide.with(holder.monthPic.getContext())
                    .load(item.getImageUri())
                    .centerCrop()
                    .into(holder.monthPic);
        } else {
            // 이미지가 설정되어 있지 않은 경우 ImageView를 초기화
            holder.monthPic.setImageDrawable(null);
        }

        // gallerybtn 찾기와 클릭 리스너 설정
        ImageButton galleryBtn = holder.itemView.findViewById(R.id.gallerybtn);
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                // 클릭 리스너 안에서 현재 아이템의 위치를 기억
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {  // 위치가 유효한 경우에만 인텐트를 실행
                    mGetContent.launch(intent);
                }
            }
        });
    }

    public static class CardItem {
        private String content;
        private int viewType;
        private Uri imageUri;

        public CardItem(String content, int viewType) {
            this.content = content;
            this.viewType = viewType;
            this.imageUri = null;
        }

        public String getContent() {
            return content;
        }

        public int getViewType() {
            return viewType;
        }

        // 이미지 Uri getter와 setter 추가
        public Uri getImageUri() {
            return imageUri;
        }

        public void setImageUri(Uri imageUri) {
            this.imageUri = imageUri;
        }
    }

    public void addItem(String content) {
        int viewType = new Random().nextInt(3);
        items.add(new CardItem(content, viewType));
        notifyItemInserted(items.size() - 1);
    }

    public void removeItem(int position, Runnable afterRemoval) {
        items.remove(position);
        notifyItemRemoved(position);
        if (afterRemoval != null) {
            afterRemoval.run();
        }
        viewPager.post(() -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem());
            viewPager.getAdapter().notifyDataSetChanged();
        });
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        EditText contentEditText;
        ImageButton closeButton;
        ImageView monthPic;

        CardViewHolder(View itemView) {
            super(itemView);
            contentEditText = itemView.findViewById(R.id.contentEditText);
            closeButton = itemView.findViewById(R.id.closeButton);
            monthPic = itemView.findViewById(R.id.monthpic);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
