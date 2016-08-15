package com.cookpad.android.marketapp.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cookpad.android.marketapp.R;
import com.cookpad.android.marketapp.databinding.PurchaseConfirmationActivityBinding;

/**
 * Created by junya-ogasawara on 2016/08/15.
 */
public class PurchaseConfirmationActivity extends AppCompatActivity {

    private PurchaseConfirmationActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.purchase_confirmation_activity);
    }
}
