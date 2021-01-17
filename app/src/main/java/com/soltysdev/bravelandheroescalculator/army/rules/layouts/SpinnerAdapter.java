package com.soltysdev.bravelandheroescalculator.army.rules.layouts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.soltysdev.bravelandheroescalculator.R;
import com.soltysdev.bravelandheroescalculator.army.rules.Printable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

final class SpinnerAdapter<T> extends ArrayAdapter<T> {
    private ArrayList<T> mObjects;

    SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
        mObjects = (ArrayList<T>) objects;
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public T getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        String text = ((Printable) mObjects.get(position)).getDescription(getContext());
        label.setText(text + "...");
        label.setTextColor(parent.getResources().getColor(R.color.colorFont, null));
        return label;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getDropDownView(int position, View convertView,
                                @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        String text = ((Printable) mObjects.get(position)).getDescription(getContext());
        label.setText(text + "...");
        label.setTextColor(parent.getResources().getColor(R.color.colorFont, null));
        return label;
    }
}
