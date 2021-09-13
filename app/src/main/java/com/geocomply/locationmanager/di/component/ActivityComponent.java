package com.geocomply.locationmanager.di.component;

import android.app.Application;

import com.geocomply.locationmanager.di.module.ActivityModule;
import com.geocomply.locationmanager.di.scope.ActivityScope;
import com.geocomply.locationmanager.ui.placeDetail.PlaceDetailActivity;
import com.geocomply.locationmanager.ui.placeList.PlaceListActivity;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@ActivityScope
@Component(modules = ActivityModule.class, dependencies = AppComponent.class)
public interface ActivityComponent {
    void inject(PlaceDetailActivity activity);
    void inject(PlaceListActivity activity);

}
