package com.track.trackingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
    ArrayList<CategoryModel> categoryModels = new ArrayList<>();
    ;
    ArrayList<CategoryModel> ListModels = new ArrayList<>();
    ArrayList<CategoryModel> filteredList = new ArrayList<>();
    ;
    CategoryListAdapter categoryListAdapter;

    @BindView(R.id.recycleryVieCategoryList)
    RecyclerView recycleryVieCategoryList;
    @BindView(R.id.imgNoData)
    ImageView imgNoData;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.inputSearch)
    EditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        ButterKnife.bind(this);

        setupNetwork();
        callCategoryList();
        clickListners();
    }

    private void clickListners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence query, int arg1, int arg2, int arg3) {
                // When user changed the Text
                filteredList.clear();

                query = query.toString().toLowerCase();


                for (int i = 0; i < ListModels.size(); i++) {

                    String text = ListModels.get(i).getName();
                    text = text.toLowerCase();
                    //Log.e("TAG"+i,text);
                    if (text.contains(query)) {
                        Log.e("TAGTEXT" + i, text);
                        filteredList.add(ListModels.get(i));
                    }
                }
                recycleryVieCategoryList.setAdapter(null);
                recycleryVieCategoryList.setLayoutManager(new LinearLayoutManager(CategoryListActivity.this.getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                categoryListAdapter = new CategoryListAdapter(getApplicationContext(), ListModels);
                recycleryVieCategoryList.setAdapter(categoryListAdapter);
                categoryListAdapter.notifyDataSetChanged();
                // data set changed

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

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
                                listModel.setImage(categoryModels.get(i).getImage());
                                ListModels.add(listModel);
                            }
                            recycleryVieCategoryList.setLayoutManager(new LinearLayoutManager(CategoryListActivity.this.getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                            categoryListAdapter = new CategoryListAdapter(getApplicationContext(), ListModels);
                            recycleryVieCategoryList.setAdapter(categoryListAdapter);
                        } else {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CategoryListActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}