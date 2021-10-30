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

    @POST("checkin.php")
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    Call<BaseReponseBody> doCheckIn(@FieldMap Map<String, String> params);

    @POST("checkout.php")
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    Call<BaseReponseBody> doCheckOut(@FieldMap Map<String, String> params);

    @POST("forgotpassword.php")
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    Call<BaseReponseBody> doForgotpassword(@FieldMap Map<String, String> params);

    @POST("changepassword.php")
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    Call<BaseReponseBody> doChangepassword(@FieldMap Map<String, String> params);

    @POST("profile.php")
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    Call<BaseReponseBody> viewprofile(@FieldMap Map<String, String> params);

    @POST("employee_attendance_status.php")
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    Call<BaseReponseBody> Getstatus(@FieldMap Map<String, String> params);

    @POST("employees.php")
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    Call<BaseReponseBody> GetAllEmployeeList(@FieldMap Map<String, String> params);

    @POST("categories.php")
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    Call<BaseReponseBody> GetCategoryList(@FieldMap Map<String, String> params);

    @POST("products.php")
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    Call<BaseReponseBody> GetProductList(@FieldMap Map<String, String> params);

    @POST("edit_profile.php")
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    Call<BaseReponseBody> SetEditProfile(@FieldMap Map<String, String> params);

    @POST("employee_track_location.php")
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    Call<BaseReponseBody> AddUserLocation(@FieldMap Map<String, String> params);

    @POST("get_location.php")
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    Call<BaseReponseBody> GetUserLocation(@FieldMap Map<String, String> params);


}
