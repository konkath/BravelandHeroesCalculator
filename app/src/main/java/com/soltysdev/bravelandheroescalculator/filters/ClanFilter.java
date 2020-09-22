package com.soltysdev.bravelandheroescalculator.filters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.soltysdev.bravelandheroescalculator.R;
import com.soltysdev.bravelandheroescalculator.unit.UnitClan;

public final class ClanFilter extends Filter {
    public ClanFilter(Context ctx, ViewGroup root, int viewId) {
        super(UnitClan.getFullMask());

        LinearLayout layout = (LinearLayout) LayoutInflater.from(ctx)
                .inflate(viewId, root, false);
        root.addView(layout);

        layout.findViewById(R.id.filterByBanditButton).setOnClickListener((v) -> onClick(v, UnitClan.Bandit.mask));
        layout.findViewById(R.id.filterByCommanderButton).setOnClickListener((v) -> onClick(v, UnitClan.Commander.mask));
        layout.findViewById(R.id.filterByDeadButton).setOnClickListener((v) -> onClick(v, UnitClan.Dead.mask));
        layout.findViewById(R.id.filterByDemonButton).setOnClickListener((v) -> onClick(v, UnitClan.Demon.mask));
        layout.findViewById(R.id.filterByElvesButton).setOnClickListener((v) -> onClick(v, UnitClan.Elves.mask));
        layout.findViewById(R.id.filterByIceMageButton).setOnClickListener((v) -> onClick(v, UnitClan.IceMage.mask));
        layout.findViewById(R.id.filterByNeutralButton).setOnClickListener((v) -> onClick(v, UnitClan.Neutral.mask));
        layout.findViewById(R.id.filterByOrcButton).setOnClickListener((v) -> onClick(v, UnitClan.Orc.mask));
        layout.findViewById(R.id.filterByPirateButton).setOnClickListener((v) -> onClick(v, UnitClan.Pirate.mask));
        layout.findViewById(R.id.filterBySorcererButton).setOnClickListener((v) -> onClick(v, UnitClan.Sorcerer.mask));
    }
}
