package com.track.trackingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.track.trackingapp.Adapters.CategoryListAdapter;
import com.track.trackingapp.Adapters.UserListAdapter;
import com.track.trackingapp.GlobalClass.Constants;
import com.track.trackingapp.GlobalClass.PreferenceHelper;
import com.track.trackingapp.R;
import com.track.trackingapp.models.CategoryModel;
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

public class UserListActivity extends AppCompatActivity {
    private ApiManager mApiManager;
    private ApiResponseInterface mInterFace;
    ArrayList<LoginModel> loginModels=new ArrayList<>();
    ArrayList<LoginModel> ListModels= new ArrayList<>();;

    UserListAdapter userListAdapter;
    @BindView(R.id.recycleryViewUserList)
    RecyclerView recycleryViewUserList;
    @BindView(R.id.imgNoData)
    ImageView imgNoData;

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
                            imgNoData.setVisibility(View.GONE);
                            recycleryViewUserList.setVisibility(View.VISIBLE);
                            for (int i = 0; i < loginModels.size(); i++) {

                                LoginModel listModel = new LoginModel();
                                listModel.setUser_id(loginModels.get(i).getUser_id());
                                listModel.setFirst_name(loginModels.get(i).getFirst_name());
                                listModel.setLast_name(loginModels.get(i).getLast_name());
                                listModel.setEmail(loginModels.get(i).getEmail());
                                ListModels.add(listModel);
                            }
                            recycleryViewUserList.setLayoutManager(new LinearLayoutManager(UserListActivity.this.getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                            userListAdapter = new UserListAdapter(getApplicationContext(), ListModels);
                            recycleryViewUserList.setAdapter(userListAdapter);
                        }else {
                            imgNoData.setVisibility(View.VISIBLE);
                            recycleryViewUserList.setVisibility(View.GONE);
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