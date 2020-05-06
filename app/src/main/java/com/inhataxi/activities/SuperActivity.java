package com.inhataxi.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.speech.SpeechRecognizer;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import io.reactivex.disposables.CompositeDisposable;

public abstract class SuperActivity extends AppCompatActivity {

    Gson mGson = new Gson();
    public CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    ProgressDialog pd;
    SpeechRecognizer stt;
    Intent intent;

    public abstract void onClick(View v);
    abstract void initViews();
    public abstract void initSTT();

    public void showProgressDialog() {
        if(pd == null)
            pd = new ProgressDialog(this);
//        progressDialog.setMessage(getString(R.string.loading_buy_point));
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
    }

    public void dismissProgressDialog() {
        if(pd != null) pd.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initSTT();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (stt != null) {
            stt.cancel();
            stt.destroy();
        }
    }

}
