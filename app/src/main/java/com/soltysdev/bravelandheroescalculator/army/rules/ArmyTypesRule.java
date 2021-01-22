package com.soltysdev.bravelandheroescalculator.army.rules;

import android.content.Context;
import android.util.Log;

import com.soltysdev.bravelandheroescalculator.R;
import com.soltysdev.bravelandheroescalculator.army.Army;
import com.soltysdev.bravelandheroescalculator.unit.UnitType;

import java.util.ArrayList;
import java.util.Arrays;

import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Operator.Equal;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Operator.GreaterEqual;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Operator.LesserEqual;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Type.DefendersLimit;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Type.MagesLimit;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Type.MarksmenLimit;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Type.WarriorsLimit;

public final class ArmyTypesRule extends Rule {
    private static final int MAX_TYPES = 5;
    private static final int MIN_TYPES = 0;

    ArmyTypesRule() {
        TAG = ArmyTypesRule.class.getSimpleName();

        mAvailableTypes.addAll(Arrays.asList(WarriorsLimit, DefendersLimit, MarksmenLimit, MagesLimit));

        mAvailableOperators.put(WarriorsLimit, new ArrayList<>(Arrays.asList(LesserEqual, Equal, GreaterEqual)));
        mAvailableOperators.put(DefendersLimit, new ArrayList<>(Arrays.asList(LesserEqual, Equal, GreaterEqual)));
        mAvailableOperators.put(MarksmenLimit, new ArrayList<>(Arrays.asList(LesserEqual, Equal, GreaterEqual)));
        mAvailableOperators.put(MagesLimit, new ArrayList<>(Arrays.asList(LesserEqual, Equal, GreaterEqual)));

        mBounds.put(WarriorsLimit, new ValueBound(MIN_TYPES, MAX_TYPES, EMPTY_VALUE));
        mBounds.put(DefendersLimit, new ValueBound(MIN_TYPES, MAX_TYPES, EMPTY_VALUE));
        mBounds.put(MarksmenLimit, new ValueBound(MIN_TYPES, MAX_TYPES, EMPTY_VALUE));
        mBounds.put(MagesLimit, new ValueBound(MIN_TYPES, MAX_TYPES, EMPTY_VALUE));
    }

    @Override
    protected int getValue(Army army, Type type) {
        return army.getUnitTypeQuantity(translateKey(type));
    }

    @Override
    public String getDescription(Context context) {
        return context.getResources().getString(R.string.rule_army_types);
    }

    private UnitType translateKey(Type type) {
        switch (type) {
            case WarriorsLimit:
                return UnitType.Warrior;
            case DefendersLimit:
                return UnitType.Defender;
            case MarksmenLimit:
                return UnitType.Marksman;
            case MagesLimit:
                return UnitType.Mage;
            default:
                Log.e(TAG, "Unsupported key provided - get ready for crash!");
        }
        return null;
    }
}
