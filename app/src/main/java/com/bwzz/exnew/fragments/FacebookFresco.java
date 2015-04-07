package com.bwzz.exnew.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * @author wanghb
 * @date 15/4/7.
 */
public class FacebookFresco extends BaseFragment {

    @Override
    protected void setupView(LayoutInflater inflater, View view, Bundle savedInstanceState) {
        super.setupView(inflater, view, savedInstanceState);
        Fresco.initialize(getActivity().getApplicationContext());
        SimpleDraweeView simpleDraweeView = new SimpleDraweeView(getActivity());
        simpleDraweeView
                .setImageURI(Uri
                        .parse("http://e.hiphotos.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=c50272d3c3cec3fd9f33af27b7e1bf5a/58ee3d6d55fbb2fb3f06d70d4f4a20a44623dc84.jpg"));
        ViewGroup viewGroup = (ViewGroup) view;
        viewGroup.addView(simpleDraweeView);
    }
}
