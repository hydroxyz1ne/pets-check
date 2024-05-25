


package com.example.petscheck;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity; // открытие и работа с первой формой

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private final int zamorozka=5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent per = new Intent(MainActivity.this, MainScreen.class);
                MainActivity.this.startActivity(per);
                MainActivity.this.finish();
                if(FirebaseAuth.getInstance().getCurrentUser()==null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else{
                    startActivity(new Intent(MainActivity.this,MainScreen.class));
                }
            }

        },zamorozka);


    }
}
