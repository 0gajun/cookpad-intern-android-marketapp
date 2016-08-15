package com.cookpad.android.marketapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cookpad.android.marketapp.api.MarketServiceHolder;
import com.cookpad.android.marketapp.model.Item;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
public class CategoryItemsFragment extends ItemListFragment {
    private enum INTENT_KEY {
        CATEGORY_ID
    }

    private int categoryId;

    public static CategoryItemsFragment newInstance(int categoryId) {
        Bundle bundle = new Bundle();
        bundle.putInt(INTENT_KEY.CATEGORY_ID.name(), categoryId);
        CategoryItemsFragment fragment = new CategoryItemsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        categoryId = getArguments().getInt(INTENT_KEY.CATEGORY_ID.name());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void updateItemList() {
        MarketServiceHolder.getMarketService().getCategoryItems(categoryId)
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
