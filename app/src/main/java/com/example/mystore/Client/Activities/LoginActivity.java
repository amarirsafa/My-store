package com.example.mystore.Client.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mystore.Client.Containers.LandingActivity;
import com.example.mystore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText passwordEditText,emailEditText;
    private FirebaseAuth userAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.Email_EditText);
        passwordEditText = findViewById(R.id.PassWord_EditText);
        userAuth = FirebaseAuth.getInstance();


        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInput();
            }
        });
    }

    private void validateInput() {
        String userEmail = emailEditText.getText().toString().trim();
        String userPassword = passwordEditText.getText().toString().trim();

        if(userEmail.isEmpty()){
            emailEditText.setError("Please enter your email");
        }else if(userPassword.isEmpty()){
            passwordEditText.setError("Please enter a password");
        }else{
            userAuth.signInWithEmailAndPassword(userEmail,userPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent i = new Intent(LoginActivity.this, LandingActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, "we couldn't log you in!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}
