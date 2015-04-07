// Copyright (c) 2013-12-17 wanghb. All rights reserved.
package com.bwzz.common.activity;

import android.os.Bundle;

import com.bwzz.common.fragment.BaseFragment;

/**
 * @author wanghb
 */
public class ReusingActivity extends BaseActivity {

    private ReusingActivityHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (ReusingActivityHelper.isSingleFragmentIntent(this)) {
            helper = new ReusingActivityHelper(this);
        }
        if (helper != null) {
            helper.requestWindowFeature();
            helper.setWindowFlags();
        }
        super.onCreate(savedInstanceState);
        if (helper != null) {
            helper.ensureFragment();
        }
    }

    @Override
    public void onBackPressed() {
        if (helper != null && helper.mFragment instanceof BaseFragment) {
            BaseFragment baseFragment = (BaseFragment) helper.mFragment;
            if (!baseFragment.interceptOnBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}
