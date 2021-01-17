package com.soltysdev.bravelandheroescalculator.army.rules;

import android.content.Context;
import android.util.Log;

import com.soltysdev.bravelandheroescalculator.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Operator.Equal;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Operator.GreaterEqual;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Operator.LesserEqual;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Type.StarLimit;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Type.UnitLimit;

public final class ArmyLimitRule extends Rule {
    private static final String TAG = ArmyLimitRule.class.getSimpleName();
    private static final int EMPTY_VALUE = 0;
    private static final int MAX_STARS = 15;
    private static final int MIN_STARS = 1;
    private static final int MAX_UNITS = 5;
    private static final int MIN_UNITS = 1;

    private int mMaxStars = EMPTY_VALUE;
    private int mMinStars = EMPTY_VALUE;
    private int mMaxUnits = EMPTY_VALUE;
    private int mMinUnits = EMPTY_VALUE;

    private ArrayList<Type> mAvailableTypes = new ArrayList<>();
    private HashMap<Type, ArrayList<Operator>> mAvailableOperators = new HashMap<>();

    ArmyLimitRule() {
        mAvailableTypes.addAll(Arrays.asList(StarLimit, UnitLimit));
        mAvailableOperators.put(StarLimit, new ArrayList<>(Arrays.asList(LesserEqual, Equal, GreaterEqual)));
        mAvailableOperators.put(UnitLimit, new ArrayList<>(Arrays.asList(LesserEqual, Equal, GreaterEqual)));
    }

    @Override
    public String getDescription(Context context) {
        return context.getResources().getString(R.string.rule_army_limit);
    }

    @Override
    public String getFullDescription(Context context, Type type, Operator operator) {
        return this.getDescription(context) + " " +
                type.getDescription(context) + " " +
                operator.getDescription(context) + " " +
                (operator == LesserEqual ? getMaxEditableValue(type) : getMinEditableValue(type));
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
        int max;
        switch (type) {
            case StarLimit:
                max = (mMaxStars == EMPTY_VALUE ? MAX_STARS : mMaxStars);
                break;
            case UnitLimit:
                max = (mMaxUnits == EMPTY_VALUE ? MAX_UNITS : mMaxUnits);
                break;
            default:
                Log.w(TAG, "Unsupported type requested " + type + " - improvising!");
                max = Math.min(MAX_STARS, MAX_UNITS);
        }
        return max;
    }

    @Override
    public int getMinEditableValue(Type type) {
        int min;
        switch (type) {
            case StarLimit:
                min = (mMinStars == EMPTY_VALUE ? MIN_STARS : mMinStars);
                break;
            case UnitLimit:
                min = (mMinUnits == EMPTY_VALUE ? MIN_UNITS : mMinUnits);
                break;
            default:
                Log.w(TAG, "Unsupported type requested " + type + " - improvising!");
                min = Math.max(MIN_STARS, MIN_UNITS);
        }
        return min;
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
        switch (type) {
            case StarLimit:
                mMaxStars = value;
                break;
            case UnitLimit:
                mMaxUnits = value;
                break;
        }
    }

    private void updateMin(Type type, int value) {
        switch (type) {
            case StarLimit:
                mMinStars = value;
                break;
            case UnitLimit:
                mMinUnits = value;
                break;
        }
    }
}
