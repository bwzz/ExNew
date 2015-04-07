package com.bwzz.common.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bwzz.common.activity.ReusingActivityHelper;
import com.bwzz.common.utils.L;
import com.bwzz.common.utils.ViewHelper;
import com.bwzz.exnew.BuildConfig;
import com.bwzz.exnew.R;

/**
 * Created by wanghb on 14/12/10.
 */
public abstract class BaseFragment extends Fragment {

    private View parent;

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(getLayoutResId(), container, false);
        setupView(inflater, parent, savedInstanceState);
        return parent;
    }

    protected String getTextString(int id) {
        if (parent == null) {
            return null;
        }
        TextView tv = null;
        if (BuildConfig.DEBUG) {
            tv = (TextView) parent.findViewById(id);
        } else {
            View view = parent.findViewById(id);
            if (view instanceof TextView) {
                tv = (TextView) view;
            }
        }
        if (tv == null) {
            return null;
        } else {
            return tv.getText().toString();
        }
    }

    protected View findViewById(int id) {
        if (parent == null) {
            return null;
        }
        return parent.findViewById(id);
    }

    protected TextView setTextString(int id, String string) {
        if (parent == null) {
            return null;
        }
        if (BuildConfig.DEBUG) {
            TextView tv = (TextView) parent.findViewById(id);
            if (tv != null) {
                tv.setText(string);
                ViewHelper.setCursorToTextEnd(tv);
            }
            return tv;
        } else {
            View view = parent.findViewById(id);
            if (view instanceof TextView) {
                ((TextView) view).setText(string);
                ViewHelper.setCursorToTextEnd(view);
                return (TextView) view;
            } else {
                L.e("setTextString to view who is not a TextView : ", string);
                return null;
            }
        }
    }

    protected abstract int getLayoutResId();

    protected void setupView(LayoutInflater inflater, View view,
            Bundle savedInstanceState) {

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
        overridePendingTransition(R.anim.push_in, R.anim.alpha_out);
    }

    public void finish() {
        finish(Activity.RESULT_CANCELED, null);
    }

    public void finish(int resultCode, Intent data) {
        if (getActivity() != null) {
            getActivity().setResult(resultCode, data);
            getActivity().finish();
        }
        if (getArguments() != null && getArguments().getBoolean("launchedByModal")) {
            overridePendingTransition(R.anim.alpha_in, R.anim.modal_out);
        } else {
            overridePendingTransition(R.anim.alpha_in, R.anim.push_out);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        L.i("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        L.i("onPause");
    }

    public boolean interceptOnBackPressed() {
        finish();
        return true;
    }

    public void launchModal(Class<? extends Fragment> fragment,
            Bundle args4fragment, int reqCode) {
        if (args4fragment == null) {
            args4fragment = new Bundle();
        }
        args4fragment.putBoolean("launchedByModal", true);
        launch(fragment, args4fragment, reqCode);
        if (getActivity() != null) {
            overridePendingTransition(R.anim.modal_in,
                    R.anim.alpha_out);
        }
    }

    public void launchModal(Class<? extends Fragment> fragment,
            Bundle args4fragment) {
        launchModal(fragment, args4fragment, 0);
    }

    protected void finishModal() {
        finishModal(Activity.RESULT_CANCELED, null);
    }

    protected void finishModal(int resultCode, Intent data) {
        if (getActivity() != null) {
            getActivity().setResult(resultCode, data);
            getActivity().finish();
            overridePendingTransition(R.anim.alpha_in,
                    R.anim.modal_out);
        }
    }

    private void overridePendingTransition(int enter, int exit) {
        if (getActivity() != null) {
            getActivity().overridePendingTransition(enter, exit);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * END
     */
}
