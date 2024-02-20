package com.example.petscheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainScreen extends AppCompatActivity implements View.OnClickListener{

    LinearLayout linearPetsContainer;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);


        linearPetsContainer = findViewById(R.id.linearPetsContainer);
        databaseReference = FirebaseDatabase.getInstance().getReference("Pet");
        loadPetsFromFirebase();

        ImageButton btn1 = findViewById(R.id.addPetBtn);
        btn1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent n1 = new Intent(MainScreen.this, NewPet.class);
        startActivity(n1);
    }

    private void loadPetsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                linearPetsContainer.removeAllViews();

                for (DataSnapshot petSnapshot : dataSnapshot.getChildren()) {
                    String petName = petSnapshot.child("name").getValue(String.class);
                    String petImage = petSnapshot.child("imageId").getValue(String.class);

                    Log.d("FirebaseData", "Pet name from Firebase: " + petName);

                    Pets currentPet = petSnapshot.getValue(Pets.class);

                    View petCard = getLayoutInflater().inflate(R.layout.card_pet, null);
                    TextView textViewPetName = petCard.findViewById(R.id.title);
                    ImageView imageViewPet = petCard.findViewById(R.id.cardImage);
                    textViewPetName.setText(petName);
                    Picasso.get().load(petImage).error(R.drawable.without).into(imageViewPet);

                    linearPetsContainer.addView(petCard);

                    petCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Создаем интент для перехода в PetActivity
                            Intent intent = new Intent(MainScreen.this, PetActivity.class);

                            // Передаем данные о питомце в PetActivity
                            intent.putExtra("PET_NAME", currentPet.getName());
                            intent.putExtra("PET_WEIGHT", currentPet.getWeight());
                            intent.putExtra("PET_BREED", currentPet.getBreed());
                            intent.putExtra("PET_VISIT", currentPet.getVisit());
                            intent.putExtra("PET_IMAGE", currentPet.getImageId());

                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainScreen.this, "Failed to load pets.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}