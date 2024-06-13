package com.example.petscheck;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.squareup.picasso.Picasso;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PetActivity extends AppCompatActivity {
    private ImageView imageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView qrImage;
    private Uri imageUri;
    private StorageReference storageReference;
    private StorageReference storageReferenceQr;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_pet);

        imageView = findViewById(R.id.imageView);
        qrImage = findViewById(R.id.qrImage);
        Button openBtn = findViewById(R.id.openBtn);
        storageReference = FirebaseStorage.getInstance().getReference();

        storageReferenceQr = FirebaseStorage.getInstance().getReference("qrcodes");
        databaseReference = FirebaseDatabase.getInstance().getReference("QRCode");


        // Загрузка данных питомца из intent
        Intent intent = getIntent();
        String petKey = intent.getStringExtra("PET_KEY");
        String petName = intent.getStringExtra("PET_NAME");
        String petWeight = intent.getStringExtra("PET_WEIGHT") + " кг";
        String petBreed = "Порода: " + intent.getStringExtra("PET_BREED");
        String petLastVisit = getIntent().getStringExtra("PET_VISIT");

        TextView countdownTextView = findViewById(R.id.lastVisitInfo);
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

        openBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        loadQRCode();
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            generateQRCode();
        }
    }

    private void generateQRCode() {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap qrBitmap = encoder.encodeBitmap(bitmap.toString(), com.google.zxing.BarcodeFormat.QR_CODE, 500, 500);
            qrImage.setImageBitmap(qrBitmap);
            uploadQRCode(qrBitmap);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при генерации QR кода", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadQRCode(Bitmap qrBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        qrBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference fileReference = storageReferenceQr.child(System.currentTimeMillis() + ".jpg");

        fileReference.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                databaseReference.setValue(uri.toString());
                                Toast.makeText(PetActivity.this, "QR код успешно загружен", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PetActivity.this, "Ошибка при загрузке QR кода", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadQRCode() {
        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                String qrCodeUrl = task.getResult().getValue(String.class);
                if (qrCodeUrl != null) {
                    Picasso.get().load(qrCodeUrl).into(qrImage);
                }
            } else {
                Toast.makeText(PetActivity.this, "Ошибка при загрузке QR кода", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
