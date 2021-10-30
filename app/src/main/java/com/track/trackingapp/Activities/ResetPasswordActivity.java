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

public class ResetPasswordActivity extends AppCompatActivity {


    @BindView(R.id.edtNewPassword)
    EditText edtNewPassword;

    @BindView(R.id.edtRePassword)
    EditText edtRePassword;

    @BindView(R.id.btnConfirmResetPassword)
    Button btnConfirmResetPassword;
    private ApiManager mApiManager;
    private ApiResponseInterface mInterFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        ButterKnife.bind(this);

        setupNetwork();
        clickListners();
    }

    private void clickListners() {
        btnConfirmResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtNewPassword.getText().toString().equals(edtRePassword.getText().toString()) && edtRePassword.getText().toString().equals(edtNewPassword.getText().toString())) {
                    Toast.makeText(ResetPasswordActivity.this, "Password Matched", Toast.LENGTH_SHORT).show();
                    callResetPassword();
//                    startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));

                } else {
                    Toast.makeText(ResetPasswordActivity.this, getResources().getString(R.string.notmatchpassword_msg), Toast.LENGTH_SHORT).show();

                }


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
                    Constants.showNoInternetDialog(ResetPasswordActivity.this);
                }
            }

            @Override
            public void isUserDisabled(String errorMsg, int errorCode) {

            }

            @Override
            public void isSuccess(Object response, int ServiceCode) {
                if (ServiceCode == AppConstant.CHANGEPASSWORD) {
                    System.out.println("LOGIN Response:" + String.valueOf(response.toString()));
                    BaseReponseBody res = (BaseReponseBody) response;
                    Toast.makeText(ResetPasswordActivity.this, res.getMsg().toString(), Toast.LENGTH_LONG).show();

                    if (res.getStatus() == 1) {

                        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }

                }
            }
        };
        mApiManager = new ApiManager(this, mInterFace);
    }


    private void callResetPassword() {
        if (Constants.checkInternet(ResetPasswordActivity.this)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", PreferenceHelper.getString(Constants.user_id, ""));
            params.put("new_password", edtNewPassword.getText().toString().trim());
            mApiManager.makeCommonRequest(params, AppConstant.CHANGEPASSWORD);
        }
    }


}