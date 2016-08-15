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
import com.cookpad.android.marketapp.model.Item;

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

    private int itemId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.item_detail_activity);
        binding.setActivity(this);

        Item item = (Item) getIntent().getSerializableExtra(INTENT_KEY.ITEM.name());
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
    }

    //TODO: 動かないっぽいので後で直す
    public void onClickAddCartButton(View v) {
        Toast.makeText(this, "submit", Toast.LENGTH_SHORT).show();
    }
}
