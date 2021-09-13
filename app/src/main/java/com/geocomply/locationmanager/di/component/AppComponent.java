package com.geocomply.locationmanager.di.component;


import android.app.Application;

import com.geocomply.locationmanager.di.module.AppModule;
import com.geocomply.locationmanager.base.BaseRepository;
import com.geocomply.locationmanager.di.module.RestServiceModule;
import com.geocomply.locationmanager.repository.LocationRepository;
import com.geocomply.locationmanager.utils.ISchedulerProvider;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, RestServiceModule.class})
public interface AppComponent {

    //void inject(App app);

    LocationRepository getLocationRepository();

    ISchedulerProvider getSchedulerProvider();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
