package com.cookpad.android.marketapp;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.cookpad.android.marketapp.adapter.RecommendedAdapter;
import com.cookpad.android.marketapp.databinding.ActivityMainBinding;
import com.cookpad.android.marketapp.model.Item;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        RecommendedAdapter adapter = new RecommendedAdapter();
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter.add(new Item(0, "Orange", 1000));
        adapter.add(new Item(1, "Apple", 1000));
        adapter.add(new Item(2, "Banana", 1000));
        adapter.notifyDataSetChanged();
    }
}
