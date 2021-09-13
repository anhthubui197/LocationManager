package com.geocomply.locationmanager.ui.placeDetail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.geocomply.locationmanager.R;
import com.geocomply.locationmanager.BR;
import com.geocomply.locationmanager.base.BaseActivity;
import com.geocomply.locationmanager.databinding.ActivityPlaceDetailBinding;
import com.geocomply.locationmanager.di.component.ActivityComponent;
import com.geocomply.locationmanager.repository.model.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import io.reactivex.annotations.Nullable;

public class PlaceDetailActivity extends BaseActivity<ActivityPlaceDetailBinding, PlaceDetailViewModel> implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    final static String TAG = "Thu PlaceDetailActivity";

    private GoogleMap mMap;

    private Marker marker, currentLocation;

    private FusedLocationProviderClient fusedLocationClient;

    private static LatLng userLocation;
    @Override
    public int getBindingVariable() {
        //Log.i(TAG, "getBindingVariable");

        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        //Log.i(TAG, "getLayoutId");

        return R.layout.activity_place_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.i(TAG, "onCreate");
        setUpMap();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            getUserLocation();
        } else {
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }

    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        //Log.i(TAG,"performDependencyInjection");

        buildComponent.inject(this);
    }

    private void observeViewModel() {
        String id = getIntent().getStringExtra("id");
        String img = getIntent().getStringExtra("img");
        mViewModel.getLocationDetail(id, img).observe(this, location -> {
            if(location != null){
                //Log.d(TAG, "LocationDetail onChanged: status: " + location.status);

                if(location.data != null){
                    switch (location.status){
                        case LOADING:{

                            break;
                        }
                        case ERROR:{
                            //Log.e(TAG, "onChanged: cannot refresh the cache." );
                            //Log.e(TAG, "onChanged: ERROR message: " + location.message );
                            //Log.e(TAG, "onChanged: status: ERROR, #data: " + location.data);
                            //mAdapter.hideLoading();
                            //mAdapter.setRecipes(listResource.data);
                            showToast(location.message);
                            break;
                        }

                        case SUCCESS:{
                            //Log.d(TAG, "onChanged: cache has been refreshed.");
                            //Log.d(TAG, "onChanged: status: SUCCESS, #data: " + location.data);
                            //mAdapter.hideLoading();
                            getViewDataBinding().setLocation(location.data);
                            addIcon(location.data);
                            if(userLocation != null)
                            {
                                mViewModel.getDirection(userLocation.latitude+","+userLocation.longitude,
                                        location.data.getLat()+","+location.data.getLng());
                            }
                            break;
                        }
                    }
                }
            }

        });
        mViewModel.getPolylineOptions().observe(this, polylineOptions ->
                {
                    if (polylineOptions.data != null) {
                        switch (polylineOptions.status) {
                            case ERROR: {
                                //Log.e(TAG, "onChanged: ERROR message: " + polylineOptions.message);
                                //Log.e(TAG, "onChanged: status: ERROR, #data: " + polylineOptions.data);

                                showToast(polylineOptions.message);
                                break;
                            }

                            case SUCCESS: {
                                //Log.d(TAG, "onChanged: cache has been refreshed.");
                                //Log.d(TAG, "onChanged: status: SUCCESS, #data: " + polylineOptions.data);

                                mMap.addPolyline(polylineOptions.data);
                                break;
                            }
                        }
                    }
                });
        mViewModel.getError().observe(this, errorMessage -> {
            if (!TextUtils.isEmpty(errorMessage)) {
                showToast(errorMessage);
            }
        });
    }

    private void setUpMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        //Log.i(TAG, "onMapReady");
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomGesturesEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        observeViewModel();
    }

    private void addIcon(Location location) {

//        //Log.i(TAG,location.getDescription());
  //      //Log.i(TAG,location.getLat().toString());
        CameraUpdate cameraUpdate = CameraUpdateFactory
                .newLatLngZoom(new LatLng(location.getLat(),location.getLng()), 15);
        mMap.animateCamera(cameraUpdate);
        try {

        Glide.with(this)
                .asBitmap()
                .load(location.getImage())
                .override(200,100)
                .fitCenter()
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        //Log.i(TAG, "Glide onStart "+ location.getImage());

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        //Log.i(TAG, "Glide onLoadFailed "+ location.getImage());
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_location_on_24);
                        BitmapDescriptor icon = getMarkerIconFromDrawable(drawable);
                        MarkerOptions markerOptions = new MarkerOptions()
                                .icon(icon)
                                .position(new LatLng(location.getLat(),location.getLng()))
                                .title(location.getName());
                        if(marker != null) {
                            marker.remove();
                        }
                        marker = mMap.addMarker(markerOptions);
                    }

                    @Override
                    public void onStop() {
                        super.onStop();
                        //Log.i(TAG, "Glide onStop "+ location.getImage());

                    }

                    @Override
                    public void onDestroy() {
                        super.onDestroy();
                        //Log.i(TAG, "Glide onDestroy "+ location.getImage());

                    }

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        //Log.i(TAG, "Glide onLoadStarted "+ location.getImage());

                    }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        //Log.i(TAG, "Glide onResourceReady " + location.getImage());
                        //Bitmap smallMarker = Bitmap.createScaledBitmap(resource, 100, 100, false);
                        MarkerOptions markerOptions = new MarkerOptions()
                                .icon(BitmapDescriptorFactory
                                       .fromBitmap(resource))
                                .position(new LatLng(location.getLat(),location.getLng()))
                                .title(location.getName());
                        if(marker != null) {
                            marker.remove();
                        }
                        marker = mMap.addMarker(markerOptions);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        //Log.i(TAG, "Glide onLoadCleared");
                    }
                });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Log.i(TAG, "onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.i(TAG, "onResume");
    }

    @SuppressLint("MissingPermission")
    private void getUserLocation(){
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
                    @Override
                    public void onSuccess(android.location.Location location) {
                        showToast(location.getLatitude() +" "+ location.getLongitude());
                        userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        if(currentLocation != null) {
                            currentLocation.remove();
                        }
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_my_location_24);
                        BitmapDescriptor icon = getMarkerIconFromDrawable(drawable);

                        currentLocation = mMap.addMarker(new MarkerOptions()
                                .icon(icon)
                                .position(userLocation)
                                .title("Your location"));
                    }
                });

    }
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    getUserLocation();
                } else {
                    showToast("Cant not show direction from your location without your permission");
                }
            });
    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
