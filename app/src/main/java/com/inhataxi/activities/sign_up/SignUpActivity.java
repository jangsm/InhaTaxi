package com.inhataxi.activities.sign_up;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.inhataxi.R;
import com.inhataxi.activities.BaseActivity;

public class SignUpActivity extends BaseActivity {

    private TabLayout mTabLayout, mTabIndicator;
    public static NonSwipeableViewPager mViewPagerSignUp;
    private SignInFragmentAdapter mSignInFragmentAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mContext=this;
        init();
        initTab();
    }

    void init() {
        mTabLayout = findViewById(R.id.sign_up_tab);
        mViewPagerSignUp = findViewById(R.id.sign_up_vp);
        mTabIndicator = findViewById(R.id.sign_up_tab_indicator);
    }

    void initTab() {
        mTabLayout.addTab(mTabLayout.newTab().setText("기본정보입력"));
        mTabLayout.addTab(mTabLayout.newTab().setText("학교인증"));

        //탭 터치안되게 막기//
        LinearLayout tabStrip = ((LinearLayout)mTabLayout.getChildAt(0));
        tabStrip.setEnabled(false);
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setClickable(false);
        }
        /////////////////

        mSignInFragmentAdapter = new SignInFragmentAdapter(getSupportFragmentManager());
        mViewPagerSignUp.setAdapter(mSignInFragmentAdapter);

        mTabIndicator.setupWithViewPager(mViewPagerSignUp, true);

        LinearLayout tabIndicatorStrip = ((LinearLayout)mTabIndicator.getChildAt(0));
        tabIndicatorStrip.setEnabled(false);
        for(int i = 0; i < tabIndicatorStrip.getChildCount(); i++) {
            tabIndicatorStrip.getChildAt(i).setClickable(false);
        }

    }

    public void customOnClick(View view) {
        switch (view.getId()) {
            case R.id.sign_up_iv_back:
                backPress();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        backPress();
    }

    private void backPress(){
        int mIndex = mViewPagerSignUp.getCurrentItem();
        if (mIndex == 0) {
            finish();
        } else if (mIndex == 1) {
            mViewPagerSignUp.setCurrentItem(0, true);

        }
    }
}
