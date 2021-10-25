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

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.btnCheckIn)
    Button btnCheckIn;
    @BindView(R.id.btnCheckOut)
    Button btnCheckOut;

    private ApiManager mApiManager;
    private ApiResponseInterface mInterFace;

    @BindView(R.id.userprofile)
    Button userprofile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
        setupNetwork();
        GetStatus();
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
                    Constants.showNoInternetDialog(HomeActivity.this);
                }
            }

            @Override
            public void isUserDisabled(String errorMsg, int errorCode) {

            }

            @Override
            public void isSuccess(Object response, int ServiceCode) {
                if (ServiceCode == AppConstant.CHECKIN) {
                    System.out.println("CHECKIN Response:" + String.valueOf(response.toString()));
                    BaseReponseBody res = (BaseReponseBody) response;
                    Toast.makeText(HomeActivity.this, res.getMsg().toString(), Toast.LENGTH_LONG).show();
                    GetStatus();
                    //invalidTokenshowDialog(LoginActivity.this);

                } else if (ServiceCode == AppConstant.CHECKOUT) {
                    System.out.println("CHECKOUT Response:" + String.valueOf(response.toString()));
                    BaseReponseBody res = (BaseReponseBody) response;
                    Toast.makeText(HomeActivity.this, res.getMsg().toString(), Toast.LENGTH_LONG).show();
                    GetStatus();

                    //invalidTokenshowDialog(LoginActivity.this);

                } else if (ServiceCode == AppConstant.USERSTATUS) {
                    System.out.println("CHECKOUT Response:" + String.valueOf(response.toString()));
                    BaseReponseBody res = (BaseReponseBody) response;
//                    Toast.makeText(HomeActivity.this, res.getMsg().toString(), Toast.LENGTH_LONG).show();

                    String user_status=res.getUser_status();
                    String status= String.valueOf(res.getStatus());

                    if (status.equals("0")){
                        btnCheckIn.setVisibility(View.VISIBLE);
                        btnCheckOut.setVisibility(View.GONE);
                    }
                    if (user_status.equals("2")){

                        btnCheckIn.setVisibility(View.GONE);
                        btnCheckOut.setVisibility(View.VISIBLE);
                    }else {
                        btnCheckIn.setVisibility(View.VISIBLE);
                        btnCheckOut.setVisibility(View.GONE);
                    }

                    //invalidTokenshowDialog(LoginActivity.this);

                }
            }
        };
        mApiManager = new ApiManager(this, mInterFace);
    }

    public void makeCheckIncall() {
        if (Constants.checkInternet(HomeActivity.this)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", PreferenceHelper.getString(Constants.user_id, ""));
            params.put("device_id", PreferenceHelper.getString(Constants.token, ""));
            params.put("lat", "22.9926678");
            params.put("long", "72.4710177");
            mApiManager.makeCommonRequest(params, AppConstant.CHECKIN);
        }
    }

    public void GetStatus() {
        if (Constants.checkInternet(HomeActivity.this)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", PreferenceHelper.getString(Constants.user_id, ""));
            params.put("device_id", PreferenceHelper.getString(Constants.token, ""));
            mApiManager.makeCommonRequest(params, AppConstant.USERSTATUS);
        }
    }

    public void makeCheckOutcall() {
        if (Constants.checkInternet(HomeActivity.this)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", PreferenceHelper.getString(Constants.user_id, ""));
            params.put("device_id", PreferenceHelper.getString(Constants.token, ""));
            params.put("lat", "22.9926678");
            params.put("long", "72.4710177");
            mApiManager.makeCommonRequest(params, AppConstant.CHECKOUT);
        }
    }

    private void clickListner() {

        userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,CategoryListActivity.class);
                startActivity(intent);
            }
        });
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCheckIncall();
            }
        });
        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCheckOutcall();
            }
        });
    }
}