package com.example.earthquake.DAO;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.earthquake.Earthquake;

import java.util.List;

@Dao
public interface EarthquakeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertEarthquakes(List<Earthquake> earthquakes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertEarthquake(Earthquake earthquake);

    @Delete
    public void deleteEarthquake(Earthquake earthquake);

    @Query("SELECT * FROM earthquake ORDER BY mDate DESC")
    public LiveData<List<Earthquake>> loadAllEarthquakes();

    //此处下方的查询主要用于搜索功能的实现
    @Query("SELECT mID as _id, mDetails as suggest_text_1, mID as suggest_intent_data_id FROM earthquake WHERE mDetails LIKE :query ORDER BY mDate DESC")
    public Cursor generateSearchSuggestions(String query);

    @Query("SELECT * FROM earthquake WHERE mDetails LIKE :query ORDER BY mDate DESC")
    public LiveData<List<Earthquake>> searchEarthquakes(String query);

    @Query("SELECT * FROM earthquake WHERE mID = :id LIMIT 1")
    public LiveData<Earthquake> getEarthquake(String id);
}
