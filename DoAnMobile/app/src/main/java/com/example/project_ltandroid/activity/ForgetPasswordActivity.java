package com.example.project_ltandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_ltandroid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView img_logo;
    EditText email;
    Button btnreset;
    TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_password);
        img_logo= findViewById(R.id.img_logo);
        email= findViewById(R.id.edtresetpass);
        btnreset= findViewById(R.id.btnresetpass);
        loginText = findViewById(R.id.loginText);
        btnreset.setOnClickListener(this);
        loginText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btnresetpass){
            Toast.makeText(ForgetPasswordActivity.this, "Kiểm tra email", Toast.LENGTH_SHORT).show();
            resetPass();
        }else if(id == R.id.loginText){
            Toast.makeText(ForgetPasswordActivity.this, "Quay lại đăng nhập", Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(ForgetPasswordActivity.this,LoginActivity.class);
            startActivity(intent);
        }

    }

    private void resetPass() {
        String str_email = email.getText().toString().trim();
        if(TextUtils.isEmpty(str_email)){
            Toast.makeText(ForgetPasswordActivity.this, "Vui Lòng Nhập Email", Toast.LENGTH_SHORT).show();
            email.setError("Email bị để trống");
            email.requestFocus();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()) {
            Toast.makeText(ForgetPasswordActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            email.setError("Không có @gmail.com");
            email.requestFocus();
        }else{
            FirebaseAuth auth= FirebaseAuth.getInstance();
            auth.sendPasswordResetEmail(str_email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Kiểm Tra email rồi đăng nhập lại tài khoản", Toast.LENGTH_LONG).show();
                                Intent intent= new Intent(ForgetPasswordActivity.this,LoginActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(), "Đổi mật khẩu thất bại.Thử lại !", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }
    }

}