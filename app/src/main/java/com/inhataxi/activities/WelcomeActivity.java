package com.inhataxi.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.inhataxi.R;

public class WelcomeActivity extends AppCompatActivity {

    private TextView mTextViewNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mTextViewNickName=findViewById(R.id.activity_welcome_tv_nickname);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        mTextViewNickName.setText(name);
    }

    public void customOnClick(View view) {
        switch (view.getId()) {
            case R.id.activity_welcome_iv_start:
                startActivity(new Intent(getApplication(), MainActivity.class));
                break;
            default:
                break;
        }
    }
}
