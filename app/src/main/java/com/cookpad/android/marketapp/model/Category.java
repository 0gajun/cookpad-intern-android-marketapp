package com.cookpad.android.marketapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
public class Category {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("category_items_url")
    private String categoryItemsUrl;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategoryItemsUrl() {
        return categoryItemsUrl;
    }
}
