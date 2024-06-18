package com.example.lab1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab1.R;
import com.example.lab1.dao.UserDao;
import com.example.lab1.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.integrity.internal.m;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtdangki, txtresetpass;
    EditText email, pass;
    AppCompatButton btndangnhap;
    UserDao userDao;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    ImageView logo;
    ImageView google;
    DatabaseReference mDatabase;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN =9001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
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
        google= findViewById(R.id.google);
        google.setOnClickListener(this);

        GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

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
        }else if(id== R.id.google){
            loginByGoogle();
            Toast.makeText(LoginActivity.this, "Login google", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginByGoogle() {
        Intent signinIntent= mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signinIntent,RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task= GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account= task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());

            }
            catch (ApiException e)
            {
                Toast.makeText(this,"login failed:"+e,Toast.LENGTH_SHORT).show();
                String errorMessage = "Google sign-in failed: " + e.getStatusCode() + " - " + e.getMessage();
                Log.e("LoginFaile", "login failed:"+e, e);
            }
        }
    }
    private void firebaseAuth(String idToken){
        AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            // thêm dữ liệu user nếu chưa tồn tại.
                            firebaseUser = auth.getCurrentUser();
                            User a= new User(firebaseUser.getUid(),firebaseUser.getDisplayName(),firebaseUser.getEmail(),firebaseUser.getPhoneNumber(),0);
                            DatabaseReference myRef= mDatabase.getDatabase().getReference();
                            Query query= myRef.orderByChild("email").equalTo(firebaseUser.getEmail());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot userSnapshot : snapshot.getChildren()){
                                        User user = userSnapshot.getValue(User.class);
                                        if(user == null){
                                            mDatabase.child("User").push().setValue(a);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                        else{
                            Toast.makeText(LoginActivity.this,"login failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    //phía dưới là chức năng đăng nhâập thông thường.

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

            auth.signInWithEmailAndPassword(str_email, str_pass).addOnCompleteListener(LoginActivity.this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_LONG).show();
                                Intent intent= new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "User register unsuccessfully", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }
    }
}