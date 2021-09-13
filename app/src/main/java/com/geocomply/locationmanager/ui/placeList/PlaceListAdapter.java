package com.geocomply.locationmanager.ui.placeList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.geocomply.locationmanager.R;
import com.geocomply.locationmanager.databinding.LocationItemBinding;
import com.geocomply.locationmanager.repository.model.FavouriteLocation;
import com.geocomply.locationmanager.repository.model.Location;
import com.geocomply.locationmanager.ui.placeDetail.PlaceDetailActivity;

import java.util.List;

import javax.inject.Inject;

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.PlaceViewHolder> {

    final static String TAG ="Thu PostsAdapter";

    private List<FavouriteLocation> locations;

    private Context context;

    @Inject
    public PlaceListAdapter()
    { }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ////Log.i(TAG,"onCreateViewHolder");

        LocationItemBinding LocationItemBinding =
                DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                        R.layout.location_item, viewGroup, false);
        context = viewGroup.getContext();
        return new PlaceViewHolder(LocationItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder placeViewHolder, int i) {
        ////Log.i(TAG,"onBindViewHolder");

        FavouriteLocation currentLocation = locations.get(i);
        placeViewHolder.locationItemBinding.setLocation(currentLocation);
        placeViewHolder.locationItemBinding.executePendingBindings();
        placeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlaceDetailActivity.class);
                intent.putExtra("id", currentLocation.getPlaceId());
                intent.putExtra("img", currentLocation.getImage());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (locations != null) {
            return locations.size();
        } else {
            return 0;
        }
    }

    public void setLocations(List<FavouriteLocation> locations) {
        this.locations = locations;
        notifyDataSetChanged();
    }

    static class PlaceViewHolder extends RecyclerView.ViewHolder {

        private LocationItemBinding locationItemBinding;

        public PlaceViewHolder(@NonNull LocationItemBinding locationItemBinding) {
            super(locationItemBinding.getRoot());

            this.locationItemBinding = locationItemBinding;
        }

        public LocationItemBinding getLocationItemBinding() {
            return locationItemBinding;
        }
    }
}