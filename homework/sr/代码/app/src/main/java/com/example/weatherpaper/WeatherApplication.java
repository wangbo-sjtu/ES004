package com.example.weatherpaper;

import android.app.Application;

import interfaces.heweather.com.interfacesmodule.view.HeConfig;

public class WeatherApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        HeConfig.switchToFreeServerNode();
        HeConfig.init("", "");

    }
}
