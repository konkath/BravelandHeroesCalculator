package com.soltysdev.bravelandheroescalculator.army.rules;

import android.content.Context;
import android.util.Log;

import com.soltysdev.bravelandheroescalculator.R;
import com.soltysdev.bravelandheroescalculator.army.Army;
import com.soltysdev.bravelandheroescalculator.unit.UnitType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Operator.Equal;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Operator.GreaterEqual;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Operator.LesserEqual;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Type.DefendersLimit;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Type.MagesLimit;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Type.MarksmenLimit;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Type.WarriorsLimit;

public final class ArmyTypesRule extends Rule {
    private static final String TAG = ArmyTypesRule.class.getSimpleName();

    private static final int EMPTY_VALUE = -1;

    private ArrayList<Type> mAvailableTypes = new ArrayList<>();
    private HashMap<Type, ArrayList<Operator>> mAvailableOperators = new HashMap<>();
    private HashMap<Type, UnitTypeBounds> mTypesBounds = new HashMap<>();

    private static class UnitTypeBounds {
        private static final int MAX_TYPES = 5;
        private static final int MIN_TYPES = 0;

        private int mUpperBound = MAX_TYPES;
        private int mLowerBound = MIN_TYPES;

        private UnitType mUnitType;

        UnitTypeBounds(UnitType unitType) {
            mUnitType = unitType;
        }

        int getUpperBound() {
            return mUpperBound;
        }

        void setUpperBound(int max) {
            mUpperBound = (max == EMPTY_VALUE ? MAX_TYPES : max);
        }

        int getLowerBound() {
            return mLowerBound;
        }

        void setLowerBound(int min) {
            mLowerBound = (min == EMPTY_VALUE ? MIN_TYPES : min);
        }

        boolean isArmyInRange(Army army) {
            return mUpperBound >= army.getUnitTypeQuantity(mUnitType) &&
                    mLowerBound <= army.getUnitTypeQuantity(mUnitType);
        }
    }

    ArmyTypesRule() {
        mAvailableTypes.addAll(Arrays.asList(WarriorsLimit, DefendersLimit, MarksmenLimit, MagesLimit));

        mAvailableOperators.put(WarriorsLimit, new ArrayList<>(Arrays.asList(LesserEqual, Equal, GreaterEqual)));
        mAvailableOperators.put(DefendersLimit, new ArrayList<>(Arrays.asList(LesserEqual, Equal, GreaterEqual)));
        mAvailableOperators.put(MarksmenLimit, new ArrayList<>(Arrays.asList(LesserEqual, Equal, GreaterEqual)));
        mAvailableOperators.put(MagesLimit, new ArrayList<>(Arrays.asList(LesserEqual, Equal, GreaterEqual)));

        mTypesBounds.put(WarriorsLimit, new UnitTypeBounds(UnitType.Warrior));
        mTypesBounds.put(DefendersLimit, new UnitTypeBounds(UnitType.Defender));
        mTypesBounds.put(MarksmenLimit, new UnitTypeBounds(UnitType.Marksman));
        mTypesBounds.put(MagesLimit, new UnitTypeBounds(UnitType.Mage));
    }

    @Override
    public ArrayList<Type> getAvailableTypes() {
        return mAvailableTypes;
    }

    @Override
    public ArrayList<Operator> getAvailableOperators(Type type) {
        return mAvailableOperators.get(type);
    }

    @Override
    public boolean isEditable() {
        return !mAvailableTypes.isEmpty();
    }

    @Override
    public int getMaxEditableValue(Type type) {
        if (!mTypesBounds.containsKey(type)) {
            Log.e(TAG, "Max value requested for nonexistent key! " + type);
            return EMPTY_VALUE;
        }

        UnitTypeBounds unitTypeBounds = mTypesBounds.get(type);
        return unitTypeBounds != null ? unitTypeBounds.getUpperBound() : EMPTY_VALUE;
    }

    @Override
    public int getMinEditableValue(Type type) {
        if (!mTypesBounds.containsKey(type)) {
            Log.e(TAG, "Min value requested for nonexistent key! " + type);
            return EMPTY_VALUE;
        }

        UnitTypeBounds unitTypeBounds = mTypesBounds.get(type);
        return unitTypeBounds != null ? unitTypeBounds.getLowerBound() : EMPTY_VALUE;
    }

    @Override
    public boolean isArmyAcceptable(Army army) {
        return mTypesBounds.entrySet().stream().allMatch(
                entry -> entry.getValue().isArmyInRange(army));
    }

    @Override
    public void addSubRule(Type type, Operator operator, int value) {
        ArrayList<Operator> operators = mAvailableOperators.get(type);
        if (operators == null) {
            Log.e(TAG, "Operator " + operator + " for type " + type + " null. Fatal!");
            return;
        }

        switch (operator) {
            case LesserEqual:
                updateMax(type, value);
                operators.remove(LesserEqual);
                operators.remove(Equal);
                break;
            case Equal:
                updateMax(type, value);
                updateMin(type, value);
                operators.clear();
                break;
            case GreaterEqual:
                updateMin(type, value);
                operators.remove(GreaterEqual);
                operators.remove(Equal);
                break;
            default:
                Log.w(TAG, "Unsupported operator added " + operator + " - skipping!");
        }

        mAvailableOperators.put(type, operators);
        if (operators.isEmpty()) {
            mAvailableTypes.remove(type);
        }
    }

    @Override
    public void removeSubRule(Type type, Operator operator) {
        ArrayList<Operator> operators = mAvailableOperators.get(type);
        if (operators == null) {
            Log.e(TAG, "Operator " + operator + " for type " + type + " null. Fatal!");
            return;
        }

        switch (operator) {
            case LesserEqual:
                updateMax(type, EMPTY_VALUE);
                operators.add(LesserEqual);
                if (operators.contains(GreaterEqual)) {
                    operators.add(Equal);
                }
                break;
            case Equal:
                updateMax(type, EMPTY_VALUE);
                updateMin(type, EMPTY_VALUE);
                operators.addAll(Arrays.asList(LesserEqual, Equal, GreaterEqual));
                break;
            case GreaterEqual:
                updateMin(type, EMPTY_VALUE);
                operators.add(GreaterEqual);
                if (operators.contains(LesserEqual)) {
                    operators.add(Equal);
                }
                break;
            default:
                Log.w(TAG, "Unsupported operator removed " + operator + " - skipping!");
        }

        mAvailableOperators.put(type, operators);
        if (!mAvailableTypes.contains(type)) {
            mAvailableTypes.add(type);
        }
    }

    private void updateMax(Type type, int value) {
        UnitTypeBounds unitTypeBounds = mTypesBounds.get(type);
        if (unitTypeBounds == null) {
            Log.e(TAG, "Unsupported type requested " + type + " - ignoring!");
            return;
        }
        unitTypeBounds.setUpperBound(value);
    }

    private void updateMin(Type type, int value) {
        UnitTypeBounds unitTypeBounds = mTypesBounds.get(type);
        if (unitTypeBounds == null) {
            Log.e(TAG, "Unsupported type requested " + type + " - ignoring!");
            return;
        }
        unitTypeBounds.setLowerBound(value);
    }

    @Override
    public String getFullDescription(Context context, Type type, Operator operator) {
        return this.getDescription(context) + " " +
                type.getDescription(context) + " " +
                operator.getDescription(context) + " " +
                (operator == LesserEqual ? getMaxEditableValue(type) : getMinEditableValue(type));
    }

    @Override
    public String getDescription(Context context) {
        return context.getResources().getString(R.string.rule_army_types);
    }
}
