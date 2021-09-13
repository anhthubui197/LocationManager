package com.geocomply.locationmanager.repository.model;

import com.geocomply.locationmanager.base.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationDetailResponse extends BaseResponse<LocationDetailResponse.Data> {

    public LocationDetailResponse(String error_message, Integer error_code) {
        super(error_message, error_code);
    }

    public static class Data{
        @Expose
        @SerializedName("location")
        private Location location;

        public Location getLocation() {
            return location;
        }

        public void setLocations(Location locations) {
            this.location = locations;
        }
    }
}
