package com.cookpad.android.marketapp.ui;

import android.support.v4.app.Fragment;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
public class CategoryItemsActivity extends ItemListActivity {

    public enum INTENT_KEY {
        CATEGORY_ID
    }

    @Override
    protected Fragment getFragment() {
        final int categoryId = getIntent().getIntExtra(INTENT_KEY.CATEGORY_ID.name(), -1);

        if (categoryId < 0) {
            throw new IllegalArgumentException();
        }

        return CategoryItemsFragment.newInstance(categoryId);
    }

}
