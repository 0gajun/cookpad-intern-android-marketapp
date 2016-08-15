package com.cookpad.android.marketapp.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cookpad.android.marketapp.R;
import com.cookpad.android.marketapp.databinding.CellRecommendedBinding;
import com.cookpad.android.marketapp.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.ViewHolder> {
    private List<Item> items = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cell_recommended, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.binding.itemName.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void add(Item item) {
        items.add(item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CellRecommendedBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
