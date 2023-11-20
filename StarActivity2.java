package com.example.informaticapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StarActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star2);

        final AppCompatButton startQuizBtn = findViewById(R.id.startQuizBtn);
        final AppCompatButton quitBtn = findViewById(R.id.quitBtn);

        startQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // starting MainActivity
                startActivity(new Intent(StarActivity2.this, Redes.class));
            }
        });

        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // close StartActivity
                finish();
            }
        });
    }
}