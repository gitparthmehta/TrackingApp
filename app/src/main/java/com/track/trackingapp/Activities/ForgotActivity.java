package com.track.trackingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ForgotActivity extends AppCompatActivity {

    private ApiManager mApiManager;
    private ApiResponseInterface mInterFace;

    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.btnResetPassword)
    Button btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        ButterKnife.bind(this);

        setupNetwork();

        clickListners();
    }

    private void clickListners() {
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callForgotPassword();
            }
        });
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
                    Constants.showNoInternetDialog(ForgotActivity.this);
                }
            }

            @Override
            public void isUserDisabled(String errorMsg, int errorCode) {

            }

            @Override
            public void isSuccess(Object response, int ServiceCode) {
                if (ServiceCode == AppConstant.FORGOTPASSWORD) {
                    System.out.println("LOGIN Response:" + String.valueOf(response.toString()));
                    BaseReponseBody res = (BaseReponseBody) response;
                    Toast.makeText(ForgotActivity.this, res.getMsg().toString(), Toast.LENGTH_LONG).show();

                    if (res.getStatus() == 1) {

                        Intent intent=new Intent(ForgotActivity.this,ResetPasswordActivity.class);
                        startActivity(intent);
                    }

                }
            }
        };
        mApiManager = new ApiManager(this, mInterFace);
    }


    private void callForgotPassword() {
        if (Constants.checkInternet(ForgotActivity.this)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("email", edtEmail.getText().toString());
            mApiManager.makeCommonRequest(params, AppConstant.FORGOTPASSWORD);
        }
    }
}