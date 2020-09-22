package com.soltysdev.bravelandheroescalculator.filters;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;

class Filter {

    private OnFiltrationChangedCallback mFiltrationCallback;
    private int mFilterMask;

    Filter(int filterMask) {
        mFilterMask = filterMask;
    }

    public interface OnFiltrationChangedCallback {
        void FiltrationChanged(int filterMask);
    }

    public void setFiltrationChangedCallback(OnFiltrationChangedCallback callback) {
        mFiltrationCallback = callback;
    }

    void onClick(View view, int mask) {
        final int filteredMask = updateFilteredUnits(mask, view);
        if (mFiltrationCallback != null) {
            mFiltrationCallback.FiltrationChanged(filteredMask);
        }
    }

    private int updateFilteredUnits(int unitMask, View view) {
        if ((mFilterMask & unitMask) == unitMask) {
            mFilterMask &= ~unitMask;
            view.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        } else {
            mFilterMask |= unitMask;
            view.getBackground().clearColorFilter();
        }
        return mFilterMask;
    }
}
