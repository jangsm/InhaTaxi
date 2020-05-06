package com.inhataxi.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.demono.AutoScrollViewPager;
import com.inhataxi.R;
import com.inhataxi.model.MainEventBanner;

import java.util.ArrayList;


public class AutoBannerFragment extends BaseFragment {

    ImageView imageView;
    Context mContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auto_banner, container, false);
        mContext=getContext();
        setComponentView(view);
        return view;
    }


    @Override
    public void setComponentView(View v) {

    }
}
