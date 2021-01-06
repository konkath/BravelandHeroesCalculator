package com.soltysdev.bravelandheroescalculator;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public abstract class KeyboardCaptureActivity extends AppCompatActivity {

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            final View view = getCurrentFocus();

            if (view != null) {
                final boolean consumed = super.dispatchTouchEvent(ev);

                final View tmpView = getCurrentFocus();
                final View newView = tmpView != null ? tmpView : view;

                if (newView.equals(view)) {
                    final Rect rect = new Rect();
                    final int[] coordinates = new int[2];

                    view.getLocationOnScreen(coordinates);
                    rect.set(coordinates[0], coordinates[1], coordinates[0] + view.getWidth(),
                            coordinates[1] + view.getHeight());

                    if (rect.contains((int) ev.getX(), (int) ev.getY())) {
                        return consumed;
                    }
                } else if (newView instanceof EditText) {
                    return consumed;
                }

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(newView.getWindowToken(), 0);
                }

                newView.clearFocus();
                return consumed;
            }
        }

        return super.dispatchTouchEvent(ev);
    }
}
