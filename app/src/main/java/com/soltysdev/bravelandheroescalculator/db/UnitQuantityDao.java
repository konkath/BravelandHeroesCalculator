package com.soltysdev.bravelandheroescalculator.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UnitQuantityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UnitQuantity unitQuantity);

    @Query("SELECT * FROM UnitQuantity WHERE unitName == :unitName")
    UnitQuantity getUnitQuantity(String unitName);
}
