//package com.example.petscheck;
//
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//
//public class Drafts extends AppCompatActivity {
//    private static final int NOTIFY_ID = 101;
//    private static final String CHANNEL_ID = "Cat_channel";
//    private BottomNavigationView bottomNavigationView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.test);
//        createNotificationChannel();
//
//        bottomNavigationView = findViewById(R.id.bottomNavigation);
//
//        Button button = findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showNotification();
//            }
//        });
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                int itemId = menuItem.getItemId();
//
//                if (itemId == R.id.navigation_home) {
//                    loadFragment(new HomeFragment());
//                    return true;
//                } else if (itemId == R.id.navigation_read) {
//                    loadFragment(new ReadFragment());
//                    return true;
//                } else if (itemId == R.id.navigation_tracker) {
//                    loadFragment(new TrackerFragment());
//                    return true;
//                } else {
//                    loadFragment(new ProfileFragment());
//                    return true;
//                }
//            }
//        });
//    }
//    private void loadFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_container, fragment); // Replace R.id.fragment_container with your actual container id
//        fragmentTransaction.commit();
//    }
//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//
//    private void showNotification() {
//        // Создание интента для действия кнопки 1
//        Intent intent1 = new Intent(this, SplashScreen.class);
//        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent1 = PendingIntent.getActivity(
//                this,
//                0,
//                intent1,
//                PendingIntent.FLAG_IMMUTABLE
//        );
//
//        // Создание интента для действия кнопки 2
//        Intent intent2 = new Intent(this, SplashScreen.class);
//        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent2 = PendingIntent.getActivity(
//                this,
//                0,
//                intent2,
//                PendingIntent.FLAG_IMMUTABLE
//        );
//
//        NotificationCompat.Builder builder =
//                new NotificationCompat.Builder(Drafts.this, CHANNEL_ID)
//                        .setSmallIcon(R.drawable.icon)
//                        .setContentTitle("Напоминание")
//                        .setContentText("Пора покормить кота")
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                        .addAction(R.drawable.icon, "Покормить", pendingIntent1)
//                        // Добавление кнопки 2
//                        .addAction(R.drawable.icon, "Отложить", pendingIntent2);;
//
//        NotificationManagerCompat notificationManager =
//                NotificationManagerCompat.from(this);
//        notificationManager.notify(NOTIFY_ID, builder.build());
//    }
//
//    private static final int PICK_IMAGE_REQUEST = 1;
//    private ImageView imageView;
//    private Uri imageUri;
//    private StorageReference storageReference;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.test);
//        storageReference = FirebaseStorage.getInstance().getReference();
//
//        imageView = findViewById(R.id.qrImage);
//
//        // Находим кнопку по ID и устанавливаем на нее обработчик нажатия
//        findViewById(R.id.openBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Вызываем метод выбора изображения из галереи
//                chooseImage();
//            }
//        });
//
//        Button openDialogButton = findViewById(R.id.btnDialog);
//        openDialogButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openMyDialog();
//            }
//        });
//    }
//
//    private void openMyDialog() {
//        MyDialogFragment dialogFragment = new MyDialogFragment();
//        dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
//    }
//
//
//    private void chooseImage() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), PICK_IMAGE_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            imageUri = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                imageView.setImageBitmap(bitmap);
//                uploadImage();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void uploadImage() {
//        if (imageUri != null) {
//            StorageReference fileReference = storageReference.child("qrcodes/" + System.currentTimeMillis() + ".jpg");
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                byte[] data = baos.toByteArray();
//
//                UploadTask uploadTask = fileReference.putBytes(data);
//                uploadTask.addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(TestActivity.this, "Ошибка при загрузке изображения", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        // Получаем URL загруженного изображения
//                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                String imageUrl = uri.toString();
//                                // После успешной загрузки изображения вызываем метод для генерации QR-кода
//                                generateQRCode(imageUrl);
//                            }
//                        });
//                    }
//                });
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            Toast.makeText(this, "Не выбрано изображение", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void generateQRCode(String url) {
//        try {
//            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
//            BitMatrix bitMatrix = multiFormatWriter.encode(url, BarcodeFormat.QR_CODE, 500, 500);
//            int width = bitMatrix.getWidth();
//            int height = bitMatrix.getHeight();
//            Bitmap qrBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    qrBitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
//                }
//            }
//            imageView.setImageBitmap(qrBitmap);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//    }
//}
