package com.geocomply.locationmanager.repository.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;


@Entity(tableName = "favourite_locations", primaryKeys = {"userId", "placeId"})
public class FavouriteLocation {

    @NonNull
    @ColumnInfo(name = "userId")
    String userId;
    @NonNull
    @ColumnInfo(name = "placeId")
    String placeId;
    @ColumnInfo(name = "code")
    private String code;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "image")
    private String image;
    @ColumnInfo(name = "timestamp")
    private int timestamp;

    public FavouriteLocation(@NonNull String userId, @NonNull String placeId, String code, String name, String image, int timestamp) {
        this.userId = userId;
        this.placeId = placeId;
        this.code = code;
        this.name = name;
        this.image = image;
        this.timestamp = timestamp;
    }

    public FavouriteLocation(String userId, Location location) {
        this.userId = userId;
        this.placeId = location.getId();
        this.code = location.getCode();
        this.name = location.getName();
        this.image = location.getImage();
        this.timestamp = (int)(System.currentTimeMillis() / 1000);
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getPlaceId() {
        return placeId;
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

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public void setPlaceId(@NonNull String placeId) {
        this.placeId = placeId;
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

    @Override
    public String toString() {
        return  "Location{" +
                "id='" + placeId + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", userId='" + userId + '\'' +
                ", image_url='" + image + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
