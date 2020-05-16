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
    private ProgressBar progressBar;
    private FirebaseFirestore DBstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstNameEditText = findViewById(R.id.Fist_Name_EditText);
        emailEditText = findViewById(R.id.Email_EditText);
        passwordEditText = findViewById(R.id.PassWord_EditText);


        userAuth = FirebaseAuth.getInstance();
        DBstore = FirebaseFirestore.getInstance();

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
            //Toast.makeText(this, "hey", Toast.LENGTH_SHORT).show();
            //validateEmail(userName,userEmail,userPassword);
            progressBar = new ProgressBar(RegisterActivity.this);
            progressBar.setVisibility(View.VISIBLE);
            userAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        //User user = new User(userEmail,userName);
                        HashMap<String, Object> user  = new HashMap<>();
                        user.put("First Name",userName);
                        user.put("Email",userEmail);
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

    /*private void validateEmail(final String userName, final String userEmail, final String userPassword) {
        final DatabaseReference DbRef;
        DbRef = FirebaseDatabase.getInstance().getReference();

        DbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("User").child(userName).exists())){
                    Toast.makeText(RegisterActivity.this, "hey", Toast.LENGTH_SHORT).show();
                    HashMap<String, Object> userDataMap  = new HashMap<>();
                    userDataMap.put("First Name",userName);
                    userDataMap.put("Email",userEmail);
                    userDataMap.put("password",userPassword);

                    DbRef.child("User").child(userName).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Your account has been created!", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(RegisterActivity.this, "Sorry something went wrong try again later!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }else{
                    emailEditText.setError("This email already exists");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RegisterActivity.this, "something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        });
    }*/


}
