package com.cookpad.android.marketapp.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.cookpad.android.marketapp.R;
import com.cookpad.android.marketapp.adapter.RecommendedAdapter;
import com.cookpad.android.marketapp.api.MarketServiceHolder;
import com.cookpad.android.marketapp.databinding.ActivityMainBinding;
import com.cookpad.android.marketapp.model.Item;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        RecommendedAdapter adapter = new RecommendedAdapter();
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setListener(new RecommendedAdapter.ClickListener() {
            @Override
            public void onClickItem(Item item, View view) {
                Toast.makeText(MainActivity.this, item.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        updateRecommendedList();
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
}
