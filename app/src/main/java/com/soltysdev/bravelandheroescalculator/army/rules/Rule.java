package com.soltysdev.bravelandheroescalculator.army.rules;

import android.content.Context;
import android.util.Log;

import com.soltysdev.bravelandheroescalculator.R;
import com.soltysdev.bravelandheroescalculator.army.Army;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Operator.Equal;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Operator.GreaterEqual;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Operator.LesserEqual;

public abstract class Rule implements Printable {
    String TAG;
    static final int EMPTY_VALUE = -1;

    ArrayList<Type> mAvailableTypes = new ArrayList<>();
    HashMap<Type, ArrayList<Operator>> mAvailableOperators = new HashMap<>();
    HashMap<Type, ValueBound> mBounds = new HashMap<>();

    public enum Operator implements Printable {
        Lesser(R.string.operator_lesser),
        LesserEqual(R.string.operator_lesser_equal),
        Equal(R.string.operator_equal),
        GreaterEqual(R.string.operator_greater_equal),
        Greater(R.string.operator_greater);

        private int resId;

        Operator(int resId) {
            this.resId = resId;
        }

        @Override
        public String getDescription(Context context) {
            return context.getResources().getString(resId);
        }
    }

    public enum Type implements Printable {
        StarLimit(R.string.type_limit_stars),
        UnitLimit(R.string.type_limit_units),
        WarriorsLimit(R.string.type_limit_warriors),
        DefendersLimit(R.string.type_limit_defenders),
        MarksmenLimit(R.string.type_limit_marksmen),
        MagesLimit(R.string.type_limit_mages),
        BanditsLimit(R.string.type_limit_bandits),
        CommandersLimit(R.string.type_limit_commanders),
        DemonsLimit(R.string.type_limit_demons),
        SorcerersLimit(R.string.type_limit_sorcerers),
        IceMagesLimit(R.string.type_limit_ice_mages),
        DeadLimit(R.string.type_limit_dead),
        NeutralLimit(R.string.type_limit_neutral),
        OrcLimit(R.string.type_limit_orc),
        PiratesLimit(R.string.type_limit_pirates),
        ElvesLimit(R.string.type_limit_elves);

        private int resId;

        Type(int resId) {
            this.resId = resId;
        }

        @Override
        public String getDescription(Context context) {
            return context.getResources().getString(resId);
        }
    }

    protected abstract int getValue(Army army, Type type);

    boolean isEditable() {
        return !mAvailableTypes.isEmpty();
    }

    boolean isArmyAcceptable(Army army) {
        return mBounds.entrySet().stream().allMatch(
                entry -> entry.getValue().isValueInRange(
                        getValue(army, entry.getKey())));
    }

    public ArrayList<Type> getAvailableTypes() {
        return mAvailableTypes;
    }

    public ArrayList<Operator> getAvailableOperators(Type type) {
        return mAvailableOperators.get(type);
    }

    public int getMaxEditableValue(Type type) {
        if (!mBounds.containsKey(type)) {
            Log.e(TAG, "Max value requested for nonexistent key! " + type);
            return EMPTY_VALUE;
        }

        ValueBound bound = mBounds.get(type);
        return bound != null ? bound.getUpperBound() : EMPTY_VALUE;
    }

    public int getMinEditableValue(Type type) {
        if (!mBounds.containsKey(type)) {
            Log.e(TAG, "Min value requested for nonexistent key! " + type);
            return EMPTY_VALUE;
        }

        ValueBound bound = mBounds.get(type);
        return bound != null ? bound.getLowerBound() : EMPTY_VALUE;
    }

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

    public String getFullDescription(Context context, Type type, Operator operator) {
        return this.getDescription(context) + " " +
                type.getDescription(context) + " " +
                operator.getDescription(context) + " " +
                (operator == LesserEqual ? getMaxEditableValue(type) : getMinEditableValue(type));
    }

    private void updateMax(Type type, int value) {
        ValueBound bound = mBounds.get(type);
        if (bound == null) {
            Log.e(TAG, "Unsupported type requested " + type + " - ignoring!");
            return;
        }
        bound.setUpperBound(value);
    }

    private void updateMin(Type type, int value) {
        ValueBound bound = mBounds.get(type);
        if (bound == null) {
            Log.e(TAG, "Unsupported type requested " + type + " - ignoring!");
            return;
        }
        bound.setLowerBound(value);
    }
}
