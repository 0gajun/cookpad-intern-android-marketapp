package com.cookpad.android.marketapp.model;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
@Table
public class CartItem {
    @PrimaryKey(autoincrement = true)
    public long id;

    @Column(indexed = true)
    public int itemId;

    @Column
    public String name;

    @Column
    public int price;

    @Column(value = "0")
    public int count;

    public CartItem() {
    }

    public CartItem(Item item, int count) {
        this.itemId = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.count = count;
    }
}
