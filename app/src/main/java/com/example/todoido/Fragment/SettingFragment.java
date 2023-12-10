package com.example.todoido.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.todoido.LoginActivity;
import com.example.todoido.R;
import com.example.todoido.SnowView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingFragment extends Fragment {

    private TabLayout tabLayout;
    private int prevSelectedTab;
    private boolean isEyeOff1 = true;
    private boolean isEyeOff2 = true;
    private boolean isEyeOff3 = true;

    public SettingFragment(TabLayout tabLayout, int prevSelectedTab) {
        this.tabLayout = tabLayout;
        this.prevSelectedTab = prevSelectedTab;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        MaterialCardView materialCardView = view.findViewById(R.id.setting_layout);
        Animation slideInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);
        Animation slideOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_right);

        materialCardView.startAnimation(slideInAnimation);

        FrameLayout dimBackground = view.findViewById(R.id.dim_background);
        dimBackground.setVisibility(View.VISIBLE);

        // 설정창 닫기
        ImageButton cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialCardView.startAnimation(slideOutAnimation);
                dimBackground.setVisibility(View.GONE);
                slideOutAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (getFragmentManager() != null) {
                            getFragmentManager().beginTransaction().remove(SettingFragment.this).commit();
                            tabLayout.getTabAt(prevSelectedTab).select();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        });

        // 이름과 이메일 띄우기
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(uid);

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String username = dataSnapshot.child("username").getValue(String.class);

                    // TextView에 username 설정
                    TextView userId = view.findViewById(R.id.user_id);
                    userId.setText(username + "님");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle possible errors.
                }
            });

            String email = user.getEmail();
            TextView userEmail = view.findViewById(R.id.user_email);
            userEmail.setText(email);
        }

        // 비밀번호 변경
        Button passChangeButton = view.findViewById(R.id.pass_change);
        passChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dialog 생성
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.pass_change);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // Dialog 크기를 WRAP_CONTENT로 설정
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.show();  // Dialog 띄우기

                // 비밀번호 변경 Dialog에서 필요한 View를 찾습니다.
                ImageButton eye1 = dialog.findViewById(R.id.eye);
                ImageButton eye2 = dialog.findViewById(R.id.eye2);
                ImageButton eye3 = dialog.findViewById(R.id.eye3);

                EditText et_pass1 = dialog.findViewById(R.id.et_pass);
                EditText et_pass2 = dialog.findViewById(R.id.new_pass);
                EditText et_pass3 = dialog.findViewById(R.id.confirm_pass);

                // 취소 버튼
                Button dialogCancelButton = dialog.findViewById(R.id.cancelButton);
                dialogCancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();  // Dialog 닫기
                    }
                });

                // 변경 버튼
                Button changeButton = dialog.findViewById(R.id.changeButton);
                changeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 현재 비밀번호, 새로운 비밀번호, 비밀번호 확인
                        String currentPassword = et_pass1.getText().toString();
                        String newPassword = et_pass2.getText().toString();
                        String confirmPassword = et_pass3.getText().toString();

                        // Firebase 인증 객체
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        FirebaseUser user = auth.getCurrentUser();

                        // 현재 비밀번호와 새로운 비밀번호가 비어있지 않은지 확인
                        if (!TextUtils.isEmpty(currentPassword) && !TextUtils.isEmpty(newPassword)) {
                            // 새로운 비밀번호와 현재 비밀번호가 동일한지 확인
                            if (currentPassword.equals(newPassword)) {
                                Toast.makeText(getContext(), "새 비밀번호가 현재 비밀번호와 같습니다.", Toast.LENGTH_SHORT).show();
                            }
                            // 새로운 비밀번호와 비밀번호 확인이 일치하는지 확인
                            else if (newPassword.equals(confirmPassword)) {
                                // 사용자 재인증 (이메일과 현재 비밀번호를 사용)
                                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
                                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // 비밀번호 변경
                                            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getContext(), "비밀번호가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();  // Dialog 닫기
                                                    } else {
                                                        Toast.makeText(getContext(), "비밀번호 변경에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(getContext(), "현재 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), "비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // 보이기 숨기기 이미지버튼
                eye1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isEyeOff1) {
                            // 비밀번호 보이기
                            eye1.setImageResource(R.drawable.eye);
                            et_pass1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        } else {
                            // 비밀번호 숨기기
                            eye1.setImageResource(R.drawable.eyeoff);
                            et_pass1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        }
                        isEyeOff1 = !isEyeOff1;
                        et_pass1.setSelection(et_pass1.getText().length()); // 커서를 마지막으로 이동
                    }
                });

                eye2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isEyeOff2) {
                            // 비밀번호 보이기
                            eye2.setImageResource(R.drawable.eye);
                            et_pass2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        } else {
                            // 비밀번호 숨기기
                            eye2.setImageResource(R.drawable.eyeoff);
                            et_pass2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        }
                        isEyeOff2 = !isEyeOff2;
                        et_pass2.setSelection(et_pass2.getText().length()); // 커서를 마지막으로 이동
                    }
                });

                eye3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isEyeOff3) {
                            // 비밀번호 보이기
                            eye3.setImageResource(R.drawable.eye);
                            et_pass3.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        } else {
                            // 비밀번호 숨기기
                            eye3.setImageResource(R.drawable.eyeoff);
                            et_pass3.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        }
                        isEyeOff3 = !isEyeOff3;
                        et_pass3.setSelection(et_pass3.getText().length()); // 커서를 마지막으로 이동
                    }
                });
            }
        });


        // 로그아웃
        Button logoutButton = view.findViewById(R.id.logout_btn);
        logoutButton.setPaintFlags(logoutButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Firebase에서 로그아웃
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getContext(), "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();

                // 로그인 화면으로 이동
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        // 회원 탈퇴
        Button deleteButton = view.findViewById(R.id.delet_btn);
        deleteButton.setPaintFlags(deleteButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                        .setMessage("탈퇴하시겠습니까?")
                        .setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                                if (user != null) {
                                    // Delete the user from FirebaseAuth
                                    user.delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        // User is deleted from FirebaseAuth. Now delete the user from FirebaseDatabase
                                                        ref.child("Users").child(user.getUid()).removeValue()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(getContext(), "계정이 성공적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                                                            // Here you can start LoginActivity after successful deletion
                                                                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                            startActivity(intent);
                                                                        } else {
                                                                            Toast.makeText(getContext(), "데이터베이스에서 계정을 삭제하는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                    } else {
                                                        Toast.makeText(getContext(), "계정을 삭제하는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        })
                        .setNegativeButton("취소", null)
                        .create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        int positiveButtonColor = ContextCompat.getColor(getActivity(), R.color.asdf);
                        int negativeButtonColor = ContextCompat.getColor(getActivity(), R.color.asdf);

                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(positiveButtonColor);
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(negativeButtonColor);
                    }
                });

                dialog.show();
            }
        });

        //테마 변경
        Button themeChange = view.findViewById(R.id.theme_change);
        themeChange.setPaintFlags(themeChange.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        themeChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.backdialog_layout);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.backdialog_shape);

                // 각 LinearLayout과 RadioButton을 찾습니다.
                LinearLayout layout1 = dialog.findViewById(R.id.layout1);
                LinearLayout layout2 = dialog.findViewById(R.id.layout2);
                LinearLayout layout3 = dialog.findViewById(R.id.layout3);

                RadioButton radioButton1 = dialog.findViewById(R.id.radio1);
                RadioButton radioButton2 = dialog.findViewById(R.id.radio2);
                RadioButton radioButton3 = dialog.findViewById(R.id.radio3);

                ColorStateList colorStateList = ContextCompat.getColorStateList(getActivity(), R.color.radio_button_selector);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    radioButton1.setButtonTintList(colorStateList);
                    radioButton2.setButtonTintList(colorStateList);
                    radioButton3.setButtonTintList(colorStateList);
                }

                // 라디오 버튼에 체크 변경 리스너를 설정합니다.
                radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            radioButton2.setChecked(false);
                            radioButton3.setChecked(false);
                        }
                    }
                });

                radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            radioButton1.setChecked(false);
                            radioButton3.setChecked(false);
                        }
                    }
                });

                radioButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            radioButton1.setChecked(false);
                            radioButton2.setChecked(false);
                        }
                    }
                });

                // '적용' 버튼을 찾아 클릭 리스너를 설정합니다.
                Button applyButton = dialog.findViewById(R.id.apply_button);
                applyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 라디오 버튼의 상태에 따라 테마를 변경하는 코드를 여기에 작성합니다.
                        // ...
                        dialog.dismiss();
                    }
                });

                // '취소' 버튼을 찾아 클릭 리스너를 설정합니다.
                Button cancelButton = dialog.findViewById(R.id.cancel_button);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        // 눈 내리는 효과
        final SnowView snowView = view.findViewById(R.id.snowView);
        ToggleButton toggleButton = view.findViewById(R.id.toggleButton1);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // 눈 내리는 효과 켜기
                    snowView.setVisibility(View.VISIBLE);
                } else {
                    // 눈 내리는 효과 끄기
                    snowView.setVisibility(View.INVISIBLE);
                }
            }
        });

        return view;
    }

}