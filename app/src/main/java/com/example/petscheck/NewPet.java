package com.example.petscheck;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewPet extends AppCompatActivity {
    private EditText namePet, agePet, weightPet, breedPet;
    private DatabaseReference mDataBase;
    private String PET_KEY = "Pet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pet);
        init();
    }


    private void init() {
        namePet = findViewById(R.id.namePet);
        agePet = findViewById(R.id.agePet);
        weightPet = findViewById(R.id.weightPet);
        breedPet = findViewById(R.id.breedPet);
        mDataBase = FirebaseDatabase.getInstance().getReference(PET_KEY);
    }

    public void onClickSave(View view){
        String id = mDataBase.getKey();
        String name = namePet.getText().toString();
        String age = agePet.getText().toString();
        String weight = weightPet.getText().toString();
        String breed = breedPet.getText().toString();
        Pets newPet = new Pets(id, name, age, weight, breed);
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(age) && !TextUtils.isEmpty(weight) && !TextUtils.isEmpty(breed)) {
            mDataBase.push().setValue(newPet);
            startActivity(new Intent(NewPet.this, MainActivity7.class));
        } else {
            Toast.makeText(this, "Пустое поле", Toast.LENGTH_SHORT).show();
        }

    }
}