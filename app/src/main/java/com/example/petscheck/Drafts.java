package com.example.petscheck;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Черновик extends AppCompatActivity {
    private static final int NOTIFY_ID = 101;
    private static final String CHANNEL_ID = "Cat_channel";
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test);
        createNotificationChannel();

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotification();
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
                } else if (itemId == R.id.navigation_tracker) {
                    loadFragment(new TrackerFragment());
                    return true;
                } else {
                    loadFragment(new ProfileFragment());
                    return true;
                }
            }
        });
    }
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment); // Replace R.id.fragment_container with your actual container id
        fragmentTransaction.commit();
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification() {
        // Создание интента для действия кнопки 1
        Intent intent1 = new Intent(this, SplashScreen.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(
                this,
                0,
                intent1,
                PendingIntent.FLAG_IMMUTABLE
        );

        // Создание интента для действия кнопки 2
        Intent intent2 = new Intent(this, SplashScreen.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(
                this,
                0,
                intent2,
                PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(чЕРНОВИК.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle("Напоминание")
                        .setContentText("Пора покормить кота")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .addAction(R.drawable.icon, "Покормить", pendingIntent1)
                        // Добавление кнопки 2
                        .addAction(R.drawable.icon, "Отложить", pendingIntent2);;

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFY_ID, builder.build());
    }
}
