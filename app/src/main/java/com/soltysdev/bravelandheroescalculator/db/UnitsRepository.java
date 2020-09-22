package com.soltysdev.bravelandheroescalculator.db;

import android.content.Context;

public class UnitsRepository {
    private UnitQuantityDao mUnitQuantityDao;

    public UnitsRepository(Context context) {
        UnitsDatabase db = UnitsDatabase.getDatabase(context);
        mUnitQuantityDao = db.unitQuantityDao();
    }

    public int getUnitQuantity(String unitName) {
        UnitQuantity unitQuantity = mUnitQuantityDao.getUnitQuantity(unitName);
        return unitQuantity == null ? 0 : unitQuantity.getQuantity();
    }

    public void insert(final UnitQuantity unit) {
        UnitsDatabase.databaseWriteExecutor.execute(() -> mUnitQuantityDao.insert(unit));
    }
}
