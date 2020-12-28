package com.soltysdev.bravelandheroescalculator.army;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.soltysdev.bravelandheroescalculator.R;
import com.soltysdev.bravelandheroescalculator.filters.ClanFilter;
import com.soltysdev.bravelandheroescalculator.filters.TypeFilter;
import com.soltysdev.bravelandheroescalculator.unit.Unit;
import com.soltysdev.bravelandheroescalculator.unit.UnitClan;
import com.soltysdev.bravelandheroescalculator.unit.UnitType;
import com.soltysdev.bravelandheroescalculator.unit.UnitViewHolder;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ArmyActivity extends AppCompatActivity {

    private static final String TAG = ArmyActivity.class.getSimpleName();

    private ArmyCalculator mCalculator;
    private ArrayList<Army> mArmies;
    private Button mNextArmyButton;
    private Button mPreviousArmyButton;
    private int mArmyIdx;
    private int mArmyStars;

    private int mTypeFilter = UnitType.getFullMask();
    private int mClanFilter = UnitClan.getFullMask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_army);

        ArrayList<Unit> units = getIntent().getParcelableArrayListExtra("units");
        mCalculator = new ArmyCalculator(units);

        mNextArmyButton = findViewById(R.id.nextArmyButton);
        mNextArmyButton.setEnabled(false);
        mPreviousArmyButton = findViewById(R.id.previousArmyButton);
        mPreviousArmyButton.setEnabled(false);

        ClanFilter clanFilter = findViewById(R.id.includeArmyClanFilter);
        clanFilter.setFiltrationChangedCallback(filterMask -> {
            mClanFilter = filterMask;
            calculateArmy();
        });

        TypeFilter typeFilter = findViewById(R.id.includeArmyTypeFilter);
        typeFilter.setFiltrationChangedCallback(filterMask -> {
            mTypeFilter = filterMask;
            calculateArmy();
        });

        EditText editText = findViewById(R.id.army_quantity);
        editText.addTextChangedListener(new ArmyStarsWatcher());
        mArmyStars = getArmyStars(editText.getText());

        calculateArmy();
    }

    void calculateArmy() {
        mArmyIdx = 0;
        mArmies = mCalculator.calculate(mArmyStars, mTypeFilter, mClanFilter);
        refreshNavButtons();
        populateArmy();
    }

    public void onNextArmyClick(View view) {
        mArmyIdx++;
        populateArmy();
        refreshNavButtons();
    }

    public void onPreviousArmyClick(View view) {
        mArmyIdx--;
        populateArmy();
        refreshNavButtons();
    }

    private void populateArmy() {
        if (mArmyIdx < 0 || mArmyIdx > mArmies.size()) {
            Log.w(TAG, "requested to populate army outside of array bounds (" +
                    mArmyIdx + "/" + mArmies.size() + ")!");
            return;
        }

        LinearLayout armyLayout = findViewById(R.id.armyUnits);
        armyLayout.removeAllViews();

        final Army army = mArmies.get(mArmyIdx);
        for (Unit unit : army.getUnits()) {
            ConstraintLayout cLayout = (ConstraintLayout) LayoutInflater.from(this)
                    .inflate(R.layout.view_unit, armyLayout, false);
            UnitViewHolder uv = new UnitViewHolder(cLayout);
            uv.populateUnitHolder(unit);
            armyLayout.addView(cLayout);
        }
    }

    private void refreshNavButtons() {
        mPreviousArmyButton.setEnabled(mArmyIdx != 0);
        mNextArmyButton.setEnabled(mArmyIdx != mArmies.size() - 1);
    }

    private int getArmyStars(Editable editable) {
        String value = editable.toString();
        return value.isEmpty() ? 0 : Integer.parseInt(value);
    }

    class ArmyStarsWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            mArmyStars = getArmyStars(editable);
            calculateArmy();
        }
    }
}
