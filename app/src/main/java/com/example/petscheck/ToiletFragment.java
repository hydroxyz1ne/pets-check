package com.example.petscheck;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToiletFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToiletFragment extends Fragment {
    LinearLayout linearToiletContainer;
    DatabaseReference databaseReference;
    private Spinner spinner;

    private Set<String> petNames = new HashSet<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ToiletFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ToiletFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ToiletFragment newInstance(String param1, String param2) {
        ToiletFragment fragment = new ToiletFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_toilet, container, false);
        linearToiletContainer = view.findViewById(R.id.linearToiletContainer);
        databaseReference = FirebaseDatabase.getInstance().getReference("Pet");
        // Загрузка данных из Firebase
        loadPetsFromFirebase();
        spinner = view.findViewById(R.id.spinner);
        // Заполнение спиннера данными из petNames
        setupSpinner();
        return view;
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, new ArrayList<>(petNames)); // Преобразуем множество в список для адаптера
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Log.d("MyLog", "Получено " + petNames.size() + " имен питомцев из базы данных Firebase");
    }

    private void loadPetsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                linearToiletContainer.removeAllViews();
                // Очистка множества перед каждым обновлением данных
                petNames.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot toiletSnapshot : dataSnapshot.child("toilets").getChildren()) {
                        String petName = toiletSnapshot.child("petName").getValue(String.class);
                        Float petKakiFloat = toiletSnapshot.child("kaki").getValue(Float.class);
                        String petKaki = String.valueOf(petKakiFloat);
                        String petTime = toiletSnapshot.child("dateTime").getValue(String.class);

                        Log.d("FirebaseData", "Pet name from Firebase: " + petTime);

                        // Добавление имени в множество
                        petNames.add(petName);

                        // Создаем новое представление каждый раз
                        View toiletCard = getLayoutInflater().inflate(R.layout.toilet_card, null);
                        TextView textViewPetName = toiletCard.findViewById(R.id.toiletPet);
                        TextView textViewPetKaki = toiletCard.findViewById(R.id.toiletCount);
                        TextView textViewPetTime = toiletCard.findViewById(R.id.timeToilet);
                        textViewPetName.setText(petName);
                        textViewPetKaki.setText(petKaki);
                        textViewPetTime.setText(petTime);

                        // Добавляем представление в контейнер
                        linearToiletContainer.addView(toiletCard);
                    }
                }
                // Обновление спиннера после обновления данных
                setupSpinner();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибки при загрузке данных из Firebase
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
}