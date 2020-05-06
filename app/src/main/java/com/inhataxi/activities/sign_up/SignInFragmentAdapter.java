package com.inhataxi.activities.sign_up;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class SignInFragmentAdapter extends FragmentStatePagerAdapter {

    final int BASIC_INFO = 0;
    final int PASS_WORD = 1;

    private int mPageCount=2;

    public SignInFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case BASIC_INFO:
                return new BasicInfoFragment();
            case PASS_WORD:
                return new PasswordFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mPageCount;
    }
}
