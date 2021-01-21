package com.soltysdev.bravelandheroescalculator.army.rules;

import android.content.Context;
import android.util.Log;

import com.soltysdev.bravelandheroescalculator.R;
import com.soltysdev.bravelandheroescalculator.army.Army;
import com.soltysdev.bravelandheroescalculator.unit.UnitClan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Operator.Equal;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Operator.GreaterEqual;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Operator.LesserEqual;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Type.BanditsLimit;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Type.CommandersLimit;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Type.DeadLimit;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Type.DemonsLimit;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Type.ElvesLimit;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Type.IceMagesLimit;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Type.NeutralLimit;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Type.OrcLimit;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Type.PiratesLimit;
import static com.soltysdev.bravelandheroescalculator.army.rules.Rule.Type.SorcerersLimit;

public final class ArmyClansRule extends Rule {
    private static final String TAG = ArmyClansRule.class.getSimpleName();

    private static final int EMPTY_VALUE = -1;

    private ArrayList<Type> mAvailableTypes = new ArrayList<>();
    private HashMap<Type, ArrayList<Operator>> mAvailableOperators = new HashMap<>();
    private HashMap<Type, UnitClanBounds> mClanBounds = new HashMap<>();

    private static class UnitClanBounds {
        private static final int MAX_CLANS = 5;
        private static final int MIN_CLANS = 0;

        private int mUpperBound = MAX_CLANS;
        private int mLowerBound = MIN_CLANS;

        private UnitClan mUnitClan;

        UnitClanBounds(UnitClan unitClan) {
            mUnitClan = unitClan;
        }

        int getUpperBound() {
            return mUpperBound;
        }

        void setUpperBound(int max) {
            mUpperBound = (max == EMPTY_VALUE ? MAX_CLANS : max);
        }

        int getLowerBound() {
            return mLowerBound;
        }

        void setLowerBound(int min) {
            mLowerBound = (min == EMPTY_VALUE ? MIN_CLANS : min);
        }

        boolean isArmyInRange(Army army) {
            return mUpperBound >= army.getUnitClanQuantity(mUnitClan) &&
                    mLowerBound <= army.getUnitClanQuantity(mUnitClan);
        }
    }

    ArmyClansRule() {
        mAvailableTypes.addAll(Arrays.asList(BanditsLimit, CommandersLimit,
                DemonsLimit, SorcerersLimit, IceMagesLimit, DeadLimit,
                NeutralLimit, OrcLimit, PiratesLimit, ElvesLimit));

        mAvailableOperators.put(BanditsLimit, new ArrayList<>(Arrays.asList(LesserEqual, Equal, GreaterEqual)));
        mAvailableOperators.put(CommandersLimit, new ArrayList<>(Arrays.asList(LesserEqual, Equal, GreaterEqual)));
        mAvailableOperators.put(DemonsLimit, new ArrayList<>(Arrays.asList(LesserEqual, Equal, GreaterEqual)));
        mAvailableOperators.put(SorcerersLimit, new ArrayList<>(Arrays.asList(LesserEqual, Equal, GreaterEqual)));
        mAvailableOperators.put(IceMagesLimit, new ArrayList<>(Arrays.asList(LesserEqual, Equal, GreaterEqual)));
        mAvailableOperators.put(DeadLimit, new ArrayList<>(Arrays.asList(LesserEqual, Equal, GreaterEqual)));
        mAvailableOperators.put(NeutralLimit, new ArrayList<>(Arrays.asList(LesserEqual, Equal, GreaterEqual)));
        mAvailableOperators.put(OrcLimit, new ArrayList<>(Arrays.asList(LesserEqual, Equal, GreaterEqual)));
        mAvailableOperators.put(PiratesLimit, new ArrayList<>(Arrays.asList(LesserEqual, Equal, GreaterEqual)));
        mAvailableOperators.put(ElvesLimit, new ArrayList<>(Arrays.asList(LesserEqual, Equal, GreaterEqual)));

        mClanBounds.put(BanditsLimit, new UnitClanBounds(UnitClan.Bandit));
        mClanBounds.put(CommandersLimit, new UnitClanBounds(UnitClan.Commander));
        mClanBounds.put(DemonsLimit, new UnitClanBounds(UnitClan.Demon));
        mClanBounds.put(SorcerersLimit, new UnitClanBounds(UnitClan.Sorcerer));
        mClanBounds.put(IceMagesLimit, new UnitClanBounds(UnitClan.IceMage));
        mClanBounds.put(DeadLimit, new UnitClanBounds(UnitClan.Dead));
        mClanBounds.put(NeutralLimit, new UnitClanBounds(UnitClan.Neutral));
        mClanBounds.put(OrcLimit, new UnitClanBounds(UnitClan.Orc));
        mClanBounds.put(PiratesLimit, new UnitClanBounds(UnitClan.Pirate));
        mClanBounds.put(ElvesLimit, new UnitClanBounds(UnitClan.Elves));
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
        if (!mClanBounds.containsKey(type)) {
            Log.e(TAG, "Max value requested for nonexistent key! " + type);
            return EMPTY_VALUE;
        }

        UnitClanBounds unitClanBounds = mClanBounds.get(type);
        return unitClanBounds != null ? unitClanBounds.getUpperBound() : EMPTY_VALUE;
    }

    @Override
    public int getMinEditableValue(Type type) {
        if (!mClanBounds.containsKey(type)) {
            Log.e(TAG, "Min value requested for nonexistent key! " + type);
            return EMPTY_VALUE;
        }

        UnitClanBounds unitClanBounds = mClanBounds.get(type);
        return unitClanBounds != null ? unitClanBounds.getLowerBound() : EMPTY_VALUE;
    }

    @Override
    public boolean isArmyAcceptable(Army army) {
        return mClanBounds.entrySet().stream().allMatch(
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
        UnitClanBounds unitClanBounds = mClanBounds.get(type);
        if (unitClanBounds == null) {
            Log.e(TAG, "Unsupported type requested " + type + " - ignoring!");
            return;
        }
        unitClanBounds.setUpperBound(value);
    }

    private void updateMin(Type type, int value) {
        UnitClanBounds unitClanBounds = mClanBounds.get(type);
        if (unitClanBounds == null) {
            Log.e(TAG, "Unsupported type requested " + type + " - ignoring!");
            return;
        }
        unitClanBounds.setLowerBound(value);
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
        return context.getResources().getString(R.string.rule_army_clans);
    }
}
