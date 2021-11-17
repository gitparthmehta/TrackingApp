package com.track.trackingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.track.trackingapp.Adapters.UserListAdapter;
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

public class AddTaskActivity extends AppCompatActivity {

    @BindView(R.id.edtTitle)
    EditText edtTitle;
    @BindView(R.id.edtdesc)
    EditText edtdesc;
    @BindView(R.id.spinneremployee)
    Spinner spinneremployee;

    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    private ApiManager mApiManager;
    private ApiResponseInterface mInterFace;
    ArrayList<LoginModel> loginModels = new ArrayList<>();
    ArrayAdapter<String> adapterstore;
    public static String designationListName[];
    public static String designationListId[];
    String spinnerValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);
        setupNetwork();
        CallAllUserList();
        clickListners();
    }

    private void clickListners() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAddtask();
            }
        });
    }

    public void CallAllUserList() {
        if (Constants.checkInternet(AddTaskActivity.this)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", PreferenceHelper.getString(Constants.user_id, ""));
            params.put("device_id", PreferenceHelper.getString(Constants.token, ""));
            mApiManager.makeCommonRequest(params, AppConstant.ALLEMPLOYEELIST);
        }
    }

    public void callAddtask() {
        if (Constants.checkInternet(AddTaskActivity.this)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", PreferenceHelper.getString(Constants.user_id, ""));
            params.put("device_id", PreferenceHelper.getString(Constants.token, ""));
            params.put("title", edtTitle.getText().toString());
            params.put("description", edtdesc.getText().toString());
            params.put("assigned_to", String.valueOf(designationListId[spinneremployee.getSelectedItemPosition()]));
            mApiManager.makeCommonRequest(params, AppConstant.ADDTASK);
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
                    Constants.showNoInternetDialog(AddTaskActivity.this);
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
                    Toast.makeText(AddTaskActivity.this, res.getMsg().toString(), Toast.LENGTH_LONG).show();

                    if (res.getStatus() == 1) {
                        loginModels = res.getLoginModels();
                        if (loginModels.size() > 0) {

                            designationListName = new String[loginModels.size() + 1];
                            designationListId = new String[loginModels.size() + 1];


                            designationListName[0] = "Select Employee(optional)";
                            designationListId[0] = "0";

                            for (int i = 0; i < loginModels.size(); i++) {

                                designationListName[i + 1] = loginModels.get(i).getFirst_name();
                                designationListId[i + 1] = String.valueOf(loginModels.get(i).getUser_id());
                            }
                            adapterstore = new ArrayAdapter<String>(AddTaskActivity.this, R.layout.simple_spinner_dropdown_item, designationListName);
                            spinneremployee.setAdapter(adapterstore);

                            if (spinnerValue != null) {
                                int spinnerPosition = adapterstore.getPosition(spinnerValue);
                                spinneremployee.setSelection(spinnerPosition);
                            }
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