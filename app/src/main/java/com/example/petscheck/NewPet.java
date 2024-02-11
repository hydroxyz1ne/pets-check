package com.example.petscheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class NewPetCat extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pet_cat);
        Button cat_btn = (Button) findViewById(R.id.addCatBtn);
        cat_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent n1 = new Intent(NewPetCat.this, MainActivity4.class);
        startActivity(n1);
    }
}