package com.track.trackingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.track.trackingapp.Adapters.CategoryListAdapter;
import com.track.trackingapp.Adapters.ProductListAdapter;
import com.track.trackingapp.GlobalClass.Constants;
import com.track.trackingapp.GlobalClass.PreferenceHelper;
import com.track.trackingapp.R;
import com.track.trackingapp.models.CategoryModel;
import com.track.trackingapp.models.ProductModel;
import com.track.trackingapp.restApi.ApiManager;
import com.track.trackingapp.restApi.ApiResponseInterface;
import com.track.trackingapp.restApi.AppConstant;
import com.track.trackingapp.restApi.Response.BaseReponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductListActivity extends AppCompatActivity {
    private ApiManager mApiManager;
    private ApiResponseInterface mInterFace;
    @BindView(R.id.recyclerProductList)
    RecyclerView recyclerProductList;

    @BindView(R.id.imgNoData)
    ImageView imgNoData;
    @BindView(R.id.back)
    ImageView back;

    ProductListAdapter productListAdapter;
    ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
    ;
    ArrayList<ProductModel> ListModels = new ArrayList<>();
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);


        setupNetwork();
        callProductList();

        clickListners();
    }

    private void clickListners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void callProductList() {
        if (Constants.checkInternet(ProductListActivity.this)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", PreferenceHelper.getString(Constants.user_id, ""));
            params.put("device_id", PreferenceHelper.getString(Constants.token, ""));
            params.put("category_id", PreferenceHelper.getString(Constants.category_id, ""));
            mApiManager.makeCommonRequest(params, AppConstant.PRODUCTLIST);
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
                    Constants.showNoInternetDialog(ProductListActivity.this);
                }
            }

            @Override
            public void isUserDisabled(String errorMsg, int errorCode) {

            }

            @Override
            public void isSuccess(Object response, int ServiceCode) {
                if (ServiceCode == AppConstant.PRODUCTLIST) {
                    System.out.println("PRODUCTLIST Response:" + String.valueOf(response.toString()));
                    BaseReponseBody res = (BaseReponseBody) response;
                    Toast.makeText(ProductListActivity.this, res.getMsg().toString(), Toast.LENGTH_LONG).show();

                    if (res.getStatus() == 1) {
                        productModelArrayList = res.getProductModel();

                        if (productModelArrayList.size() > 0) {
                            imgNoData.setVisibility(View.GONE);
                            recyclerProductList.setVisibility(View.VISIBLE);
                            for (int i = 0; i < productModelArrayList.size(); i++) {

                                ProductModel listModel = new ProductModel();
                                listModel.setId(productModelArrayList.get(i).getId());
                                listModel.setName(productModelArrayList.get(i).getName());
                                listModel.setId(productModelArrayList.get(i).getId());
                                listModel.setCategory(productModelArrayList.get(i).getCategory());
                                listModel.setImage(productModelArrayList.get(i).getImage());
                                ListModels.add(listModel);
                            }
                            recyclerProductList.setLayoutManager(new LinearLayoutManager(ProductListActivity.this.getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                            productListAdapter = new ProductListAdapter(getApplicationContext(), ListModels);
                            recyclerProductList.setAdapter(productListAdapter);
                        } else {
                            imgNoData.setVisibility(View.VISIBLE);
                            recyclerProductList.setVisibility(View.GONE);
                        }
                    } else {

                    }
                    //invalidTokenshowDialog(ProductListActivity.this);

                }
            }
        };
        mApiManager = new ApiManager(this, mInterFace);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProductListActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}