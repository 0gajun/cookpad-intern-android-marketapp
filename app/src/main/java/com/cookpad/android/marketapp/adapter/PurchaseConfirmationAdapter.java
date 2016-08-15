package com.cookpad.android.marketapp.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.cookpad.android.marketapp.databinding.CellPurchaseConfirmationBinding;
import com.cookpad.android.marketapp.model.CartItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
public class PurchaseConfirmationAdapter extends RecyclerView.Adapter<PurchaseConfirmationAdapter.ViewHolder> {
    private List<CartItem> cartItems = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CartItem cartItem = cartItems.get(position);
        holder.binding.itemName.setText(cartItem.name + " × " + cartItem.count + "ケ");
        holder.binding.itemPrice.setText(cartItem.price * cartItem.count + "円");
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CellPurchaseConfirmationBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
