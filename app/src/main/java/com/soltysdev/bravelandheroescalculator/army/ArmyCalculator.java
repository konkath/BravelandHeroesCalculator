package com.soltysdev.bravelandheroescalculator.army;

import com.soltysdev.bravelandheroescalculator.army.rules.RuleManager;
import com.soltysdev.bravelandheroescalculator.unit.Unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

final class ArmyCalculator {
    private static final int MAX_STAR_UNIT = 5;

    private HashMap<Integer, ArrayList<Unit>> mBestUnits;
    private RuleManager mRuleManager;

    ArmyCalculator(ArrayList<Unit> units) {
        mBestUnits = new HashMap<>();
        mRuleManager = RuleManager.getInstance();

        for (int i = 0; i <= MAX_STAR_UNIT; i++) {
            mBestUnits.put(i, new ArrayList<>());
        }

        units.forEach(unit -> Objects.requireNonNull(mBestUnits.get(unit.getStars())).add(unit));
        mBestUnits.forEach((k, v) -> v.sort(Unit.ByAttack));
    }

    ArrayList<Army> calculate() {
        ArmyPermutations armyPermutations = new ArmyPermutations();
        List<List<Integer>> perms = armyPermutations.get();

        HashMap<Integer, ArrayList<Unit>> filteredUnits = mBestUnits;
        ArrayList<Army> generatedArmies = new ArrayList<>();

        perms.forEach(perm -> {
            List<Integer> usedIndexes = Arrays.asList(0, 0, 0, 0, 0, 0);
            ArrayList<Unit> armyUnits = new ArrayList<>();

            perm.forEach(unitStars -> {
                int unitIdx = usedIndexes.get(unitStars);
                usedIndexes.set(unitStars, unitIdx + 1);

                ArrayList<Unit> units = filteredUnits.get(unitStars);
                if (units != null && units.size() > unitIdx) {
                    Unit unit = units.get(unitIdx);
                    if (unit.getQuantity() > 0) {
                        armyUnits.add(unit);
                    }
                }
            });

            if (!armyUnits.isEmpty()) {
                Army army = new Army(armyUnits);
                if (!generatedArmies.contains(army) && mRuleManager.isArmyValid(army)) {
                    generatedArmies.add(army);
                }
            }
        });

        generatedArmies.sort(Army.ByAttack);
        return generatedArmies;
    }
}
