package com.digidot.ishansupportsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.digidot.ishansupportsystem.R;
import com.digidot.ishansupportsystem.util.Constant;


public class SplashScreenActivity extends AppCompatActivity {
    private final String TAG="SplashScreenActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                // close this activity
                finish();
                Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(i);

            }
        }, Constant.SPLASH_TIME_OUT);
    }
}