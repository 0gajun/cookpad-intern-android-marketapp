package com.cookpad.android.marketapp.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cookpad.android.marketapp.R;
import com.cookpad.android.marketapp.adapter.RecommendedAdapter;
import com.cookpad.android.marketapp.api.MarketServiceHolder;
import com.cookpad.android.marketapp.databinding.RecommendedItemsFragmentBinding;
import com.cookpad.android.marketapp.model.Item;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
public class RecommendedItemsFragment extends Fragment {
    public static String TITLE = "おすすめ商品";

    private RecommendedItemsFragmentBinding binding;

    public static RecommendedItemsFragment newInstance() {
        return new RecommendedItemsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.recommended_items_fragment, container, false);

        RecommendedAdapter adapter = new RecommendedAdapter();
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        adapter.setListener(new RecommendedAdapter.ClickListener() {
            @Override
            public void onClickItem(Item item, View view) {
                startDetailActivity(item);
            }
        });

        updateRecommendedList();


        return binding.getRoot();
    }

    private void updateRecommendedList() {
        MarketServiceHolder.getMarketService().getRecommendedItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Item>>() {
                    @Override
                    public void call(List<Item> items) {
                        RecommendedAdapter adapter = (RecommendedAdapter) binding.recyclerView.getAdapter();
                        if (adapter != null) {
                            adapter.replace(items);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void startDetailActivity(Item item) {
        if (getActivity().isFinishing()) {
            return;
        }
        Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
        intent.putExtra(ItemDetailActivity.INTENT_KEY.ITEM.name(), item);
        startActivity(intent);
    }
}
