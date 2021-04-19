package com.example.earthquake.DAO;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.earthquake.Earthquake;

@Database(entities = {Earthquake.class}, version = 1, exportSchema = false)
@TypeConverters(EarthquakeTypeConverters.class)
public abstract class EarthquakeDatabase extends RoomDatabase {
    public abstract EarthquakeDAO earthquakeDAO();
}
