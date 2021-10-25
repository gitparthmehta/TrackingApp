package com.track.trackingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.track.trackingapp.R;
import com.track.trackingapp.restApi.ApiManager;
import com.track.trackingapp.restApi.ApiResponseInterface;

public class ForgotActivity extends AppCompatActivity {

    private ApiManager mApiManager;
    private ApiResponseInterface mInterFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
    }
}