package com.soltysdev.bravelandheroescalculator.army.rules;

import android.content.Context;

import com.soltysdev.bravelandheroescalculator.R;
import com.soltysdev.bravelandheroescalculator.army.Army;

import java.util.ArrayList;

public abstract class Rule implements Printable {

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

    public abstract ArrayList<Type> getAvailableTypes();

    public abstract ArrayList<Operator> getAvailableOperators(Type type);

    public abstract boolean isEditable();

    public abstract int getMaxEditableValue(Type type);

    public abstract int getMinEditableValue(Type type);

    public abstract boolean isArmyAcceptable(Army army);

    public abstract void addSubRule(Type type, Operator operator, int value);

    public abstract void removeSubRule(Type type, Operator operator);

    public abstract String getFullDescription(Context context, Type type, Operator operator);
}
