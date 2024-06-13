package com.example.petscheck;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TrackerFragment extends Fragment {
    private AnyChartView anyChartView;
    private Cartesian cartesian;
    private List<DataEntry> seriesData;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "WeightPrefs";
    private static final String WEIGHTS_KEY = "weights";
    private Set set;
    private Mapping series1Mapping;
    private DatabaseReference petsRef;
    private List<String> petNames = new ArrayList<>();

    public TrackerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        petsRef = FirebaseDatabase.getInstance().getReference("Pet");
        loadPetNames();
    }

    private void loadPetNames() {
        petsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                petNames.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("name").getValue(String.class);
                    if (name != null) {
                        petNames.add(name);
                    }
                }
                updateGraph();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load pet names.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracker, container, false);
        sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        anyChartView = view.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));

        Button addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddWeightDialog();
            }
        });

        cartesian = AnyChart.line();
        cartesian.animation(true);
        cartesian.crosshair().enabled(true);
        cartesian.crosshair().yLabel(true).yStroke((Stroke) null, null, null, (String) null, (String) null);
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        seriesData = loadWeights();
        set = Set.instantiate();
        set.data(seriesData);
        series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Brandy");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers().type(MarkerType.CIRCLE).size(4d);
        series1.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5d).offsetY(5d);

        anyChartView.setChart(cartesian);

        return view;
    }

    private void showAddWeightDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog_weight, (ViewGroup) getView(), false);
        final EditText input = viewInflated.findViewById(R.id.input);
        Spinner spinner = viewInflated.findViewById(R.id.pet_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, petNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            dialog.dismiss();
            String weightStr = input.getText().toString();
            String petName = spinner.getSelectedItem().toString();
            if (!weightStr.isEmpty()) {
                double newWeight = Double.parseDouble(weightStr);
                addWeight(newWeight, petName);
            } else {
                Toast.makeText(getContext(), "Weight cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void addWeight(double newWeight, String petName) {
        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // Добавляем данные в SharedPreferences
        seriesData.add(new CustomDataEntry(dateTime, newWeight));
        saveWeights(seriesData);

        // Обновление графика
        updateGraph();
    }

    private void updateGraph() {
        cartesian.removeAllSeries();

        set.data(seriesData);
        series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Питомец: " + petNames.get(0));
        series1.hovered().markers().enabled(true);
        series1.hovered().markers().type(MarkerType.CIRCLE).size(4d);
        series1.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5d).offsetY(5d);

        anyChartView.setChart(cartesian); // Обновление графика
    }

    private void saveWeights(List<DataEntry> seriesData) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(seriesData);
        editor.putString(WEIGHTS_KEY, json);
        editor.apply();
    }

    private List<DataEntry> loadWeights() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(WEIGHTS_KEY, null);
        Type type = new TypeToken<ArrayList<CustomDataEntry>>() {}.getType();
        List<CustomDataEntry> weights = gson.fromJson(json, type);
        if (weights == null) {
            weights = new ArrayList<>();
        }
        return new ArrayList<>(weights);
    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value) {
            super(x, value);
        }
    }
}
