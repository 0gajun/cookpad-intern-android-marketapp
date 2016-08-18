package com.cookpad.android.marketapp.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.cookpad.android.marketapp.R;
import com.cookpad.android.marketapp.adapter.PurchaseConfirmationAdapter;
import com.cookpad.android.marketapp.databinding.PurchaseConfirmationActivityBinding;
import com.cookpad.android.marketapp.model.CartItem;
import com.cookpad.android.marketapp.model.OrmaDatabase;
import com.cookpad.android.marketapp.model.db.OrmaHolder;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
public class PurchaseConfirmationActivity extends AppCompatActivity {

    private PurchaseConfirmationActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.purchase_confirmation_activity);

        PurchaseConfirmationAdapter adapter = new PurchaseConfirmationAdapter();
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    // TODO: 直すコピペ
    protected void update() {
        final OrmaDatabase orma = OrmaHolder.get(this);
        orma.selectFromCartItem()
                .where("count > 0")
                .executeAsObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(new Action1<List<CartItem>>() {
                    @Override
                    public void call(final List<CartItem> cartItems) {
                        updateViews(cartItems);
                    }
                });
    }

    private void updateViews(List<CartItem> items) {
        int total = 0;
        for (CartItem item : items) {
            total += item.price;
        }
        binding.totalAmount.setText(getString(R.string.total_amount, total));
        updateCartItemList(items);
    }

    private void updateCartItemList(List<CartItem> items) {
        if (binding.recyclerView.getAdapter() == null) {
            return;
        }
        PurchaseConfirmationAdapter adapter = (PurchaseConfirmationAdapter) binding.recyclerView.getAdapter();
        adapter.setCartItems(items);
        adapter.notifyDataSetChanged();
    }
}
