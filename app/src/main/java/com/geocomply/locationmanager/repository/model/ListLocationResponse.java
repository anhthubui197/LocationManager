package com.geocomply.locationmanager.repository.model;

import com.geocomply.locationmanager.base.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListLocationResponse extends BaseResponse<ListLocationResponse.Data> {

    public ListLocationResponse(String error_message, Integer error_code) {
        super(error_message, error_code);
    }

    public static class Data{
        @Expose
        @SerializedName("locations")
        private List<Location> locations;

        public Data(List<Location> locations) {
            this.locations = locations;
        }

        public List<Location> getLocations() {
            return locations;
        }

        public void setLocations(List<Location> locations) {
            this.locations = locations;
        }
    }
}
