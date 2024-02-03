package com.example.petscheck;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity7 extends AppCompatActivity {

    ProgressBar pb;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);

        prog();
    }

    public void prog() {
        pb = (ProgressBar) findViewById(R.id.progressBarView);
        final Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                counter++;
                pb.setProgress(counter);

                if(counter == 100) {
                    t.cancel();
                    Intent n1 = new Intent(MainActivity7.this, MainActivity8.class);
                    startActivity(n1);

                }
            }
        };
        t.schedule(tt,0,100);

    }
}