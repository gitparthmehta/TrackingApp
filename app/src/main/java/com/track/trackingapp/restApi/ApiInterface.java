package com.track.trackingapp.restApi;

import com.track.trackingapp.restApi.Response.BaseReponseBody;
import com.track.trackingapp.restApi.Response.CommonBaseResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ApiInterface {


    @POST("login.php")
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    Call<BaseReponseBody> doLogin(@FieldMap Map<String, String> params);


    @POST("verify_otp.php")
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    Call<BaseReponseBody> doOtp(@FieldMap Map<String, String> params);



}
