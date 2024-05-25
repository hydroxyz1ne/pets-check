package com.example.petscheck;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MyDialogFragment extends DialogFragment {
    // Поля для работы с базой данных Firebase
    private DatabaseReference databaseReference;
    private Spinner spinner;
    private RatingBar ratingBar;
    private List<String> petNames = new ArrayList<>();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_layout, null);
        builder.setView(dialogView)
                .setPositiveButton("OK", (dialog, id) -> {
                    String selectedPet = spinner.getSelectedItem().toString();
                    float rating = ratingBar.getRating();
                    writeToDatabase(selectedPet, rating);
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    dialog.cancel();
                });

        spinner = dialogView.findViewById(R.id.spinner);
        ratingBar = dialogView.findViewById(R.id.ratingbar);

        DatabaseReference petsRef = FirebaseDatabase.getInstance().getReference("Pet");
        petsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("name").getValue(String.class);
                    if (name != null) {
                        petNames.add(name);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item, petNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                Log.d("MyLog", "Получено " + petNames.size() + " имен питомцев из базы данных Firebase");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("MyLog", "Получено " + petNames.size() + " имен питомцев из базы данных Firebase");
            }
        });

        ratingBar.setNumStars(5);
        ratingBar.setStepSize(1);

        return builder.create();
    }

    private void writeToDatabase(String selectedPet, float rating) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Pet");
        databaseRef.orderByChild("name").equalTo(selectedPet).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String petKey = snapshot.getKey();

                    Calendar calendar = Calendar.getInstance();

                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    String currentTime = timeFormat.format(calendar.getTime());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                    String currentDate = dateFormat.format(calendar.getTime());

                    // Получаем значения name, age, weight и breed из снимка
                    String name = snapshot.child("name").getValue(String.class);
                    String age = snapshot.child("age").getValue(String.class);
                    String weight = snapshot.child("weight").getValue(String.class);
                    String breed = snapshot.child("breed").getValue(String.class);
                    String visit = snapshot.child("visit").getValue(String.class);
                    String imageId = snapshot.child("imageId").getValue(String.class);
                    String qrcode = "https://firebasestorage.googleapis.com/v0/b/petscheckapp.appspot.com/o/qrcodes%2Fpustoy.png?alt=media&token=3cc48665-c007-4739-8b5e-4ac4ce34a0cf";

                    // Создаем новый список объектов Toilet
                    ArrayList<Toilet> toiletsList = new ArrayList<>();
                    ArrayList<Weights> weights = new ArrayList<>();
                    toiletsList.add(new Toilet(currentTime,currentDate, selectedPet, rating));

                    // Получаем текущий список toilets из снимка, если он существует
                    DataSnapshot toiletsSnapshot = snapshot.child("toilets");
                    if (toiletsSnapshot.exists()) {
                        // Добавляем существующие записи в новый список
                        for (DataSnapshot toiletSnapshot : toiletsSnapshot.getChildren()) {
                            String toiletKey = toiletSnapshot.getKey();
                            Toilet toilet = toiletSnapshot.getValue(Toilet.class);
                            if (toilet != null) {
                                toiletsList.add(toilet);
                            }
                        }
                    }

                    // Создаем объект Pets с новым списком toilets
                    Pets pet = new Pets(snapshot.child("id").getValue(String.class),
                            name, age, weights, breed, visit, imageId, toiletsList,qrcode);

                    // Обновляем данные в базе данных Firebase
                    databaseRef.child(petKey).setValue(pet);
                    Log.d("MyLog", "Питомец: " + selectedPet + ", рейтинг: " + rating);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("MyLog", "Питомец: " + selectedPet + ", рейтинг: " + rating);
            }
        });
    }
}
