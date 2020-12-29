package com.soltysdev.bravelandheroescalculator.army;

import com.soltysdev.bravelandheroescalculator.unit.Unit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

final class Army {

    private ArrayList<Unit> mUnits;
    private int mStars;
    private int mMinAttack;
    private int mMaxAttack;
    private float mAvgAttack;
    private int mHealth;

    Army(ArrayList<Unit> units) {
        mUnits = units;

        mUnits.forEach(unit -> {
            mStars += unit.getStars();
            mMinAttack += unit.getMinAttack();
            mMaxAttack += unit.getMaxAttack();
            mAvgAttack += getAverageAttack();
            mHealth += unit.getHealth();
        });
    }

    ArrayList<Unit> getUnits() {
        return mUnits;
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
