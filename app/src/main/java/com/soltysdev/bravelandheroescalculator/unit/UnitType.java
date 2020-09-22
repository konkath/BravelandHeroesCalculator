package com.soltysdev.bravelandheroescalculator.unit;

public enum UnitType {
    Warrior(1),
    Defender(1 << 1),
    Marksman(1 << 2),
    Mage(1 << 3);

    public int mask;

    UnitType(int mask) {
        this.mask = mask;
    }

    public static int getFullMask() {
        return (1 << 4) - 1;
    }

    static UnitType get(int mask) {
        for (UnitType type : values()) {
            if (type.mask == mask) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid Unit Type for : " + mask);
    }
}
