package com.cookpad.android.marketapp.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
public class MarketServiceHolder {
    private static final String ENDPOINT = "http://fierce-beach-89314.herokuapp.com";
    private static MarketService marketService;

    public static MarketService getMarketService() {
        if (marketService == null) {
            marketService = createMarketService();
        }
        return marketService;
    }

    private static MarketService createMarketService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(MarketService.class);
    }
}
