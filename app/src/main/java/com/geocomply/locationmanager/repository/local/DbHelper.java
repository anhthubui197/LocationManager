package com.geocomply.locationmanager.repository.local;

import androidx.lifecycle.LiveData;

import com.geocomply.locationmanager.repository.model.FavouriteLocation;
import com.geocomply.locationmanager.repository.model.Location;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DbHelper implements IDbHelper{

    private final LocationDatabase locationDatabase;

    @Inject
    public DbHelper(LocationDatabase locationDatabase) {
        this.locationDatabase = locationDatabase;
    }

    @Override
    public void insertLocation(Location location) {
        locationDatabase.getLocationDao().insertLocation(location);
    }

    @Override
    public void insertFavouriteLocation(FavouriteLocation location) {
        locationDatabase.getLocationDao().insertFavouriteLocation(location);
    }

    @Override
    public LiveData<Location> loadLocation(String location_id) {
        return locationDatabase.getLocationDao().getLocation(location_id);
    }

    @Override
    public LiveData<List<FavouriteLocation>> loadFavouriteLocations(String user_id) {
        return locationDatabase.getLocationDao().getFavouriteLocations(user_id);
    }

    @Override
    public LiveData<FavouriteLocation> loadFavouriteLocation(String placeId, String userId) {
        return locationDatabase.getLocationDao().getFavouriteLocation(placeId, userId);
    }

    @Override
    public void updateLocation(String id, String code, String name, String image, String description, Double lat, Double lng) {
        locationDatabase.getLocationDao()
                .updateLocation( id, code, name, image, description, lat, lng);
    }

}
