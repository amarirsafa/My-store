package com.example.mystore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LandingActivity extends AppCompatActivity {
    private Button LoginBtn,SignUpBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        LoginBtn = findViewById(R.id.login_btn);
        SignUpBtn = findViewById(R.id.sign_up_btn);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LandingActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LandingActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });
    }
}
