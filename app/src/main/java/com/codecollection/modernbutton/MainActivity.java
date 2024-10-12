package com.codecollection.modernbutton;

import android.os.Bundle;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ModernButton progressButton = findViewById(R.id.sendingBtn);
        progressButton.setOnProgressCompleteListener(() -> Toast.makeText(MainActivity.this, "Progress Complete", Toast.LENGTH_SHORT).show());

    }
}