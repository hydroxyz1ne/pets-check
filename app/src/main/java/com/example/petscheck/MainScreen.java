package com.example.petscheck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainScreen extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        CardView btn1 = (CardView) findViewById(R.id.roksikCard);
        MainScreen mainScreen = this;
        btn1.setOnClickListener(mainScreen);
        ImageButton btn2 = (ImageButton) findViewById(R.id.addPetBtn);
        btn2.setOnClickListener(mainScreen);
    }

    @Override
    public void onClick(View v) {
        Intent n1 = new Intent(MainScreen.this, PetActivity.class);
        startActivity(n1);
        Intent n2 = new Intent(MainScreen.this, NewPet.class);
        startActivity(n2);
    }
}