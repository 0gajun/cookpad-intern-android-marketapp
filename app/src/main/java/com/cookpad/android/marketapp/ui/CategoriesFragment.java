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
import com.cookpad.android.marketapp.adapter.CategoryAdapter;
import com.cookpad.android.marketapp.api.MarketServiceHolder;
import com.cookpad.android.marketapp.databinding.CategoriesFragmentBinding;
import com.cookpad.android.marketapp.model.Category;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
public class CategoriesFragment extends Fragment {
    public static String TITLE = "カテゴリ一覧";

    private CategoriesFragmentBinding binding;

    public static CategoriesFragment newInstance() {
        return new CategoriesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.categories_fragment, container, false);

        CategoryAdapter adapter = new CategoryAdapter();
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        adapter.setListener(new CategoryAdapter.ClickListener() {
            @Override
            public void onClick(Category category, View v) {
                if (getActivity().isFinishing()) {
                    return;
                }

                Intent intent = new Intent(getActivity(), CategoryItemsActivity.class);
                intent.putExtra(CategoryItemsActivity.INTENT_KEY.CATEGORY_ID.name(), category.getId());
                startActivity(intent);
            }
        });

        updateRecommendedList();
        return binding.getRoot();
    }

    private void updateRecommendedList() {
        MarketServiceHolder.getMarketService().getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Category>>() {
                    @Override
                    public void call(List<Category> categories) {
                        CategoryAdapter adapter = (CategoryAdapter) binding.recyclerView.getAdapter();
                        adapter.setCategories(categories);
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
