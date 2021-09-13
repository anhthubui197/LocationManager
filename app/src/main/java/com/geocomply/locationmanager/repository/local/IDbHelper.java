package com.geocomply.locationmanager.repository.local;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.geocomply.locationmanager.repository.model.FavouriteLocation;
import com.geocomply.locationmanager.repository.model.Location;

import java.util.List;

public interface IDbHelper {

    void insertLocation(Location location);
    void insertFavouriteLocation(FavouriteLocation location);

    LiveData<Location> loadLocation(String location_id);
    LiveData<List<FavouriteLocation>> loadFavouriteLocations(String user_id);
    LiveData<FavouriteLocation> loadFavouriteLocation(String placeId, String userId);
    void updateLocation(String id, String code, String name, String image, String description, Double lat, Double lng);


}
