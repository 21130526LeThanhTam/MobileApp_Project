package com.example.lab1.activity;


import static com.example.lab1.activity.MainActivity.MY_REQUEST_CODE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.lab1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {
    private View mView;
    private ImageView imgAvatar;
    private EditText editFullName, editphone;
    private TextView editsdt;
    private Button btnUpdateProfile;
    private Button btnBack;
    Uri mUri;
    MainActivity mMainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_edit_profile, container, false);

        initUI();
        mMainActivity = (MainActivity) getActivity();
        setUserInformation();
        initListener();
        return mView;
    }

    private void initListener() {
        imgAvatar.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             onClickRequestPermisson();
                                         }
                                     }
        );
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateProfile();
                onClickUpdatePhone();

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

    }



    private void onClickUpdateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String strFullName = editFullName.getText().toString().trim();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(strFullName)
                .setPhotoUri(mUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User")
                                    .child(user.getUid());
                            databaseReference.child("name").setValue(strFullName).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                                    Toast.makeText(getActivity(), "update thành công", Toast.LENGTH_SHORT).show();
                                    mMainActivity.showUserInformation();
                                }
                            });

                        }
                    }
                });
    }

    private void onClickUpdatePhone() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user == null) {
//            return;
//        }
//        String strFullName = editphone.getText().toString().trim();
//        user.updatePhoneNumber(PhoneAuthCredential.zza())
//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName(strFullName)
//                .setPhotoUri(mUri)
//                .build();
//
//        user.updateProfile(profileUpdates)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User")
//                                    .child(user.getUid());
//                            databaseReference.child("phone").setValue(editphone).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
//                                    Toast.makeText(getActivity(), "update thành công", Toast.LENGTH_SHORT).show();
//                                    mMainActivity.showUserInformation();
//                                }
//                            });
//
//                        }
//                    }
//                });
    }

    public void setBitMapImageView(Bitmap bitmapImageView) {
        imgAvatar.setImageBitmap(bitmapImageView);

    }

    private void onClickRequestPermisson() {

        if (mMainActivity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mMainActivity.openGallery();
            return;
        }
        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mMainActivity.openGallery();
        } else {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermissions(permissions, MY_REQUEST_CODE);
        }
    }

    private void setUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;

        }
        editFullName.setText(user.getDisplayName());
        editsdt.setText(user.getEmail());
        editphone.setText(user.getPhoneNumber());
        Glide.with(getActivity()).load(user.getPhotoUrl()).error(R.drawable.user).into(imgAvatar);
    }

    public void initUI() {
        imgAvatar = mView.findViewById(R.id.img_avatar);
        editFullName = mView.findViewById(R.id.editTextName);
        editsdt = mView.findViewById(R.id.editTextPhone);
        btnUpdateProfile = mView.findViewById(R.id.buttonUpdate);
        btnBack = mView.findViewById(R.id.buttonBack);
        editphone=mView.findViewById(R.id.editphone);


    }

    public void setUri(Uri mUri) {
        this.mUri = mUri;
    }
}
