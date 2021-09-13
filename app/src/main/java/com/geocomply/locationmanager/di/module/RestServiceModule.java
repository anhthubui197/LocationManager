package com.geocomply.locationmanager.di.module;


import com.geocomply.locationmanager.BuildConfig;
import com.geocomply.locationmanager.di.qualifier.RetrofitGoogle;
import com.geocomply.locationmanager.di.qualifier.RetrofitLocation;
import com.geocomply.locationmanager.repository.remote.GoogleApi;
import com.geocomply.locationmanager.repository.remote.LocationApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RestServiceModule {

    @Singleton
    @Provides
    @RetrofitLocation
    static Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                        //.client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    @RetrofitGoogle
    static Retrofit provideRetrofitGoogle() {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_GOOGLE_URL)
                //.client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    static LocationApi provideLocationService(@RetrofitLocation Retrofit retrofit) {
        return retrofit.create(LocationApi.class);
    }

    @Singleton
    @Provides
    static GoogleApi provideGoogleService(@RetrofitGoogle Retrofit retrofit) {
        return retrofit.create(GoogleApi.class);
    }
}
