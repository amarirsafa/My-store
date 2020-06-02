package com.example.mystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    //private Button registerBtn;
    private EditText firstNameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private String userUID;
    private FirebaseAuth userAuth;
    private FirebaseFirestore DBstore;
    private ProgressBar progressBar;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstNameEditText = findViewById(R.id.Fist_Name_EditText);
        emailEditText = findViewById(R.id.Email_EditText);
        passwordEditText = findViewById(R.id.PassWord_EditText);


        userAuth = FirebaseAuth.getInstance();
        DBstore = FirebaseFirestore.getInstance();
        user = new User();

        findViewById(R.id.register_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInPut();
            }
        });

        findViewById(R.id.signIn_Text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

    }

    private void validateInPut() {

        final String userEmail = emailEditText.getText().toString().trim();
        final String userName = firstNameEditText.getText().toString().trim();
        final String userPassword = passwordEditText.getText().toString().trim();

        if(userName.isEmpty()){
            firstNameEditText.setError("Please enter a password");
        }else if(userEmail.isEmpty()){
            emailEditText.setError("Please enter your email");
        }else if(userPassword.isEmpty()){
            passwordEditText.setError("Please enter a password");
        }else {
            progressBar = new ProgressBar(RegisterActivity.this);
            progressBar.setVisibility(View.VISIBLE);
            userAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        user.setEmail(userEmail);
                        user.setName(userEmail);
                        userUID = Objects.requireNonNull(userAuth.getCurrentUser()).getUid();
                        DocumentReference dRef = DBstore.collection("users").document(userUID);
                        dRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "Your account has been created!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(RegisterActivity.this,ItemsActivity.class);
                                    startActivity(i);
                                    finish();
                                }else{
                                    Toast.makeText(RegisterActivity.this, "Sorry something went wrong try again later!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        if(task.getException() instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(RegisterActivity.this, "this email already exists", Toast.LENGTH_SHORT).show();
                        }else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });

        }
    }
}
