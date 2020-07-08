package com.example.mystore.Client.Containers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.mystore.Admin.containers.AdminLanding;
import com.example.mystore.Client.Activities.LoginActivity;
import com.example.mystore.Client.Classes.User;
import com.example.mystore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Timer timer;
    private FirebaseAuth fAuth;
    private User currentUser;
    private FirebaseFirestore mDataBaseStore ;
    private CollectionReference userColRef;
    private DocumentReference docRef;
    private User user;
    private boolean adminState=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        mDataBaseStore = FirebaseFirestore.getInstance();
        userColRef = mDataBaseStore.collection("users");
        user = new User();
        if(fAuth.getCurrentUser() != null) {
            userColRef.whereEqualTo("email",fAuth.getCurrentUser().getEmail())
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            user = document.toObject(User.class);
                            if(user.getAdmin()){
                                adminState = true;
                            }
                        }
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                }
            });
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(fAuth.getCurrentUser() != null){
                    if(adminState){
                        Toast.makeText(MainActivity.this, "its happening here", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MainActivity.this, AdminLanding.class);
                        startActivity(i);
                        finish();
                    }else{
                        Intent i = new Intent(MainActivity.this, LandingActivity.class);
                        startActivity(i);
                        finish();
                    }
                }else{
                    Intent m = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(m);
                    finish();
                }
            }
        }, 1000);
    }
}
