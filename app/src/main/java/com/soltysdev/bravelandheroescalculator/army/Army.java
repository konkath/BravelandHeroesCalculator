package com.soltysdev.bravelandheroescalculator.army;

import com.soltysdev.bravelandheroescalculator.unit.Unit;

import java.util.ArrayList;
import java.util.Comparator;

final class Army {

    private ArrayList<Unit> mUnits;
    private int mStars;
    private int mMinAttack;
    private int mMaxAttack;
    private int mHealth;

    Army(ArrayList<Unit> units) {
        mUnits = units;

        mUnits.forEach(unit -> {
            mStars += unit.getStars();
            mMinAttack += unit.getMinAttack();
            mMaxAttack += unit.getMaxAttack();
            mHealth += unit.getHealth();
        });
    }

    ArrayList<Unit> getUnits() {
        return mUnits;
    }

    private float getAverageAttack() {
        return (float) (mMaxAttack + mMinAttack) / 2;
    }

    static Comparator<Army> ByAttack = (lhs, rhs) -> Float.compare(rhs.getAverageAttack(), lhs.getAverageAttack());
}
