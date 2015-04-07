// Copyright (c) 2014-10-10 wanghb@fenbi.com. All rights reserved.
package com.bwzz.common.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import com.bwzz.common.utils.L;
import com.bwzz.exnew.R;

/**
 * @author wanghb
 */
public class BaseActivity extends ActionBarActivity {

    public void launch(Class<? extends Activity> clazz) {
        Intent i = new Intent(this, clazz);
        startActivity(i);
    }

    public void launch(Class<? extends Fragment> fragment,
            Bundle args4fragment) {
        launch(fragment, args4fragment, 0);
    }

    public void launch(Class<? extends Fragment> fragment,
            Bundle args4fragment, int reqCode) {
        Intent in = ReusingActivityHelper.builder(this)
                .setLayoutId(R.layout.non)
                .setContainerId(R.id.container)
                .setFragment(fragment, args4fragment)
                .build();
        if (reqCode != 0) {
            startActivityForResult(in, reqCode);
        } else {
            startActivity(in);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.i("onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.i("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        L.i("onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.i("onDestroy");
    }
    /**
     * END
     */
}
