package com.geocomply.locationmanager.ui.placeDetail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.geocomply.locationmanager.base.BaseViewModel;
import com.geocomply.locationmanager.repository.LocationRepository;
import com.geocomply.locationmanager.repository.model.Location;
import com.geocomply.locationmanager.utils.ISchedulerProvider;
import com.geocomply.locationmanager.utils.Resource;
import com.google.android.gms.maps.model.PolylineOptions;

public class PlaceDetailViewModel extends BaseViewModel<LocationRepository> {

    private static final String TAG = "Thu PlaceDetailVM";
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Resource<PolylineOptions>> polylineOptions = new MutableLiveData<>();

    public MutableLiveData<Resource<PolylineOptions>> getPolylineOptions() {
        return polylineOptions;
    }

    public PlaceDetailViewModel(LocationRepository baseRepository) {
        super(baseRepository);
    }

    public MutableLiveData<String> getError() {
        return error;
    }

    public LiveData<Resource<Location>> getLocationDetail(String id, String img) {
        return getAppRepository().getLocationDetail(id, img);
    }

    public void getDirection(String origin, String destination)
    {
        //Log.i(TAG, "getDirection");
        getCompositeDisposable()
                .add(getAppRepository().getDirection(origin, destination)
                        .subscribe(polylineOptions::setValue));
    }
}
