package com.example.petscheck;

import static android.app.PendingIntent.getActivity;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainScreen extends AppCompatActivity implements View.OnClickListener{
    private static final int PERMISSION_REQUEST_CODE = 1;

    LinearLayout linearPetsContainer;
    DatabaseReference databaseReference;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private WebView myWebView;
    private TextView textView1, textView2, textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        linearPetsContainer = findViewById(R.id.linearPetsContainer);
        databaseReference = FirebaseDatabase.getInstance().getReference("Pet");
        loadPetsFromFirebase();

        TextView textView1 = findViewById(R.id.newsbtn1);
        TextView textView2 = findViewById(R.id.newsbtn2);
        TextView textView3 = findViewById(R.id.newsbtn3);
        ImageButton btn1 = findViewById(R.id.addPetBtn);
        TrackerFragment fragment = new TrackerFragment();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n1 = new Intent(MainScreen.this, NewPet.class);
                startActivity(n1);
            }
        });

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebView("https://vetclinika.by/articles/5-kastraciya-kotov-i-sterilizaciya-koshek.html");
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebView("https://vetclinika.by/zapax-izo-rta-u-koshki.-dva-primera-lichnogo-opyita.html");
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebView("https://vetclinika.by/stoit-li-brat-zhivotnoe-iz-priyuta.html");
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        ImageButton openDialogButton = findViewById(R.id.btnDialog);

        openDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMyDialog();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.navigation_home) {
                    loadFragment(new HomeFragment());
                    return true;
                } else if (itemId == R.id.navigation_read) {
                    loadFragment(new ReadFragment());
                    return true;
                } else if (itemId == R.id.navigation_toilet) {
                    loadFragment(new ToiletFragment());
                    return true;
                } else if (itemId == R.id.navigation_tracker) {
                    loadFragment(new TrackerFragment());
                    return true;
                } else {
                    loadFragment(new ProfileFragment());
                    return true;
                }
            }
        });
        // Запрос разрешений на Bluetooth
        requestBluetoothPermissions();
    }

    private void requestBluetoothPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
            }, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение предоставлено
                Log.d("Permissions", "Bluetooth permissions granted");
            } else {
                // Разрешение отклонено
                Log.d("Permissions", "Bluetooth permissions denied");
            }
        }
    }

    private void openWebView(String url) {
        Intent intent = new Intent(MainScreen.this, WebViewActivity.class);
        intent.putExtra("URL", url);
        startActivity(intent);
    }


    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment); // Replace R.id.fragment_container with your actual container id
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        Intent n1 = new Intent(MainScreen.this, NewPet.class);
        startActivity(n1);
    }
    private void openMyDialog() {
        MyDialogFragment dialogFragment = new MyDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
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
                            intent.putExtra("PET_BREED", currentPet.getBreed());
                            intent.putExtra("PET_WEIGHT", currentPet.getWeight());
                            intent.putExtra("PET_VISIT", currentPet.getVisit());
                            intent.putExtra("PET_IMAGE", currentPet.getImageId());
                            intent.putExtra("PET_KEY", currentPet.getImageId());

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