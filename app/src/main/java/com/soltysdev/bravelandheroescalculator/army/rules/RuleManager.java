package com.soltysdev.bravelandheroescalculator.army.rules;

import java.util.ArrayList;

public final class RuleManager {

    private static RuleManager INSTANCE = null;

    private ArmyLimitRule mArmyLimitRule;

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
        mArmyLimitRule = new ArmyLimitRule();
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

        if (mArmyLimitRule.isEditable()) {
            editableRules.add(mArmyLimitRule);
        }

        return editableRules;
    }

    public ArmyLimitRule getArmyLimitRule() {
        return mArmyLimitRule;
    }
}
