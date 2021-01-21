package com.soltysdev.bravelandheroescalculator.army.rules;

import com.soltysdev.bravelandheroescalculator.army.Army;

import java.util.ArrayList;

public final class RuleManager {

    private static RuleManager INSTANCE = null;

    private ArmyLimitRule mArmyLimitRule;
    private ArrayList<Rule> mAllRules;

    private RuleManager() {
        mAllRules = new ArrayList<>();
    }

    public static RuleManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RuleManager();
        }
        return INSTANCE;
    }

    public static final class ArmyLimits {
        public int maxStars;
        public int minStars;
        public int maxUnits;
        public int minUnits;
    }

    public void initRules() {
        mAllRules.clear();
        mArmyLimitRule = new ArmyLimitRule();

        mAllRules.add(mArmyLimitRule);
        mAllRules.add(new ArmyTypesRule());
        mAllRules.add(new ArmyClansRule());
    }

    public ArmyLimits getArmyLimits() {
        ArmyLimits armyLimits = new ArmyLimits();
        armyLimits.maxStars = mArmyLimitRule.getMaxEditableValue(Rule.Type.StarLimit);
        armyLimits.minStars = mArmyLimitRule.getMinEditableValue(Rule.Type.StarLimit);
        armyLimits.maxUnits = mArmyLimitRule.getMaxEditableValue(Rule.Type.UnitLimit);
        armyLimits.minUnits = mArmyLimitRule.getMinEditableValue(Rule.Type.UnitLimit);
        return armyLimits;
    }

    public ArrayList<Rule> getEditableRules() {
        ArrayList<Rule> editableRules = new ArrayList<>();
        mAllRules.forEach(rule -> {
            if (rule.isEditable()) {
                editableRules.add(rule);
            }
        });
        return editableRules;
    }

    public boolean isArmyValid(Army army) {
        return mAllRules.stream().allMatch(rule -> rule.isArmyAcceptable(army));
    }
}
