package com.geocomply.locationmanager.utils;

import io.reactivex.Scheduler;

public interface ISchedulerProvider {

    Scheduler computation();

    Scheduler io();

    Scheduler ui();
}
