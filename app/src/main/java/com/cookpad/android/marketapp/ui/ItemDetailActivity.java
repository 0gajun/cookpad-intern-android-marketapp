package com.cookpad.android.marketapp.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cookpad.android.marketapp.R;
import com.cookpad.android.marketapp.api.MarketServiceHolder;
import com.cookpad.android.marketapp.databinding.ItemDetailActivityBinding;
import com.cookpad.android.marketapp.model.CartItem;
import com.cookpad.android.marketapp.model.Item;
import com.cookpad.android.marketapp.model.OrmaDatabase;
import com.cookpad.android.marketapp.model.db.OrmaHolder;

import rx.AsyncEmitter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
public class ItemDetailActivity extends AppCompatActivity {
    public enum INTENT_KEY {
        ITEM_ID, ITEM
    }

    private ItemDetailActivityBinding binding;

    private Item item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.item_detail_activity);
        binding.setActivity(this);

        item = (Item) getIntent().getSerializableExtra(INTENT_KEY.ITEM.name());

        setupView(item);
    }

    // DBにItem保存できたら使う
    private void setupView(int itemId) {
        MarketServiceHolder.getMarketService().getItem(itemId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Item>() {
                    @Override
                    public void call(Item item) {
                        setContent(item);
                    }
                });
    }

    private void setupView(Item item) {
        setContent(item);
    }

    private void setContent(Item item) {
        setTitle(item.getName());
        Glide.with(this).load(item.getImageUrl()).into(binding.itemThumbnail);

        binding.itemDescription.setText(item.getDescription());
        binding.itemPrice.setText(getString(R.string.item_price_holder, item.getPrice()));

        updateCurrentCountInCart();
    }

    private void updateCurrentCountInCart() {
        final OrmaDatabase orma = OrmaHolder.get(this);
        orma.selectFromCartItem().itemIdEq(item.getId())
                .executeAsObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CartItem>() {
                    @Override
                    public void call(CartItem cartItem) {
                        if (cartItem != null) {
                            binding.itemCartCount.setText(getString(R.string.item_count_in_cart, cartItem.count));
                        }
                    }
                });
    }

    public void onClickAddCartButton(View v) {
        addCart();
    }

    private void addCart() {
        final OrmaDatabase orma = OrmaHolder.get(this);

        orma.selectFromCartItem().itemIdEq(item.getId())
                .executeAsObservable()
                .subscribe(new Action1<CartItem>() {
                    @Override
                    public void call(CartItem cartItem) {
                        if (cartItem == null) {
                            createNewCartItem();
                        } else {
                            updateCartItem(cartItem, 1);
                        }
                    }
                });
    }

    private void updateCartItem(final CartItem cartItem, final int addCount) {
        final OrmaDatabase orma = OrmaHolder.get(this);

        Observable.fromAsync(new Action1<AsyncEmitter<CartItem>>() {
            @Override
            public void call(AsyncEmitter<CartItem> emitter) {
                orma.updateCartItem()
                        .idEq(cartItem.id)
                        .count(cartItem.count + addCount)
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
                        Toast.makeText(ItemDetailActivity.this,
                                R.string.toast_msg_success_adding_cart,
                                Toast.LENGTH_SHORT)
                                .show();
                        updateCurrentCountInCart();
                    }
                });

    }

    private void createNewCartItem() {
        final OrmaDatabase orma = OrmaHolder.get(this);
        Observable.fromAsync(new Action1<AsyncEmitter<CartItem>>() {
            @Override
            public void call(AsyncEmitter<CartItem> emitter) {

                CartItem cartItem = new CartItem(item, 1);
                orma.insertIntoCartItem(cartItem);
                emitter.onNext(cartItem);
                emitter.onCompleted();

            }
        }, AsyncEmitter.BackpressureMode.NONE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CartItem>() {
                    @Override
                    public void call(CartItem cartItem) {
                        Toast.makeText(ItemDetailActivity.this,
                                R.string.toast_msg_success_adding_cart,
                                Toast.LENGTH_SHORT)
                                .show();
                        updateCurrentCountInCart();
                    }
                });

    }
}
