package com.track.trackingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.track.trackingapp.GlobalClass.Constants;
import com.track.trackingapp.GlobalClass.PreferenceHelper;
import com.track.trackingapp.R;
import com.track.trackingapp.models.LoginModel;
import com.track.trackingapp.restApi.ApiManager;
import com.track.trackingapp.restApi.ApiResponseInterface;
import com.track.trackingapp.restApi.AppConstant;
import com.track.trackingapp.restApi.Response.BaseReponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditProfileActivity extends AppCompatActivity {

    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    @BindView(R.id.edtFirstName)
    EditText edtFirstName;

    @BindView(R.id.edtLastName)
    EditText edtLastName;

    @BindView(R.id.edtEmail)
    EditText edtEmail;

    @BindView(R.id.edtMobile)
    EditText edtMobile;
    @BindView(R.id.edtDob)
    TextView edtDob;
    private ApiManager mApiManager;
    private ApiResponseInterface mInterFace;
    ArrayList<LoginModel> loginModels;
    @BindView(R.id.back)
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        setupNetwork();
        callProfileData();

        clickListner();
    }

    private void clickListner() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callEditProfile();
            }
        });
    }

    public void callProfileData() {
        if (Constants.checkInternet(EditProfileActivity.this)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", PreferenceHelper.getString(Constants.user_id, ""));
            params.put("device_id", PreferenceHelper.getString(Constants.token, ""));
            mApiManager.makeCommonRequest(params, AppConstant.VIEEPROFILE);
        }
    }

    public void callEditProfile() {
        if (Constants.checkInternet(EditProfileActivity.this)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", PreferenceHelper.getString(Constants.user_id, ""));
            params.put("device_id", PreferenceHelper.getString(Constants.token, ""));
            params.put("first_name", edtFirstName.getText().toString().trim());
            params.put("last_name", edtLastName.getText().toString().trim());
            params.put("email", edtEmail.getText().toString().trim());
            params.put("mobile", edtMobile.getText().toString().trim());
            params.put("dob", edtDob.getText().toString().trim());
            mApiManager.makeCommonRequest(params, AppConstant.EDITPROFILE);
        }
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
                    Constants.showNoInternetDialog(EditProfileActivity.this);
                }
            }

            @Override
            public void isUserDisabled(String errorMsg, int errorCode) {

            }

            @Override
            public void isSuccess(Object response, int ServiceCode) {
                if (ServiceCode == AppConstant.VIEEPROFILE) {
                    System.out.println("User profile Response:" + String.valueOf(response.toString()));
                    BaseReponseBody res = (BaseReponseBody) response;
                    Toast.makeText(EditProfileActivity.this, res.getMsg().toString(), Toast.LENGTH_LONG).show();

                    if (res.getStatus() == 1) {
                        loginModels = res.getLoginModels();

                        if (loginModels.size() > 0) {
                            edtFirstName.setText(loginModels.get(0).getFirst_name());
                            edtLastName.setText(loginModels.get(0).getLast_name());
                            edtEmail.setText(loginModels.get(0).getEmail());
                            edtMobile.setText(loginModels.get(0).getMobile());
                            edtDob.setText(loginModels.get(0).getDob());
                        }

                    } else {

                    }
                    //invalidTokenshowDialog(EditProfileActivity.this);

                } else if (ServiceCode == AppConstant.EDITPROFILE) {
                    System.out.println("User profile Response:" + String.valueOf(response.toString()));
                    BaseReponseBody res = (BaseReponseBody) response;
                    Toast.makeText(EditProfileActivity.this, res.getMsg().toString(), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(EditProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                    //invalidTokenshowDialog(EditProfileActivity.this);

                }
            }
        };
        mApiManager = new ApiManager(this, mInterFace);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EditProfileActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}