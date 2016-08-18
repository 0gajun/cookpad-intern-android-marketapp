package com.cookpad.android.marketapp.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cookpad.android.marketapp.R;
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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cell_purchase_confirmation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CartItem cartItem = cartItems.get(position);
        final Context context = holder.binding.getRoot().getContext();
        holder.binding.itemName.setText(context.getString(R.string.purchase_confirm_item_name, cartItem.name, cartItem.count));
        holder.binding.itemPrice.setText(context.getString(R.string.purchase_confirm_amount, cartItem.price * cartItem.count));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems.clear();
        this.cartItems.addAll(cartItems);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CellPurchaseConfirmationBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
