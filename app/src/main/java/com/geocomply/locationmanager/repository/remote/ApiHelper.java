package com.geocomply.locationmanager.repository.remote;



import com.geocomply.locationmanager.BuildConfig;
import com.geocomply.locationmanager.repository.model.Direction;
import com.geocomply.locationmanager.repository.model.ListLocationResponse;
import com.geocomply.locationmanager.repository.model.Location;
import com.geocomply.locationmanager.repository.model.LocationDetailResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Singleton
public class ApiHelper implements IApiHelper {

    final static String TAG ="Thu ApiHelper";

    final private LocationApi locationApi;
    final private GoogleApi googleApi;

    @Inject
    public ApiHelper(LocationApi locationApi, GoogleApi googleApi) {
        this.locationApi = locationApi;
        this.googleApi = googleApi;
    }

    @Override
    public Flowable<ListLocationResponse> getLocationList() {
        return locationApi.getLocationList();
    }

    @Override
    public Flowable<LocationDetailResponse> getLocationDetailResponse(String id) {
        return locationApi.getLocationDetail(id);
    }

    @Override
    public Single<Direction> getDirection(String origin, String destination) {
        return googleApi.getDirection(origin, destination, BuildConfig.API_KEY);
    }
}
