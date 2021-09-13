package com.geocomply.locationmanager.di.module;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.geocomply.locationmanager.di.qualifier.DatabaseInfo;
import com.geocomply.locationmanager.repository.LocationRepository;
import com.geocomply.locationmanager.base.BaseRepository;
import com.geocomply.locationmanager.repository.local.DbHelper;
import com.geocomply.locationmanager.repository.local.IDbHelper;
import com.geocomply.locationmanager.repository.local.LocationDatabase;
import com.geocomply.locationmanager.repository.remote.ApiHelper;
import com.geocomply.locationmanager.repository.remote.GoogleApi;
import com.geocomply.locationmanager.repository.remote.IApiHelper;
import com.geocomply.locationmanager.repository.remote.LocationApi;
import com.geocomply.locationmanager.utils.ISchedulerProvider;
import com.geocomply.locationmanager.utils.SchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    ISchedulerProvider provideSchedulerProvider(){
        return new SchedulerProvider();
    }

    @Provides
    @Singleton
    LocationRepository provideLocationRepository(Context context, IApiHelper iApiHelper, ISchedulerProvider mSchedulerProvider, IDbHelper dbHelper){
        return new LocationRepository(mSchedulerProvider, context, iApiHelper, dbHelper); }

    @Provides
    @Singleton
    LocationDatabase provideLocationDatabase(@DatabaseInfo String dbName, Context context) {
        return Room.databaseBuilder(context, LocationDatabase.class, dbName).fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    IApiHelper provideApiHelper (LocationApi locationApi, GoogleApi googleApi){
        return new ApiHelper(locationApi,googleApi);
    }

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    IDbHelper provideDbHelper(DbHelper dbHelper) {
        return dbHelper;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return "locations_db";
    }

}
