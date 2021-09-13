package com.geocomply.locationmanager.di.module;

import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;

import com.geocomply.locationmanager.base.BaseRepository;
import com.geocomply.locationmanager.repository.LocationRepository;
import com.geocomply.locationmanager.ui.placeDetail.PlaceDetailViewModel;
import com.geocomply.locationmanager.ui.placeList.PlaceListViewModel;
import com.geocomply.locationmanager.utils.ViewModelFactory;
import com.geocomply.locationmanager.base.BaseActivity;
import com.geocomply.locationmanager.utils.ISchedulerProvider;
import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private BaseActivity<?, ?> activity;

    public ActivityModule(BaseActivity<?, ?> activity) {
        this.activity = activity;
    }

    @Provides
    PlaceListViewModel providePlaceListViewModel(LocationRepository appRepository) {
        Supplier<PlaceListViewModel> supplier = () -> new PlaceListViewModel(appRepository );
        ViewModelFactory<PlaceListViewModel> factory = new ViewModelFactory<>(PlaceListViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(PlaceListViewModel.class);
    }
    @Provides
    PlaceDetailViewModel providePlaceDetailViewModel(LocationRepository appRepository) {
        Supplier<PlaceDetailViewModel> supplier = () -> new PlaceDetailViewModel(appRepository );
        ViewModelFactory<PlaceDetailViewModel> factory = new ViewModelFactory<>(PlaceDetailViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(PlaceDetailViewModel.class);
    }
}
