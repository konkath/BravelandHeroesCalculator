package com.soltysdev.bravelandheroescalculator.unit;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.soltysdev.bravelandheroescalculator.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class UnitAdapter extends RecyclerView.Adapter<UnitViewHolder> {
    private ArrayList<Unit> mUnits;

    public UnitAdapter(ArrayList<Unit> units) {
        mUnits = units;
    }

    @NonNull
    @Override
    public UnitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_unit, parent, false);
        return new UnitViewHolder(layout, true);
    }

    @Override
    public void onBindViewHolder(UnitViewHolder holder, int position) {
        holder.populateUnitHolder(mUnits.get(position));
    }

    @Override
    public int getItemCount() {
        return mUnits.size();
    }
}
