package com.geocomply.locationmanager.ui.placeList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.geocomply.locationmanager.base.BaseViewModel;
import com.geocomply.locationmanager.repository.LocationRepository;
import com.geocomply.locationmanager.repository.model.FavouriteLocation;
import com.geocomply.locationmanager.repository.model.Location;
import com.geocomply.locationmanager.utils.ISchedulerProvider;
import com.geocomply.locationmanager.utils.Resource;

import java.util.List;

public class PlaceListViewModel extends BaseViewModel<LocationRepository> {

    private static final String TAG = "Thu PlaceListViewModel";
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<List<Location>> locations = new MutableLiveData<>();

    public PlaceListViewModel(LocationRepository locationRepository) {
        super(locationRepository);
    }

    public MutableLiveData<List<Location>> getLocations() {
        return locations;
    }

    public MutableLiveData<String> getError() {
        return error;
    }

    public LiveData<Resource<List<FavouriteLocation>>> getFavouriteLocations(String userId) {
        return getAppRepository().getFavouriteLocations(userId);
    }
}
