package com.soltysdev.bravelandheroescalculator.db;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UnitQuantity.class}, version = 1, exportSchema = false)
abstract class UnitsDatabase extends RoomDatabase {

    abstract UnitQuantityDao unitQuantityDao();

    private static volatile UnitsDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static UnitsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UnitsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UnitsDatabase.class, "units_database")
                            .allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}
