package com.soltysdev.bravelandheroescalculator.unit;

public enum UnitClan {
    Bandit(1),
    Commander(1 << 1),
    Demon(1 << 2),
    Sorcerer(1 << 3),
    IceMage(1 << 4),
    Dead(1 << 5),
    Neutral(1 << 6),
    Orc(1 << 7),
    Pirate(1 << 8),
    Elves(1 << 9);

    public int mask;

    UnitClan(int mask) {
        this.mask = mask;
    }

    public static int getFullMask() {
        return (1 << 10) - 1;
    }

    static UnitClan get(int mask) {
        for (UnitClan unitClan : values()) {
            if (unitClan.mask == mask) {
                return unitClan;
            }
        }
        throw new IllegalArgumentException("Invalid Clan Type for : " + mask);
    }
}
