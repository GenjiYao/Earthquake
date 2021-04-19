package com.example.earthquake.DAO;

import android.content.Context;

import androidx.room.Room;

public class EarthquakeDatabaseAccessor {
    private static EarthquakeDatabase EarthquakeDatabaseInstance;
    private static final String EARTHQUAKE_DB_NAME = "earthquake_db";
    private EarthquakeDatabaseAccessor() {}
    public static EarthquakeDatabase getInstance(Context context) {
        if (EarthquakeDatabaseInstance == null){
            //获取已经创建的数据库实例
            EarthquakeDatabaseInstance = Room.databaseBuilder(context, EarthquakeDatabase.class, EARTHQUAKE_DB_NAME).build();
        }
        return EarthquakeDatabaseInstance;
    }
}
