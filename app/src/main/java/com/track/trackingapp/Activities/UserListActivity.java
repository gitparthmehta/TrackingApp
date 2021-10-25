package com.track.trackingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

import butterknife.ButterKnife;

public class UserListActivity extends AppCompatActivity {
    private ApiManager mApiManager;
    private ApiResponseInterface mInterFace;
    ArrayList<LoginModel> loginModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);

        setupNetwork();
        CallAllUserList();

    }
    public void CallAllUserList() {
        if (Constants.checkInternet(UserListActivity.this)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", PreferenceHelper.getString(Constants.user_id, ""));
            params.put("device_id", PreferenceHelper.getString(Constants.token, ""));
            mApiManager.makeCommonRequest(params, AppConstant.ALLEMPLOYEELIST);
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
                    Constants.showNoInternetDialog(UserListActivity.this);
                }
            }

            @Override
            public void isUserDisabled(String errorMsg, int errorCode) {

            }

            @Override
            public void isSuccess(Object response, int ServiceCode) {
                if (ServiceCode == AppConstant.ALLEMPLOYEELIST) {
                    System.out.println("loginModels Response:" + String.valueOf(response.toString()));
                    BaseReponseBody res = (BaseReponseBody) response;
                    Toast.makeText(UserListActivity.this, res.getMsg().toString(), Toast.LENGTH_LONG).show();

                    if (res.getStatus() == 1) {
                        loginModels = res.getLoginModels();

                        if (loginModels.size() > 0) {
                           Log.d("loginModels", String.valueOf(loginModels.size()));
                        }

                    } else {

                    }
                    //invalidTokenshowDialog(UserListActivity.this);

                }
            }
        };
        mApiManager = new ApiManager(this, mInterFace);
    }

}