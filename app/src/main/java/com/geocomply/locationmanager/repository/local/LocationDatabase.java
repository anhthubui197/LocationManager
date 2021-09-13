package com.geocomply.locationmanager.repository.local;


import android.content.Context;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.geocomply.locationmanager.repository.model.FavouriteLocation;
import com.geocomply.locationmanager.repository.model.Location;

@Database(entities = {Location.class, FavouriteLocation.class}, version = 2)
public abstract class LocationDatabase extends RoomDatabase {

    public abstract LocationDao getLocationDao();
}