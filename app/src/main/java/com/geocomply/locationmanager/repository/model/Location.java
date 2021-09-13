package com.geocomply.locationmanager.repository.model;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.geocomply.locationmanager.R;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Arrays;

@Entity(tableName = "locations")
public final class Location implements Serializable {

    @Expose
    @SerializedName("id")
    @PrimaryKey
    @NonNull
    private String id;

    @Expose
    @SerializedName("code")
    @ColumnInfo(name = "code")
    private String code;

    @Expose
    @SerializedName("name")
    @ColumnInfo(name = "name")
    private String name;

    @Expose
    @SerializedName("image")
    @ColumnInfo(name = "image")
    private String image;

    @Expose
    @SerializedName("description")
    @ColumnInfo(name = "description")
    private String description;

    @Expose
    @SerializedName("lat")
    @ColumnInfo(name = "lat")
    private Double lat;

    @Expose
    @SerializedName("lng")
    @ColumnInfo(name = "lng")
    private Double lng;

    @ColumnInfo(name = "timestamp")
    private int timestamp;

    public Location(@NonNull String id, String code, String name, String image, String description, Double lat, Double lng) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.image = image;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    @BindingAdapter({ "image" })
    public static void loadImage(ImageView imageView, String imageURL) {
        try {
            Glide.with(imageView.getContext())
                    .setDefaultRequestOptions(new RequestOptions().fitCenter())
                    .load(imageURL)
                    .placeholder(R.drawable.marker)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public String toString() {
        return  "Location{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", image_url='" + image + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
