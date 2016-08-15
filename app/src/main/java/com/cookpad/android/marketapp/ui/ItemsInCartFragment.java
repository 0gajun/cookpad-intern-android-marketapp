package com.cookpad.android.marketapp.ui;

import android.os.Bundle;

import com.cookpad.android.marketapp.api.MarketServiceHolder;
import com.cookpad.android.marketapp.model.CartItem;
import com.cookpad.android.marketapp.model.Item;
import com.cookpad.android.marketapp.model.OrmaDatabase;
import com.cookpad.android.marketapp.model.db.OrmaHolder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
public class ItemsInCartFragment extends ItemListFragment {
    public static String TITLE = "カートの中身";

    public static ItemsInCartFragment newInstance() {
        return new ItemsInCartFragment();
    }

    @Override
    protected void updateItemList() {
        final OrmaDatabase orma = OrmaHolder.get(getActivity());
        orma.selectFromCartItem()
                .executeAsObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(new Action1<List<CartItem>>() {
                    @Override
                    public void call(final List<CartItem> cartItems) {
                        //TODO: リファクタ Rx使う
                        /*
                        Observable.from(cartItems).map(new Func1<CartItem, Integer>() {
                            @Override
                            public Integer call(CartItem cartItem) {
                                return cartItem.itemId;
                            }
                        }).toList().subscribe(new Action1<List<Integer>>() {
                            @Override
                            public void call(List<Integer> itemIdsInCart) {


                                update(new HashSet<>(itemIdsInCart), cartItems);
                            }
                        });
                        */
                        Map<Integer, CartItem> map = new HashMap<>();
                        for (CartItem ci : cartItems) {
                            map.put(ci.itemId, ci);
                        }
                        update(map);
                    }
                });
    }

    private void update(final Map<Integer, CartItem> itemMap) {
        MarketServiceHolder.getMarketService()
                .getItems()
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<List<Item>>() {
                    @Override
                    public void call(final List<Item> items) {
                        Observable.from(items)
                                .filter(new Func1<Item, Boolean>() {
                                    @Override
                                    public Boolean call(Item item) {
                                        return itemMap.containsKey(item.getId());
                                    }
                                })
                                .map(new Func1<Item, Item>() {
                                    @Override
                                    public Item call(Item item) {
                                        item.setCount(itemMap.get(item.getId()).count);
                                        return item;
                                    }
                                })
                                .toList()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<List<Item>>() {
                                    @Override
                                    public void call(List<Item> items) {
                                        updateList(items);
                                    }
                                });
                    }
                });
    }
}
