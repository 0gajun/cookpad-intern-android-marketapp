package com.cookpad.android.marketapp.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.cookpad.android.marketapp.R;
import com.cookpad.android.marketapp.databinding.ItemListActivityBinding;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
public class ItemListActivity extends AppCompatActivity {
    private ItemListActivityBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.item_list_activity);

        // TODO: ファクトリでまとめる
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_holder, getFragment());
        transaction.commit();
    }

    protected Fragment getFragment() {
        return ItemListFragment.newInstance(new Bundle());
    }
}
