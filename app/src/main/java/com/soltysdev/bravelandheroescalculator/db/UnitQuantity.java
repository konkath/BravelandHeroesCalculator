package com.soltysdev.bravelandheroescalculator.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UnitQuantity {

    @PrimaryKey
    @NonNull
    private String unitName;
    private int quantity;

    public UnitQuantity(@NonNull String unitName, int quantity) {
        this.unitName = unitName;
        this.quantity = quantity;
    }

    @NonNull
    String getUnitName() {
        return this.unitName;
    }

    int getQuantity() {
        return this.quantity;
    }
}
