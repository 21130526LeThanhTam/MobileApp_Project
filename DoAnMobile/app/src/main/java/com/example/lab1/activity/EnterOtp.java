package com.example.lab1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class EnterOtp extends AppCompatActivity {
    public static final String TAG= EnterOtp.class.getName();
public FirebaseAuth mAuth;
    public EditText otpEditText;
    public Button btnConfirmotp;
    public TextView textViewotp;
    public String mPhoneNumber;
    public String mVerificarionID;
    public PhoneAuthProvider.ForceResendingToken mForceResendingToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_phone);
        initUI();
        mAuth = FirebaseAuth.getInstance();
        btnConfirmotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strOtp = otpEditText.getText().toString().trim();
                onClickOtp(strOtp);
            }
        });
        textViewotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSendOtpAgain();
            }
        });
    }
    private void getDataIntent(){
mPhoneNumber= getIntent().getStringExtra("phonenumber");
mVerificarionID = getIntent().getStringExtra("vertifyID");
    }

    private void onClickSendOtpAgain() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mPhoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)
                        .setForceResendingToken(mForceResendingToken)// (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(EnterOtp.this,"Verify fail",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationID, forceResendingToken);
                               mVerificarionID = verificationID;
                               mForceResendingToken= forceResendingToken;
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void onClickOtp(String strOtp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificarionID, strOtp);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            goToMainActivity(user.getPhoneNumber());
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(EnterOtp.this,"mã không phù hợp",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    private void goToMainActivity(String phoneNumber) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("phonenumber",phoneNumber);
        startActivity(intent);
    }
    public void initUI(){
        otpEditText =findViewById(R.id.phoneEditText);
        btnConfirmotp = findViewById(R.id.confirmButton);
        textViewotp= findViewById(R.id.textViewOtp);

    }
}
