package com.cookpad.android.marketapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cookpad.android.marketapp.R;
import com.cookpad.android.marketapp.adapter.ItemAdapter;
import com.cookpad.android.marketapp.api.MarketServiceHolder;
import com.cookpad.android.marketapp.model.CartItem;
import com.cookpad.android.marketapp.model.Item;
import com.cookpad.android.marketapp.model.OrmaDatabase;
import com.cookpad.android.marketapp.model.db.OrmaHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.AsyncEmitter;
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

    private Map<Integer, CartItem> cartItemMap;

    public static ItemsInCartFragment newInstance() {
        return new ItemsInCartFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        ItemAdapter adapter = (ItemAdapter) binding.recyclerView.getAdapter();
        adapter.setModBtnListener(new ItemAdapter.ClickCountModBtnListener() {
            @Override
            public void onClickIncrement(Item item, View view) {
                incrementCartItem(item);
            }

            @Override
            public void onClickDecrement(Item item, View view) {
                decrementCartItem(item);
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        // FIXME: 2回呼ばれてる(Baseクラスでも)
        updateItemList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.send_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.send_action:
                doPurchase();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void doPurchase() {

    }

    @Override
    protected void updateItemList() {
        final OrmaDatabase orma = OrmaHolder.get(getActivity());
        orma.selectFromCartItem()
                .where("count > 0")
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
                        updateListView(map);
                    }
                });
    }

    private void updateListView(final Map<Integer, CartItem> itemMap) {
        cartItemMap = itemMap;
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
                                        item.setCount(cartItemMap.get(item.getId()).count);
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

    private void incrementCartItem(Item item) {
        updateCartItem(item, 1);
    }

    private void decrementCartItem(Item item) {
        updateCartItem(item, -1);
    }

    // TODO: CartItemはAdapter分けるべき
    // TODO: トランザクションはらないとおかしなことになるかも
    private void updateCartItem(final Item target, final int delta) {
        final OrmaDatabase orma = OrmaHolder.get(getActivity());
        orma.selectFromCartItem().itemIdEq(target.getId())
                .executeAsObservable()
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<CartItem>() {
                    @Override
                    public void call(final CartItem cartItem) {
                        Observable.fromAsync(new Action1<AsyncEmitter<CartItem>>() {
                            @Override
                            public void call(AsyncEmitter<CartItem> emitter) {
                                orma.updateCartItem()
                                        .itemIdEq(target.getId())
                                        .count(cartItem.count + delta)
                                        .execute();
                                emitter.onNext(null); //FIXME
                                emitter.onCompleted();
                            }
                        }, AsyncEmitter.BackpressureMode.NONE)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<CartItem>() {
                                    @Override
                                    public void call(CartItem cartItem) {
                                        updateItemList();
                                    }
                                });

                    }
                });


    }
}
