package com.soltysdev.bravelandheroescalculator.army;

import com.soltysdev.bravelandheroescalculator.unit.Unit;
import com.soltysdev.bravelandheroescalculator.unit.UnitType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

public final class Army {

    private ArrayList<Unit> mUnits;
    private int mStars;
    private int mMinAttack;
    private int mMaxAttack;
    private float mAvgAttack;
    private int mHealth;

    private HashMap<UnitType, Integer> mUnitTypes;

    Army(ArrayList<Unit> units) {
        mUnits = units;
        mUnitTypes = new HashMap<>();

        mUnits.forEach(unit -> {
            mStars += unit.getStars();
            mMinAttack += unit.getMinAttack();
            mMaxAttack += unit.getMaxAttack();
            mAvgAttack += getAverageAttack();
            mHealth += unit.getHealth();

            final Integer packedUnitTypes = mUnitTypes.get(unit.getType());
            final int unitTypes = packedUnitTypes != null ? packedUnitTypes : 0;
            mUnitTypes.put(unit.getType(), unitTypes + 1);
        });
    }

    ArrayList<Unit> getUnits() {
        return mUnits;
    }

    public int getSize() {
        return mUnits.size();
    }

    public int getStars() {
        return mStars;
    }

    public int getUnitTypeQuantity(UnitType unitType) {
        final Integer unitTypeQuantity = mUnitTypes.getOrDefault(unitType, 0);
        return unitTypeQuantity != null ? unitTypeQuantity : 0;
    }

    private float getAverageAttack() {
        return (float) (mMaxAttack + mMinAttack) / 2;
    }

    static Comparator<Army> ByAttack = (lhs, rhs) -> Float.compare(rhs.mAvgAttack, lhs.mAvgAttack);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Army army = (Army) o;
        return mStars == army.mStars &&
                mMinAttack == army.mMinAttack &&
                mMaxAttack == army.mMaxAttack &&
                mHealth == army.mHealth &&
                Objects.equals(mUnits, army.mUnits);
    }
}
