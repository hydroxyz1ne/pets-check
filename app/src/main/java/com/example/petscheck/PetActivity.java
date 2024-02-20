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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

        TextView countdownTextView = findViewById(R.id.lastVisitInfo);
        String petLastVisit = getIntent().getStringExtra("PET_VISIT");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            Date visitDate = sdf.parse(petLastVisit);

            Calendar currentDate = Calendar.getInstance();
            Date today = currentDate.getTime();

            long daysDifference = (visitDate.getTime() - today.getTime()) / (24 * 60 * 60 * 1000);

            long daysUntilNextSixMonths = 180 - (daysDifference % 180);

            countdownTextView.setText("Через " + daysUntilNextSixMonths + " дней");

        } catch (ParseException e) {
            e.printStackTrace();
        }

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
        visitInfo.setText(petLastVisit);
    }
}
