package com.example.petscheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity8 extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);
        Button btn1 = (Button) findViewById(R.id.goToMainScreen);
        MainActivity8 mainActivity = this;
        btn1.setOnClickListener(mainActivity);

    }

    @Override
    public void onClick(View v) {
        Intent n1 = new Intent(MainActivity8.this, MainScreen.class);
        startActivity(n1);
    }
}