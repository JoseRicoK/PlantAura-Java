package com.example.plantaaura;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class VideoExplicativo extends AppCompatActivity {

    Button btnRegis, btnLog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_explicativo);

        btnRegis = findViewById(R.id.btnRegistro);
        btnLog = findViewById(R.id.btnLogin);

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(VideoExplicativo.this, Registro.class);
                startActivity(intent);
            }
        });


        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(VideoExplicativo.this, Login.class);
                startActivity(intent1);
            }
        });

    }

}