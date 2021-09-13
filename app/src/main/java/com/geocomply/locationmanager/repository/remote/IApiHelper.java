package com.geocomply.locationmanager.repository.remote;

import com.geocomply.locationmanager.repository.model.Direction;
import com.geocomply.locationmanager.repository.model.ListLocationResponse;
import com.geocomply.locationmanager.repository.model.Location;
import com.geocomply.locationmanager.repository.model.LocationDetailResponse;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface IApiHelper {
    Flowable<ListLocationResponse> getLocationList();
    Flowable<LocationDetailResponse> getLocationDetailResponse(String id);
    Single<Direction> getDirection(String origin, String destination);
}
