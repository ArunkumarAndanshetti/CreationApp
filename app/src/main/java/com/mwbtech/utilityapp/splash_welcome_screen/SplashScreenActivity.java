package com.mwbtech.utilityapp.splash_welcome_screen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mwbtech.utilityapp.R;


public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        new Handler().postDelayed(new Runnable() {

            public void run() {
                // TODO Auto-generated method stub
                Intent openActivity = new Intent(getApplicationContext(), WelcomeActivity.class);
                openActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                openActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(openActivity);
                SplashScreenActivity.this.finish();

            }
        }, SPLASH_TIME_OUT);
    }


}
