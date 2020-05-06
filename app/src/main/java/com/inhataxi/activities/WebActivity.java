package com.inhataxi.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.inhataxi.R;

public class WebActivity extends AppCompatActivity {

    WebView mWebView;
    Context mContext;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mContext=this;
        mWebView=(WebView)findViewById(R.id.best_price_web_view);

        WebSettings mws=mWebView.getSettings();//Mobile Web Setting
        mws.setJavaScriptEnabled(true);//자바스크립트 허용
        mws.setLoadWithOverviewMode(true);//컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        Intent intent = getIntent();
        url = intent.getExtras().getString("url");
        mWebView.loadUrl(url);


    }

    public void customOnClick(final View view) {
        switch (view.getId()) {
            case R.id.best_price_iv_back:
                finish();
                break;
            default:
                break;
        }
    }
}
