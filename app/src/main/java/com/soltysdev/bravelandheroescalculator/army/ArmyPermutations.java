package com.soltysdev.bravelandheroescalculator.army;

import com.soltysdev.bravelandheroescalculator.army.rules.RuleManager;

import java.util.ArrayList;
import java.util.List;

final class ArmyPermutations {
    private static final int SINGLE_UNIT_MAX_STAR = 5;

    private RuleManager.ArmyLimits mArmyLimits;

    ArmyPermutations() {
        mArmyLimits = RuleManager.getInstance().getArmyLimits();
    }

    List<List<Integer>> get() {
        List<List<Integer>> perms = new ArrayList<>();
        buildPerm(perms, new ArrayList<>(), SINGLE_UNIT_MAX_STAR);
        return perms;
    }

    private void buildPerm(List<List<Integer>> allPerm, List<Integer> currPerm, int starIdx) {
        while (starIdx >= mArmyLimits.minStars) {
            int sum = currPerm.stream().mapToInt(x -> x).sum();
            if (sum + starIdx <= mArmyLimits.maxStars) {
                // FIXME Not needed?
                // Avoid producing permutations of armies without reaching star capacity filled with 0s
                // Even 1 star unit is better than no unit at all
                if (starIdx == 0 && sum + starIdx != mArmyLimits.maxStars) {
                    return;
                }

                ArrayList<Integer> newPerm = new ArrayList<>(currPerm);
                newPerm.add(starIdx);

                // Finish when army size limit is reached
                if (newPerm.size() == mArmyLimits.maxUnits) {
                    allPerm.add(newPerm);
                    return;
                }

                // recursive permutation building
                buildPerm(allPerm, newPerm, starIdx);
            } else {
                // Produce permutation even if size limit isn't reached
                if (currPerm.size() >= mArmyLimits.minUnits) {
                    allPerm.add(currPerm);
                }
            }
            starIdx--;
        }
    }
}
