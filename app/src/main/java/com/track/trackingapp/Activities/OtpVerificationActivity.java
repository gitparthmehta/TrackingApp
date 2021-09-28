package com.track.trackingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.track.trackingapp.GlobalClass.Constants;
import com.track.trackingapp.GlobalClass.PreferenceHelper;
import com.track.trackingapp.R;
import com.track.trackingapp.restApi.ApiManager;
import com.track.trackingapp.restApi.ApiResponseInterface;
import com.track.trackingapp.restApi.AppConstant;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class OtpVerificationActivity extends AppCompatActivity {

    @BindView(R.id.btnDone)
    Button btnDone;

    @BindView(R.id.otp_view)
    OtpTextView otp_view;

    private ApiManager mApiManager;
    private ApiResponseInterface mInterFace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        ButterKnife.bind(this);

        clickListner();

    }

    private void clickListner() {
        otp_view.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }
            @Override
            public void onOTPComplete(String otp) {
                // fired when user has entered the OTP fully.
                Toast.makeText(OtpVerificationActivity.this, "The OTP is " + otp,  Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void makeOtpCall() {
        if (Constants.checkInternet(OtpVerificationActivity.this)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", PreferenceHelper.getString(Constants.user_id,""));
            params.put("otp", "123456");
            mApiManager.makeCommonRequest(params, AppConstant.LOGIN);
        }
    }

}