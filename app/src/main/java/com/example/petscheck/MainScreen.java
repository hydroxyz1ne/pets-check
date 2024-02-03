package com.example.petscheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainScreen extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        ImageButton btn1 = (ImageButton) findViewById(R.id.addPet);
        MainScreen mainScreen = this;
        btn1.setOnClickListener(mainScreen);
    }

    @Override
    public void onClick(View v) {
        Intent n1 = new Intent(MainScreen.this, MainActivity3.class);
        startActivity(n1);
    }
}