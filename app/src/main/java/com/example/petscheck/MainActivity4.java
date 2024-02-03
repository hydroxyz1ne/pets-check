package com.example.petscheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity4 extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Button nullcat = (Button) findViewById(R.id.null_yearcat);
        nullcat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent n1 = new Intent(MainActivity4.this, MainActivity6.class);
        startActivity(n1);
    }
}