package com.inhataxi.activities;

import android.view.View;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    public static String sSignUpId;

    public static String sPassword;

    public abstract void setComponentView(View v);
}
