package com.example.lab1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.lab1.R;
import com.example.lab1.model.Cart;
import com.example.lab1.model.CartItem;
import com.example.lab1.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    EditText email, pass, repass, mobile, username;
    AppCompatButton btndangky, btndangnhap;
    DatabaseReference mDatabase;
//    ArrayList<String>phone;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        repass = findViewById(R.id.repass);
        mobile = findViewById(R.id.mobile);
        username = findViewById(R.id.username);
        btndangky = findViewById(R.id.btndangki);
        btndangnhap = findViewById(R.id.btn_quaylai);
        btndangky.setOnClickListener(this);
        btndangnhap.setOnClickListener(this);
    }
    private void register(){
        String str_email = email.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();
        String str_repass = repass.getText().toString().trim();
        String str_mobile = mobile.getText().toString().trim();
        String str_username = username.getText().toString().trim();


        if(TextUtils.isEmpty(str_email)) {
            Toast.makeText(RegisterActivity.this, "Nhập thông tin email", Toast.LENGTH_SHORT).show();
            email.setError("Email bị để trống");
            email.requestFocus();
        } else if(!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()){
            Toast.makeText(RegisterActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            email.setError("Không có @gmail.com");
            email.requestFocus();
        } else if(TextUtils.isEmpty(str_pass)){
            Toast.makeText(RegisterActivity.this, "Nhập mật khẩu", Toast.LENGTH_SHORT).show();
            email.setError("Mật khẩu bị để trống");
            email.requestFocus();
        } else if(TextUtils.isEmpty(str_repass)){
            Toast.makeText(RegisterActivity.this, "Nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
            email.setError("Mật khẩu nhập lại bị để trống");
            email.requestFocus();
        } else if(TextUtils.isEmpty(str_mobile)){
            Toast.makeText(RegisterActivity.this, "Nhập số điện thoại", Toast.LENGTH_SHORT).show();
            email.setError("Số điện thoại bị để trống");
            email.requestFocus();
        } else if(TextUtils.isEmpty(str_username)){
            Toast.makeText(RegisterActivity.this, "Nhập tên hệ thống", Toast.LENGTH_SHORT).show();
            email.setError("Tên hệ thống bị để trống");
            email.requestFocus();
        } else if(str_pass.equals(str_repass)){
            auth= FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(str_email,str_pass).addOnCompleteListener(RegisterActivity.this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
//                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                String userID= auth.getUid();
                                List<CartItem> ca=new ArrayList<>();
                                User a = new User(userID,str_username,str_email,str_mobile,1,new Cart(ca));
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User").child(userID);
                                mDatabase.setValue(a);
                                Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                // chuyển sang trang chính.
                                Intent intent= new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();//to close Register
                            }else{
                                Exception e = task.getException();
                                String erro=""
;                                if (e instanceof FirebaseAuthWeakPasswordException) {
                                    // Mật khẩu quá yếu
                                    erro="Mật khẩu quá yếu. Yêu cầu mật khẩu có ít nhất 6 ký tự.";
                                }  if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    // Thông tin đăng nhập không hợp lệ, ví dụ như email không đúng định dạng
                                   erro+= "Thông tin email không đúng định dạng";
                                }  if (e instanceof FirebaseAuthUserCollisionException) {
                                    // Email đã được sử dụng để đăng ký tài khoản khác
                                    erro+= "Email đã được sử dụng để đăng ký tài khoản khác.";
                                } else {
                                    // Xử lý các lỗi khác
                                    erro= "Đăng ký thất bại: " + e.getMessage();
                                }
                                Toast.makeText(RegisterActivity.this, erro, Toast.LENGTH_LONG).show();
                                //send verification email.
//                                auth.sendEmailVerification();
                                // chuyển sang trang chính.
                                //Intent intent= new Intent(RegisterActivity.this,MainActivity.class);
                                //To prevent User from returning back to Register Activity on pressing back button after register.
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
//                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
                                //finish();//to close Register
                            }
                        }
                    });
        }

    }
    @Override
    public void onClick(View v) {
        int id= v.getId();
        if(id == R.id.btndangki){
            register();

        }else if(id== R.id.btn_quaylai){
            Intent intent= new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
            Toast.makeText(RegisterActivity.this, " Quay lại Đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }

}