package com.geocomply.locationmanager.base;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.geocomply.locationmanager.repository.local.IDbHelper;
import com.geocomply.locationmanager.repository.remote.IApiHelper;
import com.geocomply.locationmanager.utils.ISchedulerProvider;


public abstract class BaseRepository  {

    protected final ISchedulerProvider schedulerProvider;

    protected final IApiHelper iApiHelper;

    protected final IDbHelper dbHelper;

    public BaseRepository(ISchedulerProvider mSchedulerProvider, Context context, IApiHelper iApiHelper, IDbHelper dbHelper) {
        this.schedulerProvider = mSchedulerProvider;
        this.iApiHelper = iApiHelper;
        this.dbHelper = dbHelper;
    }

    public ISchedulerProvider getSchedulerProvider() {
        return schedulerProvider;
    }
}
