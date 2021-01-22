package com.soltysdev.bravelandheroescalculator.army.rules;

import android.content.Context;
import android.util.Log;

import com.soltysdev.bravelandheroescalculator.R;
import com.soltysdev.bravelandheroescalculator.army.Army;

import java.util.ArrayList;
import java.util.Arrays;

import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Operator.Equal;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Operator.GreaterEqual;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Operator.LesserEqual;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Type.StarLimit;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Type.UnitLimit;

public final class ArmyLimitRule extends Rule {
    private static final int MAX_STARS = 15;
    private static final int MIN_STARS = 1;
    private static final int MAX_UNITS = 5;
    private static final int MIN_UNITS = 1;

    ArmyLimitRule() {
        TAG = ArmyLimitRule.class.getSimpleName();

        mAvailableTypes.addAll(Arrays.asList(StarLimit, UnitLimit));
        mAvailableOperators.put(StarLimit, new ArrayList<>(Arrays.asList(LesserEqual, Equal, GreaterEqual)));
        mAvailableOperators.put(UnitLimit, new ArrayList<>(Arrays.asList(LesserEqual, Equal, GreaterEqual)));

        mBounds.put(StarLimit, new ValueBound(MIN_STARS, MAX_STARS, EMPTY_VALUE));
        mBounds.put(UnitLimit, new ValueBound(MIN_UNITS, MAX_UNITS, EMPTY_VALUE));
    }

    @Override
    protected int getValue(Army army, Type type) {
        switch (type) {
            case StarLimit:
                return army.getStars();
            case UnitLimit:
                return army.getSize();
            default:
                Log.e(TAG, "Unsupported key provided - improvise!");
        }
        return EMPTY_VALUE;
    }

    @Override
    public String getDescription(Context context) {
        return context.getResources().getString(R.string.rule_army_limit);
    }
}
