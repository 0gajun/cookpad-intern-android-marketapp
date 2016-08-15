package com.cookpad.android.marketapp.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.cookpad.android.marketapp.R;
import com.cookpad.android.marketapp.databinding.CellRecommendedBinding;
import com.cookpad.android.marketapp.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<Item> items = new ArrayList<>();
    private ClickListener listener;
    private ClickCountModBtnListener modBtnListener;

    public interface ClickListener {
        void onClickItem(Item item, View view);
    }

    public interface ClickCountModBtnListener {
        void onClickIncrement(Item item, View view);
        void onClickDecrement(Item item, View view);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cell_recommended, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Item item = items.get(position);
        final Context context = holder.itemView.getContext();
        holder.binding.itemName.setText(item.getName());
        holder.binding.itemPrice.setText(context.getString(R.string.item_price_holder, item.getPrice()));

        if (item.getCount() == 0) {
            holder.binding.itemCount.setVisibility(View.GONE);
            holder.binding.countModBtns.setVisibility(View.GONE);
        } else {
            holder.binding.itemCount.setVisibility(View.VISIBLE);
            holder.binding.countModBtns.setVisibility(View.VISIBLE);
            holder.binding.itemCount.setText(context.getString(R.string.item_count_holder, item.getCount()));
        }

        Glide.with(context).load(item.getImageUrl()).into(holder.binding.itemThumbnail);

        setOnClickListeners(holder, item);
    }

    private void setOnClickListeners(final ViewHolder holder, final Item item) {
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClickItem(item, view);
                }
            }
        });

        holder.binding.incrementCountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    modBtnListener.onClickIncrement(item, view);
                }
            }
        });

        holder.binding.decrementCountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    modBtnListener.onClickDecrement(item, view);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void add(Item item) {
        this.items.add(item);
    }

    public void replace(List<Item> items) {
        this.items.clear();
        this.items.addAll(items);
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public void setModBtnListener(ClickCountModBtnListener modBtnListener) {
        this.modBtnListener = modBtnListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CellRecommendedBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
