package com.track.trackingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.track.trackingapp.R;

public class SplashScreen extends AppCompatActivity {
    Handler handler;
    Boolean showlogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        try {
            Intent intent = getIntent();
            Uri data = intent.getData();
            Log.d("print_data", data.toString());

            showlogin = false;
        } catch (Exception e) {

        }
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent1 = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent1);


            }
        }, 2000);

    }
}

