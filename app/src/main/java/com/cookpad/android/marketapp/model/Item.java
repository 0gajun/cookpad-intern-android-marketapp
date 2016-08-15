package com.cookpad.android.marketapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
public class Item {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private int price;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("description")
    private String description;

    public Item(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }
}
