package com.geocomply.locationmanager.repository.local;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.geocomply.locationmanager.repository.model.FavouriteLocation;
import com.geocomply.locationmanager.repository.model.Location;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface LocationDao {
    @Insert(onConflict = IGNORE)
    long[] insertLocations(Location... location);

    @Insert(onConflict = REPLACE)
    void insertLocation(Location location);

    @Query("UPDATE locations SET code = :code, name = :name, image = :image, description = :description, lat = :lat, lng = :lng " +
            "WHERE id = :id")
    void updateLocation(@NonNull String id, String code, String name, String image, String description, Double lat, Double lng);

    @Query("SELECT * FROM locations WHERE id = :location_id")
    LiveData<Location> getLocation(String location_id);

    @Insert(onConflict = REPLACE)
    void insertFavouriteLocation(FavouriteLocation location);

    @Query("SELECT * FROM favourite_locations WHERE userId = :userId")
    LiveData<List<FavouriteLocation>> getFavouriteLocations(String userId);
    
    @Query("SELECT * FROM favourite_locations WHERE (placeId =:placeId AND userId = :userId)")
    LiveData<FavouriteLocation> getFavouriteLocation(String placeId, String userId);
}
