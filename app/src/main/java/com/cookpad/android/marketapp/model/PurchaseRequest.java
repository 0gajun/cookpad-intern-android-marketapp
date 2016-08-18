package com.cookpad.android.marketapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by junya-ogasawara on 2016/08/18.
 */
public class PurchaseRequest {
    @SerializedName("line_items")
    private List<LineItems> lineItems;

    public PurchaseRequest(List<LineItems> lineItems) {
        this.lineItems = lineItems;
    }

    public static class LineItems {
        @SerializedName("item_id")
        private int itemId;
        @SerializedName("quantity")
        private int quantity;

        public LineItems(int itemId, int quantity) {
            this.itemId = itemId;
            this.quantity = quantity;
        }
    }
}
