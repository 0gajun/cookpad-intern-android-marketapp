package com.cookpad.android.marketapp.model;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
public class Item {
    private int id;

    private String name;

    private int price;

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

    public int getPrice() {
        return price;
    }
}
