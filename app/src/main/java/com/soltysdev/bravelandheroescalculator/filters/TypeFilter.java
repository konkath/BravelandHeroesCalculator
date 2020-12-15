package com.soltysdev.bravelandheroescalculator.filters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.soltysdev.bravelandheroescalculator.R;
import com.soltysdev.bravelandheroescalculator.unit.UnitType;

public final class TypeFilter extends Filter {
    public TypeFilter(Context ctx, AttributeSet attributeSet) {
        super(ctx, attributeSet, UnitType.getFullMask());

        LinearLayout layout = (LinearLayout) LayoutInflater.from(ctx)
                .inflate(R.layout.filter_type, this);

        layout.findViewById(R.id.filterByWarriorButton).setOnClickListener((v) -> onClick(v, UnitType.Warrior.mask));
        layout.findViewById(R.id.filterByDefenderButton).setOnClickListener((v) -> onClick(v, UnitType.Defender.mask));
        layout.findViewById(R.id.filterByMarksmanButton).setOnClickListener((v) -> onClick(v, UnitType.Marksman.mask));
        layout.findViewById(R.id.filterByMageButton).setOnClickListener((v) -> onClick(v, UnitType.Mage.mask));
    }
}
