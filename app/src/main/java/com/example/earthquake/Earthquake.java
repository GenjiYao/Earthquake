package com.example.earthquake;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Earthquake {
    @NonNull
    @PrimaryKey
    public String mID;
    public Date mDate;
    public String mDetails;
    public Location mLocation;
    public double mMagnitude;
    public String mLink;

    public String getMID() {
        return mID;
    }

    public Date getMDate() {
        return mDate;
    }

    public String getMDetails() {
        return mDetails;
    }

    public Location getMLocation() {
        return mLocation;
    }

    public double getMMagnitude() {
        return mMagnitude;
    }

    public String getMLink() {
        return mLink;
    }
    public Earthquake(){

    }
    public Earthquake(String mID, Date mDate, String mDetails, Location mLLocation, double mMagnitude, String mLink) {
        this.mID = mID;
        this.mDate = mDate;
        this.mDetails = mDetails;
        this.mLocation = mLLocation;
        this.mMagnitude = mMagnitude;
        this.mLink = mLink;
    }

    @Override
    public boolean equals(Object o) {
      if (o instanceof Earthquake){
          return (((Earthquake)o).getMID().contentEquals(mID));
      }else {
          return false;
      }
    }

    @NonNull
    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH .mm", Locale.US);
        String dateString = simpleDateFormat.format(mDate);
        return dateString + ": " + mMagnitude + " " + mDetails;

    }

    @Override
    public int hashCode() {
        return Objects.hash(mID, mDate, mDetails, mLocation, mMagnitude, mLink);
    }
}
