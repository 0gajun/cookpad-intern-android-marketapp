package com.cookpad.android.marketapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cookpad.android.marketapp.ui.CategoriesFragment;
import com.cookpad.android.marketapp.ui.RecommendedItemsFragment;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
public class MainActivityFragmentPagerAdapter extends FragmentPagerAdapter {
    public MainActivityFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return RecommendedItemsFragment.newInstance();
            case 1:
                return CategoriesFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return RecommendedItemsFragment.TITLE;
            case 1:
                return CategoriesFragment.TITLE;
        }
        return super.getPageTitle(position);
    }
}
