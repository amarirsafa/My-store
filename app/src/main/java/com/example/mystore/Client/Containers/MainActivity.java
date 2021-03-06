package com.example.mystore.Client.Containers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.mystore.Admin.containers.AdminLanding;
import com.example.mystore.Client.Activities.LoginActivity;
import com.example.mystore.Classes.User;
import com.example.mystore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Timer timer;
    private FirebaseAuth fAuth;
    private FirebaseFirestore mDataBaseStore ;
    private CollectionReference userColRef;
    private DocumentReference docRef;
    private User user;
    public boolean adminState=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        mDataBaseStore = FirebaseFirestore.getInstance();
        userColRef = mDataBaseStore.collection("users");
        user = new User();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(fAuth.getCurrentUser() != null) {
                    userColRef.whereEqualTo("email",fAuth.getCurrentUser().getEmail())
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                    user = document.toObject(User.class);
                                    if(user.getAdmin()){
                                        Intent ii = new Intent(MainActivity.this, AdminLanding.class);
                                        startActivity(ii);
                                        finish();
                                    }else{
                                        Intent ii = new Intent(MainActivity.this, LandingActivity.class);
                                        startActivity(ii);
                                        finish();
                                    }
                                }
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                    });
                }else{
                    Intent m = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(m);
                    finish();
                }
            }
        }, 1000);
    }
}
