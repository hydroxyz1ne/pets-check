package com.example.petscheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity3 extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ImageButton cat_btn = (ImageButton) findViewById(R.id.cat_btn);
        ImageButton dog_btn = (ImageButton) findViewById(R.id.dog_btn);
        cat_btn.setOnClickListener(this);
        dog_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent n1 = new Intent(MainActivity3.this, MainActivity4.class);
        startActivity(n1);
        Intent n2 = new Intent(MainActivity3.this, MainActivity4.class);
        startActivity(n2);
    }
}