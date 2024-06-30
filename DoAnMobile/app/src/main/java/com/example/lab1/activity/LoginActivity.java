package com.example.lab1.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.lab1.R;
import com.example.lab1.dao.UserDao;
import com.example.lab1.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtdangki, txtresetpass;
    EditText email, pass;
    AppCompatButton btndangnhap;
    UserDao userDao;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    ImageView logo;
TextView otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        txtdangki = findViewById(R.id.txtdangki);
        txtresetpass = findViewById(R.id.txtresetpass);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        btndangnhap = findViewById(R.id.btndangnhap);
        userDao= new UserDao(this);
        btndangnhap.setOnClickListener(this);
        txtdangki.setOnClickListener(this);
        txtresetpass.setOnClickListener(this);
        logo= findViewById(R.id.img_logo);
        logo.setOnClickListener(this);
        otp=findViewById(R.id.textotp);
        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, VerifyPhone.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id= v.getId();
        if(id == R.id.btndangnhap){
            login();
        }else if(id== R.id.txtdangki){
            Intent intent= new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
            Toast.makeText(LoginActivity.this, "Đăng ký được nhấn", Toast.LENGTH_SHORT).show();
        }else if(id== R.id.txtresetpass){
            Toast.makeText(LoginActivity.this, "Quên mật khẩu được nhấn", Toast.LENGTH_SHORT).show();
            Intent intent1= new Intent(LoginActivity.this,ForgetPasswordActivity.class);
            startActivity(intent1);
        }else if(id== R.id.img_logo){
            Toast.makeText(LoginActivity.this, "Quay về trang chủ", Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }

    private void login() {
        String str_email = email.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();
        if (TextUtils.isEmpty(str_email)) {
            Toast.makeText(LoginActivity.this, "Nhập thông tin email", Toast.LENGTH_SHORT).show();
            email.setError("Email bị để trống");
            email.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()) {
            Toast.makeText(LoginActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            email.setError("Không có @gmail.com");
            email.requestFocus();
        } else if (TextUtils.isEmpty(str_pass)) {
            Toast.makeText(LoginActivity.this, "Nhập mật khẩu", Toast.LENGTH_SHORT).show();
            email.setError("Email bị để trống");
            email.requestFocus();

        } else {
            auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(str_email, str_pass).addOnCompleteListener(LoginActivity.this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                //Toast.makeText(LoginActivity.this, "user.getUid()"+user.getUid(), Toast.LENGTH_LONG).show();
                                if (user != null) {
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()) {
                                                User user1 = snapshot.getValue(User.class);
                                                if (user1.isActive()== true) {
                                                    Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                                                    Gson gson = new Gson();
                                                    String user_json = gson.toJson(user1);
                                                    SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("user", user_json);
                                                    editor.apply();
                                                    if (user1.getRole() == 1) {
                                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                    } else if (user1.getRole() == 2) {
                                                        Toast.makeText(LoginActivity.this, "Hi Admin", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                                        startActivity(intent);
                                                    }

                                                }else {
                                                    Toast.makeText(LoginActivity.this, "Tài khoản đã bị khóa", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                                                Toast.makeText(LoginActivity.this, "lỗi truy xuât dư liệu", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }else{
                                    Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_LONG).show();
                                }


                            } else {
                                Toast.makeText(LoginActivity.this, "User register unsuccessfully", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }
    }

}