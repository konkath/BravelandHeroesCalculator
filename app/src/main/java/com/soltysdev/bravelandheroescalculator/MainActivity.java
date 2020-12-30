package com.soltysdev.bravelandheroescalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soltysdev.bravelandheroescalculator.army.ArmyActivity;
import com.soltysdev.bravelandheroescalculator.db.UnitQuantity;
import com.soltysdev.bravelandheroescalculator.db.UnitsRepository;
import com.soltysdev.bravelandheroescalculator.filters.ClanFilter;
import com.soltysdev.bravelandheroescalculator.filters.TypeFilter;
import com.soltysdev.bravelandheroescalculator.unit.Unit;
import com.soltysdev.bravelandheroescalculator.unit.UnitAdapter;
import com.soltysdev.bravelandheroescalculator.unit.UnitClan;
import com.soltysdev.bravelandheroescalculator.unit.UnitType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private static final String UNITS_JSON = "units.json";

    private RecyclerView.Adapter mRVAdapter;
    private ArrayList<Unit> mAllUnits;
    private ArrayList<Unit> mShownUnits;
    private UnitsRepository mUnitsRepository;

    private int mTypeFilter = UnitType.getFullMask();
    private int mClanFilter = UnitClan.getFullMask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnitsRepository = new UnitsRepository(getApplicationContext());

        RecyclerView recyclerView = findViewById(R.id.units_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAllUnits = loadUnits();
        mAllUnits.forEach(unit -> {
            unit.setQuantity(mUnitsRepository.getUnitQuantity(unit.getName()));
            unit.updateLanguage(getApplicationContext());
            unit.setOnDatabaseUpdateCallback(quantity -> mUnitsRepository.insert(
                    new UnitQuantity(unit.getName(), quantity)));
        });

        Collections.sort(mAllUnits, Unit.ByName);
        mShownUnits = new ArrayList<>(mAllUnits);
        mRVAdapter = new UnitAdapter(mShownUnits);
        recyclerView.setAdapter(mRVAdapter);

        ClanFilter clanFilter = findViewById(R.id.includeClanFilter);
        clanFilter.setFiltrationChangedCallback(filterMask -> {
            mClanFilter = filterMask;
            populateFilteredUnits();
        });

        TypeFilter typeFilter = findViewById(R.id.includeTypeFilter);
        typeFilter.setFiltrationChangedCallback(filterMask -> {
            mTypeFilter = filterMask;
            populateFilteredUnits();
        });
    }

    public void onCreateArmyClick(View view) {
        Intent intent = new Intent(this, ArmyActivity.class);
        intent.putParcelableArrayListExtra("units", mAllUnits);
        startActivity(intent);
    }

    public void OnSortByNameClick(View view) {
        Collections.sort(mShownUnits, Unit.ByName);
        mRVAdapter.notifyDataSetChanged();
    }

    public void OnSortByAttackClick(View view) {
        Collections.sort(mShownUnits, Unit.ByAttack);
        mRVAdapter.notifyDataSetChanged();
    }

    public void OnSortByHealthClick(View view) {
        Collections.sort(mShownUnits, Unit.ByHealth);
        mRVAdapter.notifyDataSetChanged();
    }

    public void OnSortByClanClick(View view) {
        Collections.sort(mShownUnits, Unit.ByClan);
        mRVAdapter.notifyDataSetChanged();
    }

    private void populateFilteredUnits() {
        mShownUnits.clear();
        for (Unit unit : mAllUnits) {
            if ((unit.getType().mask & mTypeFilter) != 0 &&
                    (unit.getClan().mask & mClanFilter) != 0) {
                mShownUnits.add(unit);
            }
        }
        mRVAdapter.notifyDataSetChanged();
    }

    private ArrayList<Unit> loadUnits() {
        String jsonUnits = loadJSONFromAsset();
        Gson gson = new Gson();
        return gson.fromJson(jsonUnits, new TypeToken<ArrayList<Unit>>() {
        }.getType());
    }

    private String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getAssets().open(UNITS_JSON);
            int size = is.available();
            byte[] buffer = new byte[size];

            int bytesRead = is.read(buffer);
            if ((bytesRead != size)) throw new AssertionError();

            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
