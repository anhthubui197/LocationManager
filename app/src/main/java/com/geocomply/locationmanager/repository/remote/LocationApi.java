package com.geocomply.locationmanager.repository.remote;

import com.geocomply.locationmanager.repository.model.ListLocationResponse;
import com.geocomply.locationmanager.repository.model.Location;
import com.geocomply.locationmanager.repository.model.LocationDetailResponse;

import io.reactivex.Flowable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LocationApi {

    @GET("2fe37bd6-9dd0-4384-9a65-14ae709b82d9")
    Flowable<ListLocationResponse> getLocationList();

    @GET("{id}")
    Flowable<LocationDetailResponse> getLocationDetail(@Path("id") String id);
}
