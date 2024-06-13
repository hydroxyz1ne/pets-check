


package com.example.petscheck;

import androidx.appcompat.app.AppCompatActivity; // открытие и работа с первой формой

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    private final int zamorozka=5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent per = new Intent(SplashScreen.this, MainScreen.class);
                SplashScreen.this.startActivity(per);
                SplashScreen.this.finish();
                if(FirebaseAuth.getInstance().getCurrentUser()==null) {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                } else{
                    startActivity(new Intent(SplashScreen.this,MainScreen.class));
                }
            }

        },zamorozka);


    }
}
