package com.geocomply.locationmanager.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public abstract class BaseResponse <T> {
    @Expose
    @SerializedName("error_message")
    private String error_message;

    @Expose
    @SerializedName("error_code")
    private Integer error_code;

    @Expose
    @SerializedName("data")
    private T data;

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public Integer getError_code() {
        return error_code;
    }

    public void setError_code(Integer error_code) {
        this.error_code = error_code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @NotNull
    @Override
    public String toString() {
        return  "Response{" +
                "error_message='" + error_message + '\'' +
                ", error_code='" + error_code + '\'' +
                ", data=" + data +
                '}';
    }

    public BaseResponse(String error_message, Integer error_code) {
        this.error_message = error_message;
        this.error_code = error_code;
    }
}
