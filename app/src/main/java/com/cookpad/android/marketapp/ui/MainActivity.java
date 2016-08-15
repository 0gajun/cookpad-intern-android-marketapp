package com.cookpad.android.marketapp.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.cookpad.android.marketapp.R;
import com.cookpad.android.marketapp.adapter.MainActivityFragmentPagerAdapter;
import com.cookpad.android.marketapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setViews();
    }

    private void setViews() {
        FragmentManager manager = getSupportFragmentManager();
        MainActivityFragmentPagerAdapter adapter = new MainActivityFragmentPagerAdapter(manager);
        binding.viewPager.setAdapter(adapter);
    }
}
