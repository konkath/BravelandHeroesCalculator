package com.soltysdev.bravelandheroescalculator.filters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

class Filter extends LinearLayout {

    private OnFiltrationChangedCallback mFiltrationCallback;
    private int mFilterMask;

    Filter(Context ctx, AttributeSet attributeSet, int filterMask) {
        super(ctx, attributeSet);
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
