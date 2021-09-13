package com.geocomply.locationmanager.utils;

import android.os.Debug;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.geocomply.locationmanager.base.BaseResponse;
import com.geocomply.locationmanager.repository.model.Location;

public abstract class NetworkBoundResource<CacheObject, RequestObject extends BaseResponse> {

    private static final String TAG = "Thu NetBoundResource";

    private AppExecutors appExecutors;
    private MediatorLiveData<Resource<CacheObject>> results = new MediatorLiveData<>();
    //private MutableLiveData<RequestObject> apiResponse = new MutableLiveData<>();

    public NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        init();
    }

    private void init(){

        // update LiveData for loading status
        results.setValue(Resource.loading(null));

        // observe LiveData source from local db
        final LiveData<CacheObject> dbSource = loadFromDb();

        results.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(@Nullable CacheObject cacheObject) {

                //Log.i(TAG, "dbSource on changed: ");
                if(cacheObject!=null)
                    //Log.i(TAG,  cacheObject.toString());

                results.removeSource(dbSource);

                if(shouldFetch(cacheObject)){
                    // get data from the network
                    //Log.i(TAG, "shouldFetch");
                    fetchFromNetwork(dbSource);
                }
                else{
                    //Log.i(TAG, "should not Fetch");
                    results.addSource(dbSource, new Observer<CacheObject>() {
                        @Override
                        public void onChanged(@Nullable CacheObject cacheObject) {
                            setValue(Resource.success(cacheObject));
                        }
                    });
                }
            }
        });
    }

    /**
     * 1) observe local db
     * 2) if <condition/> query the network
     * 3) stop observing the local db
     * 4) insert new data into local db
     * 5) begin observing local db again to see the refreshed data from network
     * @param dbSource
     */
    private void fetchFromNetwork(final LiveData<CacheObject> dbSource){

        //Log.d(TAG, "fetchFromNetwork: called");

        // update LiveData for loading status
        results.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(@Nullable CacheObject cacheObject) {
                setValue(Resource.loading(cacheObject));
            }
        });

        final LiveData<RequestObject> apiResponse = createCall();
        //createCall();
        RequestObject requestObject = apiResponse.getValue();
        results.addSource(apiResponse, requestObjectApiResponse -> {
            //Log.i(TAG, "apiResponse on changed: "+ requestObjectApiResponse);

            results.removeSource(dbSource);
            results.removeSource(apiResponse);

            /*
                3 cases:
                   1) ApiSuccessResponse
                   2) ApiErrorResponse
                   3) ApiEmptyResponse
             */

            if(requestObjectApiResponse.getError_code() == 0){ //Success
                //Log.d(TAG, "onChanged: ApiSuccessResponse.");

                appExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        // save the response to the local db
                        saveCallResult(requestObjectApiResponse);

                        appExecutors.mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                Debug.waitForDebugger();
                                results.addSource(loadFromDb(), new Observer<CacheObject>() {
                                    @Override
                                    public void onChanged(@Nullable CacheObject cacheObject) {
                                        //Log.d(TAG, "Success onChanged: loadFromDb.");
                                        Debug.waitForDebugger();
                                        setValue(Resource.success(cacheObject));
                                    }
                                });
                            }
                        });
                    }
                });
            }
            else{ //error
                //Log.d(TAG, "onChanged: ApiErrorResponse.");
                results.addSource(dbSource, new Observer<CacheObject>() {
                    @Override
                    public void onChanged(@Nullable CacheObject cacheObject) {
                        //Log.d(TAG, "error onChanged: loadFromDb.");

                        setValue(Resource.error(requestObjectApiResponse.getError_message(),cacheObject));
                    }
                });
            }
        });
    }

    private void setValue(Resource<CacheObject> newValue){
        if(results.getValue() != newValue){
            //Log.d(TAG, "setValue CacheObject");

            results.setValue(newValue);
        }
    }

    // Called to save the result of the API response into the database.
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestObject item);

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract boolean shouldFetch(@Nullable CacheObject data);

    // Called to get the cached data from the database.
    @NonNull @MainThread
    protected abstract LiveData<CacheObject> loadFromDb();

    // Called to create the API call.
    @NonNull @MainThread
    protected abstract LiveData<RequestObject> createCall();

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    public final LiveData<Resource<CacheObject>> getAsLiveData(){
        //Log.i(TAG, "getAsLiveData " + results.getValue());

        return results;
    };
}




