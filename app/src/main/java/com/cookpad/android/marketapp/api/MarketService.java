package com.cookpad.android.marketapp.api;

import com.cookpad.android.marketapp.model.Category;
import com.cookpad.android.marketapp.model.Item;
import com.cookpad.android.marketapp.model.PurchaseRequest;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
    @GET("items.json")
    Observable<List<Item>> getItems();
    @GET("categories.json")
    Observable<List<Category>> getCategories();
    @GET("categories/{category_id}/items.json")
    Observable<List<Item>> getCategoryItems(@Path("category_id") int categoryId);
    @POST("orders")
    Observable<Object> postOrders(@Body PurchaseRequest request);
}