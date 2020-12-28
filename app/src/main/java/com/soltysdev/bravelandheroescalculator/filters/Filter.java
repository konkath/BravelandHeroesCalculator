package com.soltysdev.bravelandheroescalculator.filters;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

class Filter extends LinearLayout {

    private OnFiltrationChangedCallback mFiltrationCallback;
    private ColorMatrixColorFilter grayFilter;
    private int mFilterMask;

    Filter(Context ctx, AttributeSet attributeSet, int filterMask) {
        super(ctx, attributeSet);
        mFilterMask = filterMask;

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  // grayscale
        grayFilter = new ColorMatrixColorFilter(matrix);
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
        ImageView imgView = (ImageView) view;

        if ((mFilterMask & unitMask) == unitMask) {
            mFilterMask &= ~unitMask;
            imgView.setColorFilter(grayFilter);
            imgView.setImageAlpha(128);
        } else {
            mFilterMask |= unitMask;
            imgView.clearColorFilter();
            imgView.setImageAlpha(255);
        }

        return mFilterMask;
    }
}
