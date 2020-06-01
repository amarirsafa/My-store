package com.example.mystore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Timer timer;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(fAuth.getCurrentUser() != null){
                    Intent i = new Intent(MainActivity.this, WishListActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent m = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(m);
                    finish();
                }
            }
        }, 1000);
    }
}
