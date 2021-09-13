package com.geocomply.locationmanager.base;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.geocomply.locationmanager.utils.ISchedulerProvider;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseViewModel<T extends BaseRepository> extends ViewModel {

    private static String TAG ="Thu BaseViewModel";

    private final T baseRepository;

    private final CompositeDisposable mCompositeDisposable;

    public BaseViewModel(T baseRepository) {
        //Log.i(TAG,"BaseViewModel");

        this.baseRepository = baseRepository;
        this.mCompositeDisposable = new CompositeDisposable();

    }

    @Override
    protected void onCleared() {
        //Log.i(TAG,"onCleared");

        mCompositeDisposable.dispose();
        super.onCleared();
    }


    public T getAppRepository() {
        return baseRepository;
    }
    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }


}
