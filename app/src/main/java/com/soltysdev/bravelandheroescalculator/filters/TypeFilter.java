package com.soltysdev.bravelandheroescalculator.filters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.soltysdev.bravelandheroescalculator.R;
import com.soltysdev.bravelandheroescalculator.unit.UnitType;

public final class TypeFilter extends Filter {
    public TypeFilter(Context ctx, ViewGroup root, int viewId) {
        super(UnitType.getFullMask());

        LinearLayout layout = (LinearLayout) LayoutInflater.from(ctx)
                .inflate(viewId, root, false);
        root.addView(layout);

        layout.findViewById(R.id.filterByWarriorButton).setOnClickListener((v) -> onClick(v, UnitType.Warrior.mask));
        layout.findViewById(R.id.filterByDefenderButton).setOnClickListener((v) -> onClick(v, UnitType.Defender.mask));
        layout.findViewById(R.id.filterByMarksmanButton).setOnClickListener((v) -> onClick(v, UnitType.Marksman.mask));
        layout.findViewById(R.id.filterByMageButton).setOnClickListener((v) -> onClick(v, UnitType.Mage.mask));
    }
}
