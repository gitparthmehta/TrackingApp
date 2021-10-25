package com.track.trackingapp.restApi.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.track.trackingapp.models.CategoryModel;
import com.track.trackingapp.models.LoginModel;
import com.track.trackingapp.models.ProductModel;

import java.util.ArrayList;

public class BaseReponseBody {
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("message")
    @Expose
    private String msg;
    @SerializedName("token")
    @Expose
    private String token;

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    @SerializedName("user_status")
    @Expose
    private String user_status;


    @SerializedName("data")
    @Expose
    private ArrayList<LoginModel> loginModels;

    @SerializedName("categories")
    @Expose
    private ArrayList<CategoryModel> categories;
    @SerializedName("products")
    @Expose
    private ArrayList<ProductModel> productModel;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<LoginModel> getLoginModels() {
        return loginModels;
    }

    public ArrayList<CategoryModel> getCategories() {
        return categories;
    }
    public ArrayList<ProductModel> getProductModel() {
        return productModel;
    }

}
