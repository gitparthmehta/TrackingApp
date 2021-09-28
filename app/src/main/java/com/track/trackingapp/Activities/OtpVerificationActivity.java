package com.track.trackingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.track.trackingapp.GlobalClass.Constants;
import com.track.trackingapp.GlobalClass.PreferenceHelper;
import com.track.trackingapp.R;
import com.track.trackingapp.restApi.ApiManager;
import com.track.trackingapp.restApi.ApiResponseInterface;
import com.track.trackingapp.restApi.AppConstant;
import com.track.trackingapp.restApi.Response.BaseReponseBody;

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
        setupNetwork();
        clickListner();


    }

    private void setupNetwork() {
        mInterFace = new ApiResponseInterface() {

            @Override
            public void isError(String errorMsg, int errorCode) {
                if (errorCode == AppConstant.ERROR_CODE) {
                    // error from server
                    //showStatusDialog(errorMsg);
                } else if (errorCode == AppConstant.NO_NETWORK_ERROR_CODE) {
                    // show no network screen with refresh button
                    Constants.showNoInternetDialog(OtpVerificationActivity.this);
                }
            }

            @Override
            public void isUserDisabled(String errorMsg, int errorCode) {

            }

            @Override
            public void isSuccess(Object response, int ServiceCode) {
                if (ServiceCode == AppConstant.OTP) {
                    System.out.println("Otp Response:" + String.valueOf(response.toString()));
                    BaseReponseBody res = (BaseReponseBody) response;
                    Toast.makeText(OtpVerificationActivity.this, res.getMsg().toString(), Toast.LENGTH_LONG).show();

                    if (res.getStatus() == 1) {
                        Intent intent = new Intent(OtpVerificationActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {

                    }
                    //invalidTokenshowDialog(LoginActivity.this);

                }
            }
        };
        mApiManager = new ApiManager(this, mInterFace);
    }

    private void clickListner() {

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeOtpCall();
            }
        });
        otp_view.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }

            @Override
            public void onOTPComplete(String otp) {
                // fired when user has entered the OTP fully.
                Toast.makeText(OtpVerificationActivity.this, "The OTP is " + otp, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void makeOtpCall() {
        if (Constants.checkInternet(OtpVerificationActivity.this)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", PreferenceHelper.getString(Constants.user_id, ""));
            params.put("otp", "123456");
            mApiManager.makeCommonRequest(params, AppConstant.OTP);
        }
    }

}