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
import com.cookpad.android.marketapp.adapter.ItemAdapter;
import com.cookpad.android.marketapp.api.MarketServiceHolder;
import com.cookpad.android.marketapp.databinding.ItemsFragmentBinding;
import com.cookpad.android.marketapp.model.Item;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
public class ItemListFragment extends Fragment {
    protected ItemsFragmentBinding binding;

    public static ItemListFragment newInstance(Bundle bundle) {
        ItemListFragment f = new ItemListFragment();
        f.setArguments(bundle);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.items_fragment, container, false);

        ItemAdapter adapter = new ItemAdapter();
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        adapter.setListener(new ItemAdapter.ClickListener() {
            @Override
            public void onClickItem(Item item, View view) {
                startDetailActivity(item);
            }
        });

        updateItemList();
        return binding.getRoot();
    }

    protected void updateItemList() {
        MarketServiceHolder.getMarketService().getItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Item>>() {
                    @Override
                    public void call(List<Item> items) {
                        updateList(items);
                    }
                });
    }

    protected void updateList(List<Item> items) {
        ItemAdapter adapter = (ItemAdapter) binding.recyclerView.getAdapter();
        if (adapter != null) {
            adapter.replace(items);
            adapter.notifyDataSetChanged();
        }
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
