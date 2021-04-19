package com.example.earthquake.DAO;

import android.location.Location;

import androidx.room.TypeConverter;

import java.util.Date;

public class EarthquakeTypeConverters {
    @TypeConverter
    public static Date dateFromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dataToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static String locationToString(Location location){
        return location == null ?
                null : location.getLatitude() + "," + location.getLongitude();
    }

    @TypeConverter
    public static Location locationFromString (String location){
        if (location != null && (location.contains(","))){
            Location results = new Location("Generated");
            String[] locationStrings = location.split(",");
            if (locationStrings.length == 2) {
                results.setLatitude(Double.parseDouble(locationStrings[0]));
                results.setLongitude(Double.parseDouble(locationStrings[1]));
                return results;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}
