package com.cookpad.android.marketapp;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
public class MarketApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
