package com.track.trackingapp.restApi.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.track.trackingapp.models.LoginModel;

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


    @SerializedName("data")
    @Expose
    private ArrayList<LoginModel> loginModels;

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

}
