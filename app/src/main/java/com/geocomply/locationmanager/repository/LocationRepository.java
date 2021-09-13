package com.geocomply.locationmanager.repository;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.geocomply.locationmanager.base.BaseRepository;
import com.geocomply.locationmanager.repository.local.IDbHelper;
import com.geocomply.locationmanager.repository.model.Direction;
import com.geocomply.locationmanager.repository.model.FavouriteLocation;
import com.geocomply.locationmanager.repository.model.ListLocationResponse;
import com.geocomply.locationmanager.repository.model.Location;
import com.geocomply.locationmanager.repository.model.LocationDetailResponse;
import com.geocomply.locationmanager.repository.remote.IApiHelper;
import com.geocomply.locationmanager.utils.AppExecutors;
import com.geocomply.locationmanager.utils.ISchedulerProvider;
import com.geocomply.locationmanager.utils.NetworkBoundResource;
import com.geocomply.locationmanager.utils.Resource;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.Function;

public class LocationRepository extends BaseRepository {

    private static final String TAG = "Thu LocationRepository";

    public LocationRepository(ISchedulerProvider mSchedulerProvider, Context context, IApiHelper iApiHelper, IDbHelper dbHelper) {
        super(mSchedulerProvider, context, iApiHelper, dbHelper);
    }

    public LiveData<Resource<List<FavouriteLocation>>> getFavouriteLocations(String userId) {
        return new NetworkBoundResource<List<FavouriteLocation>, ListLocationResponse>(AppExecutors.getInstance())
        {
            @Override
            protected void saveCallResult(@NonNull ListLocationResponse item) {
                List<Location> loc = item.getData().getLocations();
                for (Location location: item.getData().getLocations()) {
                    dbHelper.insertLocation(location);
                    dbHelper.insertFavouriteLocation(
                            new FavouriteLocation(userId,location));
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<FavouriteLocation> data) {
                //Log.i(TAG, "shouldFetch");

                if(data.size() == 0 || data == null) return true;
                //Log.i(TAG, "data not empty ");

                //FavouriteLocation item = loadFavouriteLocation(data.get(0).getPlaceId(), userId).getValue();
//                if (item == null) {
//                    //Log.i(TAG, "cached data == null => should fetch");
//                    return true;
//                }
                //Log.i(TAG,"DATA 0: " + data.get(0).toString());
                int currentTime = (int)(System.currentTimeMillis() / 1000);
                int cachedTime = data.get(0).getTimestamp();
                //Log.i(TAG, "Time caching: " + (currentTime - cachedTime) + "s");
                return ((currentTime - cachedTime) >= 5 * 60);
            }

            @NonNull
            @Override
            protected LiveData<List<FavouriteLocation>> loadFromDb() {
                //Log.i(TAG, "loadFromDb");
                return dbHelper.loadFavouriteLocations(userId);
            }

            @NonNull
            @Override
            protected LiveData<ListLocationResponse> createCall() {
                //Log.i(TAG, "createCall");
                return LiveDataReactiveStreams.fromPublisher( iApiHelper.getLocationList()
                        .onErrorReturn(error -> {
                            //Log.i(TAG, String.valueOf(error.getClass()));
                            //Log.i(TAG, "getFavouriteLocationsResponse Error: " + error.getMessage())));
                            return new ListLocationResponse(error.getMessage(),700);
                        })
                        .subscribeOn(getSchedulerProvider().io()) // "work" on io thread
                        .observeOn(getSchedulerProvider().ui()) // "listen" on UIThread
                        );
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<Location>> getLocationDetail(String id, String img) {
        return new NetworkBoundResource<Location, LocationDetailResponse>(AppExecutors.getInstance()) {
            @Override
            protected void saveCallResult(@NonNull LocationDetailResponse item) {
                //Log.i(TAG, "saveCallResult");

                // will be null if API key is expired
                if(item.getData() != null){
                    Location iLocation = item.getData().getLocation();
                    iLocation.setTimestamp((int)(System.currentTimeMillis() / 1000));
                    iLocation.setId(id);
                    iLocation.setImage(img);
                    //Log.i(TAG, iLocation.toString());
                    dbHelper.insertLocation(iLocation);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable Location data) {
                //Log.i(TAG, "shouldFetch ");
                if(data == null) return true;
                //Log.d(TAG, "location not null: " + data.toString());
                int currentTime = (int)(System.currentTimeMillis() / 1000);
                //Log.d(TAG, "shouldFetch: current time: " + currentTime);
                int lastRefresh = data.getTimestamp();
                //Log.d(TAG, "shouldFetch: last refresh: " + lastRefresh);
                //Log.d(TAG, "caching time: " + ((currentTime - lastRefresh) / 60 / 60 / 24));
                if((currentTime - data.getTimestamp()) >= 5 * 60){
                    //Log.d(TAG, "shouldFetch: SHOULD REFRESH LOCATION?! " + true);
                    return true;
                }
                //Log.d(TAG, "shouldFetch: SHOULD REFRESH LOCATION?! " + false);
                return false;            }

            @NonNull
            @Override
            protected LiveData<Location> loadFromDb() {
                //Log.i(TAG, "loadFromDb");
                return dbHelper.loadLocation(id);
            }

            @NotNull
            @Override
            protected LiveData<LocationDetailResponse> createCall() {
                //Log.i(TAG, "createCall");
                return LiveDataReactiveStreams.fromPublisher(
                        iApiHelper.getLocationDetailResponse(id)
                                .onErrorReturn(error -> {
                                    //Log.i(TAG, String.valueOf(error.getClass()));
                                    return new LocationDetailResponse(error.getMessage(),700);
                                })
                                .subscribeOn(getSchedulerProvider().io()) // "work" on io thread
                                .observeOn(getSchedulerProvider().ui()) // "listen" on UIThread
                );
            }
        }.getAsLiveData();    }

    public Single<Resource<PolylineOptions>> getDirection(String origin, String destination) {
        return iApiHelper.getDirection(origin,destination)
                .subscribeOn(getSchedulerProvider().io()) // "work" on io thread
                .observeOn(getSchedulerProvider().ui()) // "listen" on UIThread
                .onErrorReturn(error -> {
                    //Log.i(TAG, "Error Direction request");
                    Direction errorDirection = new Direction();
                    errorDirection.setStatus("Request Error. Cannot show direction");
                    return errorDirection;
                })
                .map(direction -> {
                    PolylineOptions mPolylineOptions = new PolylineOptions();
                    if(!direction.getStatus().equals("OK"))
                    {
                        //Log.i(TAG, "Response not OK: "+ direction.getStatus());
                        return Resource.error(direction.getStatus(), mPolylineOptions);
                    }
                    for(Direction.Route route: direction.getRoutes())
                    {
                        for(Direction.Leg leg :route.getLegs())
                        {
                            for (Direction.Step step:leg.getSteps())
                            {
                                mPolylineOptions.color(Color.BLUE);
                                mPolylineOptions.width(9);
                                mPolylineOptions.addAll(PolyUtil.decode(step.getPolyline().getPoints()));
                            }
                        }
                    }
                    //Log.i(TAG, "Direction Success");
                    return Resource.success(mPolylineOptions);
                });
    }

}

