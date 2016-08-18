package com.cookpad.android.marketapp.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.cookpad.android.marketapp.R;
import com.cookpad.android.marketapp.adapter.PurchaseConfirmationAdapter;
import com.cookpad.android.marketapp.api.MarketServiceHolder;
import com.cookpad.android.marketapp.databinding.PurchaseConfirmationActivityBinding;
import com.cookpad.android.marketapp.model.CartItem;
import com.cookpad.android.marketapp.model.OrmaDatabase;
import com.cookpad.android.marketapp.model.PurchaseRequest;
import com.cookpad.android.marketapp.model.db.OrmaHolder;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
public class PurchaseConfirmationActivity extends AppCompatActivity {

    private PurchaseConfirmationActivityBinding binding;

    private List<CartItem> cartItems;

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
                        PurchaseConfirmationActivity.this.cartItems = cartItems;
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

        binding.purchaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDoPurchase();
            }
        });
    }

    private void updateCartItemList(List<CartItem> items) {
        if (binding.recyclerView.getAdapter() == null) {
            return;
        }
        PurchaseConfirmationAdapter adapter = (PurchaseConfirmationAdapter) binding.recyclerView.getAdapter();
        adapter.setCartItems(items);
        adapter.notifyDataSetChanged();
    }

    public void onClickDoPurchase() {
        List<PurchaseRequest.LineItems> requests = new ArrayList<>();
        for (CartItem item : cartItems) {
            requests.add(new PurchaseRequest.LineItems(item.itemId, item.count));
        }

        MarketServiceHolder.getMarketService().postOrders(new PurchaseRequest(requests))
                .onErrorReturn(new Func1<Throwable, Object>() {
                    @Override
                    public Object call(Throwable throwable) {
                        //TODO: エラーハンドリング
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        new PurchaseSuccessDialogFragment()
                                .show(PurchaseConfirmationActivity.this.getSupportFragmentManager(), "success");
                        cleanupCart();
                    }
                });
    }

    private void cleanupCart() {
        final OrmaDatabase orma = OrmaHolder.get(this);
        orma.deleteFromCartItem()
                .executeAsObservable()
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public static class PurchaseSuccessDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle("ご購入ありがとうございます")
                    .setMessage("購入処理が完了いたしました。")
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dismiss();
                            if (!getActivity().isFinishing()) {
                                getActivity().finish();
                            }
                        }
                    })
                    .create();
        }
    }

}
