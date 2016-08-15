package com.cookpad.android.marketapp.ui;

import com.cookpad.android.marketapp.api.MarketServiceHolder;
import com.cookpad.android.marketapp.model.Item;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
public class RecommendedItemsFragment extends ItemListFragment {
    public static String TITLE = "おすすめ商品";

    public static RecommendedItemsFragment newInstance() {
        return new RecommendedItemsFragment();
    }

    protected void updateItemList() {
        MarketServiceHolder.getMarketService().getRecommendedItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Item>>() {
                    @Override
                    public void call(List<Item> items) {
                        updateList(items);
                    }
                });
    }
}
