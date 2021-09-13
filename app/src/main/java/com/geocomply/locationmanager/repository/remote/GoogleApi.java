package com.geocomply.locationmanager.repository.remote;

import com.geocomply.locationmanager.repository.model.Direction;
import com.geocomply.locationmanager.repository.model.ListLocationResponse;
import com.geocomply.locationmanager.repository.model.LocationDetailResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GoogleApi {

    @GET("/maps/api/directions/json")
    Single<Direction> getDirection(@Query("origin") String origin,
                                   @Query("destination") String destination,
                                   @Query("key") String key);
}
