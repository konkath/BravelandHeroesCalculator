package com.soltysdev.bravelandheroescalculator.army;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.soltysdev.bravelandheroescalculator.KeyboardCaptureActivity;
import com.soltysdev.bravelandheroescalculator.R;
import com.soltysdev.bravelandheroescalculator.army.rules.RuleManager;
import com.soltysdev.bravelandheroescalculator.army.rules.layouts.RulesView;
import com.soltysdev.bravelandheroescalculator.unit.Unit;
import com.soltysdev.bravelandheroescalculator.unit.UnitViewHolder;

import java.util.ArrayList;

import androidx.constraintlayout.widget.ConstraintLayout;

public class ArmyActivity extends KeyboardCaptureActivity {

    private static final String TAG = ArmyActivity.class.getSimpleName();

    private ArmyCalculator mCalculator;
    private ArrayList<Army> mArmies;
    private Button mNextArmyButton;
    private Button mPreviousArmyButton;
    private int mArmyIdx;

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

        RuleManager ruleManager = RuleManager.getInstance();
        ruleManager.initRules();

        RulesView rulesView = findViewById(R.id.rulesView);
        rulesView.setOnRuleChangedCallback(this::calculateArmy);

        calculateArmy();
    }

    void calculateArmy() {
        mArmyIdx = 0;
        mArmies = mCalculator.calculate();
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
        LinearLayout armyLayout = findViewById(R.id.armyUnits);
        armyLayout.removeAllViews();

        if (mArmyIdx < 0 || mArmyIdx >= mArmies.size()) {
            Log.w(TAG, "requested to populate army outside of array bounds (" +
                    mArmyIdx + "/" + mArmies.size() + ")!");
            return;
        }

        final Army army = mArmies.get(mArmyIdx);
        for (Unit unit : army.getUnits()) {
            ConstraintLayout cLayout = (ConstraintLayout) LayoutInflater.from(this)
                    .inflate(R.layout.view_unit, armyLayout, false);
            UnitViewHolder uv = new UnitViewHolder(cLayout, false);
            uv.populateUnitHolder(unit);
            armyLayout.addView(cLayout);
        }
    }

    private void refreshNavButtons() {
        if (!mPreviousArmyButton.isEnabled() && mArmyIdx > 0) {
            mPreviousArmyButton.setEnabled(true);
            mPreviousArmyButton.setVisibility(View.VISIBLE);
        } else if (mArmyIdx == 0) {
            mPreviousArmyButton.setEnabled(false);
            mPreviousArmyButton.setVisibility(View.INVISIBLE);
        }

        if (!mNextArmyButton.isEnabled() && mArmyIdx < mArmies.size() - 1) {
            mNextArmyButton.setEnabled(true);
            mNextArmyButton.setVisibility(View.VISIBLE);
        } else if (mArmyIdx == mArmies.size() - 1) {
            mNextArmyButton.setEnabled(false);
            mNextArmyButton.setVisibility(View.INVISIBLE);
        }
    }
}
