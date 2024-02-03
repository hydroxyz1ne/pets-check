package com.example.petscheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity6 extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        ImageButton but1 = (ImageButton) findViewById(R.id.but1);
        but1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent n1 = new Intent(MainActivity6.this, MainActivity7.class);
        startActivity(n1);

    }
}