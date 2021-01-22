package com.soltysdev.bravelandheroescalculator.army.rules;

final class ValueBound {
    private int mLowerBound;
    private int mUpperBound;
    private int mDefaultLowerBound;
    private int mDefaultUpperBound;
    private int mEmptyValue;

    ValueBound(int lowerBound, int upperBound, int emptyValue) {
        mLowerBound = lowerBound;
        mUpperBound = upperBound;
        mDefaultLowerBound = lowerBound;
        mDefaultUpperBound = upperBound;
        mEmptyValue = emptyValue;
    }

    int getUpperBound() {
        return mUpperBound;
    }

    void setUpperBound(int max) {
        mUpperBound = (max == mEmptyValue ? mDefaultUpperBound : max);
    }

    int getLowerBound() {
        return mLowerBound;
    }

    void setLowerBound(int min) {
        mLowerBound = (min == mEmptyValue ? mDefaultLowerBound : min);
    }

    boolean isValueInRange(int value) {
        return mUpperBound >= value && mLowerBound <= value;
    }
}
