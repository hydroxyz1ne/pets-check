package com.example.petscheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PetActivity extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_pet);

        imageView = findViewById(R.id.imageView);

        Intent intent = getIntent();
        String petName = intent.getStringExtra("PET_NAME");
        String petWeight = intent.getStringExtra("PET_WEIGHT") + " кг";
        String petBreed = "Порода:" + intent.getStringExtra("PET_BREED");
        String petVisit = intent.getStringExtra("PET_VISIT");

        // Затем используйте imageView
        Picasso.get()
                .load(intent.getStringExtra("PET_IMAGE"))
                .error(R.drawable.without)
                .into(imageView);

        Log.d("PetActivity", "Loading image from: " + intent.getStringExtra("PET_IMAGE"));

        TextView nameInfo = findViewById(R.id.nameInfo);
        TextView weightInfo = findViewById(R.id.weightInfo);
        TextView breedInfo = findViewById(R.id.breedInfo);
        TextView visitInfo = findViewById(R.id.visitInfo);

        nameInfo.setText(petName);
        weightInfo.setText(petWeight);
        breedInfo.setText(petBreed);
        visitInfo.setText(petVisit);
    }
}
