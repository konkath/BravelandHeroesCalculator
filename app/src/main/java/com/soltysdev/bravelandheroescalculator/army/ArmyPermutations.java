package com.soltysdev.bravelandheroescalculator.army;

import java.util.ArrayList;
import java.util.List;

final class ArmyPermutations {
    private static final int SINGLE_UNIT_MAX_STAR = 5;

    static List<List<Integer>> get(int maxStars, int maxUnits) {
        List<List<Integer>> perms = new ArrayList<>();
        buildPerm(perms, new ArrayList<>(), maxStars, SINGLE_UNIT_MAX_STAR, maxUnits);
        return perms;
    }

    private static void buildPerm(List<List<Integer>> allPerm, List<Integer> currPerm,
                                  int maxStars, int starIdx, int maxUnits) {
        while (starIdx >= 0) {
            int sum = currPerm.stream().mapToInt(x -> x).sum();
            if (sum + starIdx <= maxStars) {
                // Avoid producing permutations of armies without reaching star capacity filled with 0s
                // Even 1 star unit is better than no unit at all
                if (starIdx == 0 && sum + starIdx != maxStars) {
                    return;
                }

                ArrayList<Integer> newPerm = new ArrayList<>(currPerm);
                newPerm.add(starIdx);

                // Finish when army size limit is reached
                if (newPerm.size() == maxUnits) {
                    allPerm.add(newPerm);
                    return;
                }

                // recursive permutation building
                buildPerm(allPerm, newPerm, maxStars, starIdx, maxUnits);
            }
            starIdx--;
        }
    }
}
