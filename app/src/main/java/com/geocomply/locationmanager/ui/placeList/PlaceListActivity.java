package com.geocomply.locationmanager.ui.placeList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.geocomply.locationmanager.BR;
import com.geocomply.locationmanager.R;
import com.geocomply.locationmanager.base.BaseActivity;
import com.geocomply.locationmanager.databinding.ActivityPlaceListBinding;
import com.geocomply.locationmanager.di.component.ActivityComponent;
import com.geocomply.locationmanager.repository.model.FavouriteLocation;
import com.geocomply.locationmanager.repository.model.Location;

import java.util.List;

import javax.inject.Inject;

public class PlaceListActivity extends BaseActivity<ActivityPlaceListBinding, PlaceListViewModel> {

    final static String TAG = "Thu PlaceListActivity";

    @Inject
    PlaceListAdapter placeListAdapter;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_place_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        observeViewModel();
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    private void observeViewModel() {
        mViewModel.getFavouriteLocations("19700").observe(this, locations -> {
            if(locations != null){
                //Log.d(TAG, "onChanged: status: " + locations.status);

                if(locations.data != null){
                    switch (locations.status){
                        case LOADING:{
                            break;
                        }

                        case ERROR:{
                            //Log.e(TAG, "onChanged: cannot refresh the cache." );
                            //Log.e(TAG, "onChanged: ERROR message: " + locations.message );
                            //Log.e(TAG, "onChanged: status: ERROR, #data size: " + locations.data.size());
                            //mAdapter.hideLoading();
                            //mAdapter.setRecipes(listResource.data);
                            showToast(locations.message);
                            break;
                        }

                        case SUCCESS:{
                            //Log.d(TAG, "onChanged: cache has been refreshed.");
                            //Log.d(TAG, "onChanged: status: SUCCESS, #data size: " + locations.data.size());
                            //mAdapter.hideLoading();
                            setUpLocationList(locations.data);
                            break;
                        }
                    }
                }
            }

        });
    }
    public void setUpLocationList(List<FavouriteLocation> response) {
        //Log.i(TAG,"setUpPosts");
        RecyclerView recyclerView = getViewDataBinding().locations;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        placeListAdapter.setLocations(response);
        recyclerView.setAdapter(placeListAdapter);
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
}