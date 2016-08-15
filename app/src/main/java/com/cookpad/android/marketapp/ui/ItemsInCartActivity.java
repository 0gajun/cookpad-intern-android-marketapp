package com.cookpad.android.marketapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
public class ItemsInCartActivity extends ItemListActivity {

    @Override
    protected Fragment getFragment() {
        return ItemsInCartFragment.newInstance(new Bundle());
    }
}
