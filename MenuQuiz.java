package com.example.informaticapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MenuQuiz extends AppCompatActivity {

    private Button BotonArqui;
    private Button BotonRedes;

    private ImageView BotonAtras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_quiz);

        BotonArqui= findViewById(R.id.btnR_Arqui);

        BotonArqui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuQuiz.this, StartActivity.class);
                startActivity(intent);
            }

        });
        BotonRedes= findViewById(R.id.btnR_redes);

        BotonRedes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuQuiz.this, StarActivity2.class);
                startActivity(intent);
            }

        });
        BotonAtras= findViewById(R.id.imageAtras);

        BotonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuQuiz.this, MenuPrincipal.class);
                startActivity(intent);
            }

        });
    }
}