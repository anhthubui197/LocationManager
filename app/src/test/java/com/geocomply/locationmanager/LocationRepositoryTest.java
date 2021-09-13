package com.geocomply.locationmanager;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.geocomply.locationmanager.repository.LocationRepository;
import com.geocomply.locationmanager.repository.local.IDbHelper;
import com.geocomply.locationmanager.repository.model.FavouriteLocation;
import com.geocomply.locationmanager.repository.model.ListLocationResponse;
import com.geocomply.locationmanager.repository.model.Location;
import com.geocomply.locationmanager.repository.model.LocationDetailResponse;
import com.geocomply.locationmanager.repository.remote.IApiHelper;
import com.geocomply.locationmanager.utils.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LocationRepositoryTest {


    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Rule
    public RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

    TestScheduler mTestScheduler;
    @Mock
    Context context;
    @Mock
    IApiHelper iApiHelper;
    @Mock
    IDbHelper dbHelper;
    @Mock
    LocationRepository locationRepository;
    //@Mock
  //  LifecycleOwner lifecycleOwner;
//    LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(lifecycleOwner);
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(h -> Schedulers.trampoline());
        RxJavaPlugins.setIoSchedulerHandler(h -> Schedulers.trampoline());

        //when(lifecycleOwner.getLifecycle()).thenReturn(lifecycleRegistry);

        mTestScheduler = new TestScheduler();
        TestSchedulerProvider testSchedulerProvider = new TestSchedulerProvider(mTestScheduler);
        locationRepository = new LocationRepository(testSchedulerProvider, context, iApiHelper, dbHelper);

    }
    @After
    public void tearDown()
    {
        RxAndroidPlugins.reset();
    }

    @Test
    public void test_getFavouriteLocations_success() {
        //Resource result = Resource.success(new ListLocationResponse("",0));
        ArrayList<FavouriteLocation> favouriteLocations = new ArrayList<>();
        LiveData<List<FavouriteLocation>> listFavouriteLocation = new MutableLiveData<>(favouriteLocations);
        ListLocationResponse result = (new ListLocationResponse("",0));
        Location location = new Location("","","","","",0.0,0.0);
        FavouriteLocation favouriteLocation = new FavouriteLocation("", location);
        result.setData(new ListLocationResponse.Data(new ArrayList<>()));

        doReturn(Flowable.just(result))
                .when(iApiHelper)
                .getLocationList();
        doReturn(listFavouriteLocation)
                .when(dbHelper)
                .loadFavouriteLocations(anyString());
        doNothing()
                .when(dbHelper)
                .insertLocation(location);
        doNothing()
                .when(dbHelper)
                .insertFavouriteLocation(favouriteLocation);

        final int[] times = {1};
        LiveData<Resource<List<FavouriteLocation>>> actualResult = locationRepository.getFavouriteLocations("19700");
        actualResult.observeForever(listResource -> {

            if(times[0] == 0)
                assertEquals(listResource.status.toString(),"SUCCESS");
            times[0] -=1;
        });
        mTestScheduler.triggerActions();
        //verify(observer).onChanged(any(Resource.class));
    }
//
//    @Test
//    public void test_getFavouriteLocations_error() {
//        //Resource result = Resource.success(new ListLocationResponse("",0));
//        LiveData<List<FavouriteLocation>> listFavouriteLocation = new MutableLiveData<>();
//        ListLocationResponse result = (new ListLocationResponse("",600));
//        Location location = new Location("","","","","",0.0,0.0);
//        FavouriteLocation favouriteLocation = new FavouriteLocation("", location);
//
//        doReturn(Flowable.just(result))
//                .when(iApiHelper)
//                .getLocationList();
//        doReturn(listFavouriteLocation)
//                .when(dbHelper)
//                .loadFavouriteLocations(anyString());
//
//        LiveData<Resource<List<FavouriteLocation>>> actualResult = locationRepository.getFavouriteLocations("19700");
//
//        //actualResult.observe(lifecycleOwner,observer);
//        //verify(observer).onChanged(any(Resource.class));
//        final int[] times = {1};
//        actualResult.observeForever(new Observer<Resource<List<FavouriteLocation>>>() {
//            @Override
//            public void onChanged(Resource<List<FavouriteLocation>> listResource) {
//                if(times[0] == 0)
//                    assertEquals(listResource.status.toString(),"ERROR");
//                times[0] -=1;
//            }
//        });
//        //listFavouriteLocation.setValue(new ArrayList<>());
//    }
//
//    @Test
//    public void test_getFavouriteLocations_loading() {
//        LiveData<List<FavouriteLocation>> listFavouriteLocation = new MutableLiveData<>();
//        ListLocationResponse result = (new ListLocationResponse("",600));
//        Location location = new Location("","","","","",0.0,0.0);
//        FavouriteLocation favouriteLocation = new FavouriteLocation("", location);
//
//        doReturn(Flowable.just(result))
//                .when(iApiHelper)
//                .getLocationList();
//        doReturn(listFavouriteLocation)
//                .when(dbHelper)
//                .loadFavouriteLocations(anyString());
//
//        LiveData<Resource<List<FavouriteLocation>>> actualResult = locationRepository.getFavouriteLocations("19700");
//
//        actualResult.observeForever(new Observer<Resource<List<FavouriteLocation>>>() {
//            @Override
//            public void onChanged(Resource<List<FavouriteLocation>> listResource) {
//                assertEquals(listResource.status.toString(),"LOADING");
//            }
//        });
//    }
//    @Test
//    public void test_getLocationDetail_loading() {
//        Location location = new Location("","","","","",0.0,0.0);
//        LiveData<Location> locationLiveData = new MutableLiveData<>(location);
//        LocationDetailResponse result = (new LocationDetailResponse("",600));
//
//        doReturn(Flowable.just(result))
//                .when(iApiHelper)
//                .getLocationDetailResponse("");
//        doReturn(locationLiveData)
//                .when(dbHelper)
//                .loadLocation(anyString());
//
//        LiveData<Resource<Location>> actualResult = locationRepository.getLocationDetail("","url");
//
//        actualResult.observeForever(new Observer<Resource<Location>>() {
//            @Override
//            public void onChanged(Resource<Location> locationResource) {
//                assertEquals(locationResource.status.toString(),"LOADING");
//
//            }
//        });
//    }
//
//    @Test
//    public void test_getLocationDetail_success() {
//        Location location = new Location("thu","","","","",0.0,0.0);
//        LiveData<Location> locationLiveData = new MutableLiveData<>(location);
//        LocationDetailResponse result = new LocationDetailResponse("",0);
//        result.setData(new LocationDetailResponse.Data());
//
//        doReturn(Flowable.just(result))
//                .when(iApiHelper)
//                .getLocationDetailResponse("thu");
//        doReturn(locationLiveData)
//                .when(dbHelper)
//                .loadLocation(anyString());
//
//        LiveData<Resource<Location>> actualResult = locationRepository.getLocationDetail("thu","url");
//
//        final int[] times = {1};
//        actualResult.observeForever(locationResource -> {
//            if(times[0] == 0)
//                assertEquals(locationResource.status.toString(),"SUCCESS");
//            times[0] -=1;
//        });
//    }
//    @Test
//    public void test_getLocationDetail_error() {
//        LiveData<Location> location = new MutableLiveData<>();
//        LocationDetailResponse result = (new LocationDetailResponse("",600));
//
//        doReturn(Flowable.just(result))
//                .when(iApiHelper)
//                .getLocationDetailResponse("");
//        doReturn(location)
//                .when(dbHelper)
//                .loadLocation(anyString());
//
//        LiveData<Resource<Location>> actualResult = locationRepository.getLocationDetail("","url");
//
//        final int[] times = {1};
//        actualResult.observeForever(new Observer<Resource<Location>>() {
//            @Override
//            public void onChanged(Resource<Location> locationResource) {
//                if(times[0] == 0)
//                    assertEquals(locationResource.status.toString(),"ERROR");
//                times[0] -=1;
//            }
//        });
//    }

}
