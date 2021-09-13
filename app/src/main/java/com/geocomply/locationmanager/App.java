/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.geocomply.locationmanager;

import android.app.Application;
import android.util.Log;

import com.geocomply.locationmanager.di.component.AppComponent;
import com.geocomply.locationmanager.di.component.DaggerAppComponent;



/**
 * Created by amitshekhar on 07/07/17.
 */

public class App extends Application {

    private static final String TAG = "App";
    public AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        //Log.i(TAG,"onCreate");
        appComponent = DaggerAppComponent.builder()
                .application(this)
                .build();

        //appComponent.inject(this);
    }
}
