package com.example.petscheck;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import android.app.DatePickerDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class NewPet extends AppCompatActivity {
    private EditText namePet, agePet, weightPet, breedPet, visitPet;
    private DatabaseReference mDataBase;
    private String PET_KEY = "Pet";
    private ImageView imagePet;
    private StorageReference mStorageRef;
    private Uri uploadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pet);
        init();
    }


    private void init() {
        mStorageRef = FirebaseStorage.getInstance().getReference("imagePets");
        namePet = findViewById(R.id.namePet);
        agePet = findViewById(R.id.agePet);
        weightPet = findViewById(R.id.weightPet);
        breedPet = findViewById(R.id.breedPet);
        visitPet = findViewById(R.id.lasVisit);
        mDataBase = FirebaseDatabase.getInstance().getReference(PET_KEY);
        imagePet = findViewById(R.id.imagePet);

        visitPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
    }

    public void onClickSave(View view){
        if (validateFields()) {
            uploadImage();
        } else {
            if (TextUtils.isEmpty(namePet.getText().toString())) {
                namePet.setError("Введите имя");
            }
            if (TextUtils.isEmpty(agePet.getText().toString())) {
                agePet.setError("Введите возраст");
            }
            if (TextUtils.isEmpty(weightPet.getText().toString())) {
                weightPet.setError("Введите вес");
            }
            if (TextUtils.isEmpty(breedPet.getText().toString())) {
                breedPet.setError("Введите породу");
            }
            if (TextUtils.isEmpty(visitPet.getText().toString())) {
                visitPet.setError("Введите дату последнего визита");
            }
        }
    }

    private boolean validateFields() {
        return !TextUtils.isEmpty(namePet.getText().toString()) &&
                !TextUtils.isEmpty(agePet.getText().toString()) &&
                !TextUtils.isEmpty(weightPet.getText().toString()) &&
                !TextUtils.isEmpty(breedPet.getText().toString()) &&
                !TextUtils.isEmpty(visitPet.getText().toString());
    }

    private void savePet() {
        String id = mDataBase.push().getKey();
        String name = namePet.getText().toString();
        String age = agePet.getText().toString();
        String weight = weightPet.getText().toString();
        String breed = breedPet.getText().toString();
        String visit = visitPet.getText().toString();
        Pets newPet = new Pets(id, name, age, weight, breed, visit, uploadUri.toString());
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(age) && !TextUtils.isEmpty(weight) && !TextUtils.isEmpty(breed) && !TextUtils.isEmpty(visit)) {
            if(id != null)mDataBase.child(id).setValue(newPet);
            startActivity(new Intent(NewPet.this, MainActivity7.class));
        } else {
            Toast.makeText(this, "Пустое поле", Toast.LENGTH_SHORT).show();
        }
    }

    public void showDatePickerDialog(View v) {
        final Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String formattedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        visitPet.setText(formattedDate);
                    }
                },
                currentYear,
                currentMonth,
                currentDay
        );
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void chooseImage(View v) {
        getImage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && data != null && data.getData() != null) {
            if(resultCode == RESULT_OK) {
                Log.d("MyLog","ImageURL:" + data.getData());
                imagePet.setImageURI(data.getData());

            }
        }
    }

    private void uploadImage() {
        Bitmap bitMap = ((BitmapDrawable) imagePet.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] byteArray = baos.toByteArray();
        final StorageReference mRef = mStorageRef.child(System.currentTimeMillis() + "myimage");

        // Загрузка изображения в Firebase Storage
        UploadTask uploadTask = mRef.putBytes(byteArray);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Получение URL загруженного изображения
                mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Сохранение URI изображения и сохранение данных питомца в базу данных
                        uploadUri = uri;
                        savePet();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Обработка ошибок получения URL
                        Toast.makeText(NewPet.this, "Ошибка при загрузке изображения", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Обработка ошибок загрузки изображения в Firebase Storage
                Toast.makeText(NewPet.this, "Ошибка при загрузке изображения", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getImage() {
        Intent intentChooser = new Intent();
        intentChooser.setType("image/*");
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentChooser,1);
    }
}