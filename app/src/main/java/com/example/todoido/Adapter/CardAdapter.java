package com.example.todoido.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.todoido.R;
import com.example.todoido.ViewModel.MonthViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Random;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private ArrayList<CardItem> items;
    private Context context;
    private ViewPager2 viewPager;
    private ActivityResultLauncher<Intent> mGetContent;
    private MonthViewModel monthViewModel;

    static class CardViewHolder extends RecyclerView.ViewHolder {
        public TextWatcher textWatcher;
        EditText contentEditText;
        ImageButton closeButton;
        ImageView monthPic;
        CardItem item;

        CardViewHolder(View itemView) {
            super(itemView);
            contentEditText = itemView.findViewById(R.id.contentEditText);
            closeButton = itemView.findViewById(R.id.closeButton);
            monthPic = itemView.findViewById(R.id.monthpic);

            // TextWatcher 생성
            textWatcher = new TextWatcher() {
                private String previousText = "";

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    previousText = s.toString();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    String newText = s.toString();
                    if (!newText.equals(previousText)) {
                        // 변경된 텍스트를 데이터베이스에 저장
                        item.setContent(newText);
                        previousText = newText;
                    }
                }
            };
        }
    }

    public CardAdapter(ArrayList<CardItem> items, Context context, ViewPager2 viewPager, ActivityResultLauncher<Intent> mGetContent, MonthViewModel monthViewModel) {
        this.items = items;
        this.context = context;
        this.viewPager = viewPager;
        this.mGetContent = mGetContent;
        this.monthViewModel = monthViewModel;
    }

    // 이미지를 설정할 메소드
    public void setImageUri(Uri uri, int position) {
        CardItem item = items.get(position);
        if (item.getImageUri() == null) {  // 이미지가 설정되어 있지 않은 경우에만 이미지 설정

            // Firebase Storage 참조 가져오기
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();

            // 이미지 Uri에서 파일 이름 가져오기
            String filename = uri.getLastPathSegment();
            // 업로드할 파일의 Storage 참조 만들기
            StorageReference fileRef = storageRef.child("images/" + filename);

            // 이미지 업로드
            UploadTask uploadTask = fileRef.putFile(uri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // 업로드 성공 후, 이미지 URL 가져오기
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                            // 데이터베이스에 이미지 URL 저장
                            item.setImageUri(downloadUri);
                            notifyItemChanged(position);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // 업로드 실패
                }
            });
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

    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        // 이전에 설정한 TextWatcher 제거
        holder.contentEditText.removeTextChangedListener(holder.textWatcher);

        CardItem item = items.get(position);
        holder.item = item;
        String content = item.getContent();

        holder.contentEditText.setText(content);

        // 클릭 리스너를 통해 포커스 요청
        holder.contentEditText.setOnClickListener(v -> holder.contentEditText.requestFocus());

        // TextWatcher 설정
        holder.textWatcher.afterTextChanged(new Editable.Factory().newEditable(content));
        holder.contentEditText.addTextChangedListener(holder.textWatcher);

        holder.contentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newText = s.toString();
                if (!newText.equals(content)) {
                    // 변경된 텍스트를 데이터베이스에 저장
                    item.setContent(newText);
                    monthViewModel.updateItem(item.getId(), item.getContent(), item.getViewType(), item.getImageUri());
                }
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

                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    mGetContent.launch(intent);
                }
            }
        });
    }

    public static class CardItem {
        private String id;
        private String content;
        private int viewType;
        private Uri imageUri;

        public CardItem(String content, int viewType) {
            this.content = content;
            this.viewType = viewType;
            this.imageUri = null;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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


        public void setContent(String content) { // setContent 메소드 추가
            this.content = content;
        }
    }

    public void addItem(String content) {
        int viewType = new Random().nextInt(3);
        CardItem newItem = new CardItem(content, viewType);
        String id = monthViewModel.addItem(newItem);
        newItem.setId(id);
        items.add(newItem);
        int newPosition = items.size() - 1;
        notifyItemInserted(newPosition);
        viewPager.setCurrentItem(newPosition, true);
    }

    public void removeItem(int position, Runnable afterRemoval) {
        CardItem item = items.get(position);
        items.remove(position);
        notifyItemRemoved(position);
        if (afterRemoval != null) {
            afterRemoval.run();
        }
        viewPager.post(() -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem());
            viewPager.getAdapter().notifyDataSetChanged();
        });
        monthViewModel.removeItem(item.getId());
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
