package com.track.trackingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.track.trackingapp.Adapters.CategoryListAdapter;
import com.track.trackingapp.GlobalClass.Constants;
import com.track.trackingapp.GlobalClass.PreferenceHelper;
import com.track.trackingapp.R;
import com.track.trackingapp.models.CategoryModel;
import com.track.trackingapp.restApi.ApiManager;
import com.track.trackingapp.restApi.ApiResponseInterface;
import com.track.trackingapp.restApi.AppConstant;
import com.track.trackingapp.restApi.Response.BaseReponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryListActivity extends AppCompatActivity {
    private ApiManager mApiManager;
    private ApiResponseInterface mInterFace;
    ArrayList<CategoryModel> categoryModels= new ArrayList<>();;
    ArrayList<CategoryModel> ListModels= new ArrayList<>();;
    CategoryListAdapter categoryListAdapter;

    @BindView(R.id.recycleryVieCategoryList)
    RecyclerView recycleryVieCategoryList;
    @BindView(R.id.imgNoData)
    ImageView imgNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        ButterKnife.bind(this);

        setupNetwork();
        callCategoryList();
    }

    public void callCategoryList() {
        if (Constants.checkInternet(CategoryListActivity.this)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", PreferenceHelper.getString(Constants.user_id, ""));
            params.put("device_id", PreferenceHelper.getString(Constants.token, ""));
            mApiManager.makeCommonRequest(params, AppConstant.CATEGORYLIST);
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
                    Constants.showNoInternetDialog(CategoryListActivity.this);
                }
            }

            @Override
            public void isUserDisabled(String errorMsg, int errorCode) {

            }

            @Override
            public void isSuccess(Object response, int ServiceCode) {
                if (ServiceCode == AppConstant.CATEGORYLIST) {
                    System.out.println("loginModels Response:" + String.valueOf(response.toString()));
                    BaseReponseBody res = (BaseReponseBody) response;
                    Toast.makeText(CategoryListActivity.this, res.getMsg().toString(), Toast.LENGTH_LONG).show();

                    if (res.getStatus() == 1) {
                        categoryModels = res.getCategories();

                        if (categoryModels.size() > 0) {
                            imgNoData.setVisibility(View.GONE);
                            recycleryVieCategoryList.setVisibility(View.VISIBLE);
                            for (int i = 0; i < categoryModels.size(); i++) {

                                CategoryModel listModel = new CategoryModel();
                                listModel.setId(categoryModels.get(i).getId());
                                listModel.setName(categoryModels.get(i).getName());
                                listModel.setType_id(categoryModels.get(i).getType_id());
                                listModel.setType(categoryModels.get(i).getType());
                                ListModels.add(listModel);
                            }
                            recycleryVieCategoryList.setLayoutManager(new LinearLayoutManager(CategoryListActivity.this.getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                            categoryListAdapter = new CategoryListAdapter(getApplicationContext(), ListModels);
                            recycleryVieCategoryList.setAdapter(categoryListAdapter);
                        }else {
                            imgNoData.setVisibility(View.VISIBLE);
                            recycleryVieCategoryList.setVisibility(View.GONE);
                        }
                    } else {

                    }
                    //invalidTokenshowDialog(CategoryListActivity.this);

                }
            }
        };
        mApiManager = new ApiManager(this, mInterFace);
    }

}