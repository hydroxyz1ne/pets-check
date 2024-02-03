package com.example.petscheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button btn1 = (Button) findViewById(R.id.start_btn);
        MainActivity2 mainActivity = this;
        btn1.setOnClickListener(mainActivity);

    }

    @Override
    public void onClick(View v) {
        Intent n1 = new Intent(MainActivity2.this, MainActivity3.class);
        startActivity(n1);
    }
}