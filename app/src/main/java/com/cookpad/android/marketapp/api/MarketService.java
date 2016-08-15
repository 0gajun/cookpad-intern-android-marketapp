package com.cookpad.android.marketapp.api;

import com.cookpad.android.marketapp.model.Item;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
public interface MarketService {
    @GET("items/recommended.json")
    Observable<List<Item>> getRecommendedItems();
    @GET("items/{id}.json")
    Observable<Item> getItem(@Path("id") int id);
}