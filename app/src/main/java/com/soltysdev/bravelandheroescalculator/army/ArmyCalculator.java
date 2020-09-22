package com.soltysdev.bravelandheroescalculator.army;

import com.soltysdev.bravelandheroescalculator.unit.Unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

final class ArmyCalculator {
    private static final int MAX_STAR_UNIT = 5;
    private static final int MAX_UNITS_IN_ARMY = 5;

    private HashMap<Integer, ArrayList<Unit>> mBestUnits;

    ArmyCalculator(ArrayList<Unit> units) {
        mBestUnits = new HashMap<>();
        for (int i = 0; i <= MAX_STAR_UNIT; i++) {
            mBestUnits.put(i, new ArrayList<>());
        }

        units.forEach(unit -> Objects.requireNonNull(mBestUnits.get(unit.getStars())).add(unit));
        mBestUnits.forEach((k, v) -> v.sort(Unit.ByAttack));
    }

    ArrayList<Army> calculate(int maxStars, int typeFilter, int clanFilter) {
        List<List<Integer>> perms = ArmyPermutations.get(maxStars, MAX_UNITS_IN_ARMY);
        HashMap<Integer, ArrayList<Unit>> filteredUnits = getFilteredUnits(typeFilter, clanFilter);
        ArrayList<Army> generatedArmies = new ArrayList<>();


        perms.forEach(perm -> {
            List<Integer> usedIndexes = Arrays.asList(0, 0, 0, 0, 0, 0);
            ArrayList<Unit> armyUnits = new ArrayList<>();

            perm.forEach(unitStars -> {
                int unitIdx = usedIndexes.get(unitStars);
                usedIndexes.set(unitStars, unitIdx + 1);

                ArrayList<Unit> units = filteredUnits.get(unitStars);
                if (units != null && units.size() > unitIdx) {
                    armyUnits.add(units.get(unitIdx));
                }
            });

            generatedArmies.add(new Army(armyUnits));
        });

        generatedArmies.sort(Army.ByAttack);
        return generatedArmies;
    }

    private HashMap<Integer, ArrayList<Unit>> getFilteredUnits(int typeFilter, int clanFilter) {
        HashMap<Integer, ArrayList<Unit>> starUnits = new HashMap<>();

        mBestUnits.forEach((k, v) -> {
            ArrayList<Unit> filteredUnits = new ArrayList<>();
            v.forEach(unit -> {
                if ((unit.getType().mask & typeFilter) != 0 &&
                        (unit.getClan().mask & clanFilter) != 0) {
                    filteredUnits.add(unit);
                }
            });
            starUnits.put(k, filteredUnits);
        });

        return starUnits;
    }
}
