package com.soltysdev.bravelandheroescalculator.army.rules;

import android.content.Context;
import android.util.Log;

import com.soltysdev.bravelandheroescalculator.R;
import com.soltysdev.bravelandheroescalculator.army.Army;
import com.soltysdev.bravelandheroescalculator.unit.UnitClan;

import java.util.ArrayList;
import java.util.Arrays;

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
    private static final int MAX_CLANS = 5;
    private static final int MIN_CLANS = 0;

    ArmyClansRule() {
        TAG = ArmyClansRule.class.getSimpleName();

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

        mBounds.put(BanditsLimit, new ValueBound(MIN_CLANS, MAX_CLANS, EMPTY_VALUE));
        mBounds.put(CommandersLimit, new ValueBound(MIN_CLANS, MAX_CLANS, EMPTY_VALUE));
        mBounds.put(DemonsLimit, new ValueBound(MIN_CLANS, MAX_CLANS, EMPTY_VALUE));
        mBounds.put(SorcerersLimit, new ValueBound(MIN_CLANS, MAX_CLANS, EMPTY_VALUE));
        mBounds.put(IceMagesLimit, new ValueBound(MIN_CLANS, MAX_CLANS, EMPTY_VALUE));
        mBounds.put(DeadLimit, new ValueBound(MIN_CLANS, MAX_CLANS, EMPTY_VALUE));
        mBounds.put(NeutralLimit, new ValueBound(MIN_CLANS, MAX_CLANS, EMPTY_VALUE));
        mBounds.put(OrcLimit, new ValueBound(MIN_CLANS, MAX_CLANS, EMPTY_VALUE));
        mBounds.put(PiratesLimit, new ValueBound(MIN_CLANS, MAX_CLANS, EMPTY_VALUE));
        mBounds.put(ElvesLimit, new ValueBound(MIN_CLANS, MAX_CLANS, EMPTY_VALUE));
    }

    @Override
    protected int getValue(Army army, Type type) {
        return army.getUnitClanQuantity(translateKey(type));
    }

    @Override
    public String getDescription(Context context) {
        return context.getResources().getString(R.string.rule_army_clans);
    }

    private UnitClan translateKey(Type type) {
        switch (type) {
            case BanditsLimit:
                return UnitClan.Bandit;
            case CommandersLimit:
                return UnitClan.Commander;
            case DemonsLimit:
                return UnitClan.Demon;
            case SorcerersLimit:
                return UnitClan.Sorcerer;
            case IceMagesLimit:
                return UnitClan.IceMage;
            case DeadLimit:
                return UnitClan.Dead;
            case NeutralLimit:
                return UnitClan.Neutral;
            case OrcLimit:
                return UnitClan.Orc;
            case PiratesLimit:
                return UnitClan.Pirate;
            case ElvesLimit:
                return UnitClan.Elves;
            default:
                Log.e(TAG, "Unsupported key provided - get ready for crash!");
        }
        return null;
    }
}
