package com.inhataxi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.inhataxi.R;

public class SelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_school_home);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_go_school:
                //등교 액티비티로 이동
                Intent intentSchool = new Intent(this, MapSearchActivity.class);
                intentSchool.putExtra("select", 1);
                startActivity(intentSchool);
                break;
            case R.id.rl_go_home:
                //하교 액티비티로 이동
                Intent intentHome = new Intent(this, MapSearchActivity.class);
                intentHome.putExtra("select", 2);
                startActivity(intentHome);
                break;
        }
    }

}
