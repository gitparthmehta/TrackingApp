package com.track.trackingapp.restApi;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.track.trackingapp.restApi.Response.BaseReponseBody;
import com.track.trackingapp.restApi.Response.CommonBaseResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiManager {
    private Context mContext;
    private ProgressDialog dialog;
    private ApiResponseInterface mApiResponseInterface;
    //private SessionManager sessionManager;

    public ApiManager(Context context, ApiResponseInterface apiResponseInterface) {
        this.mContext = context;
        this.mApiResponseInterface = apiResponseInterface;
        dialog = new ProgressDialog(mContext);
        //sessionManager = new SessionManager(mContext);
    }

//    public void getRole(Map<String, String> params, final int requestCode) {
//
//        showDialog("Get Role...");
//        ApiInterface apiService =
//                ApiClient.getClient().create(ApiInterface.class);
//        Call<CommonBaseResponse> call = apiService.getRole();
//        call.enqueue(new Callback<CommonBaseResponse>() {
//            @Override
//            public void onResponse(Call<CommonBaseResponse> call, Response<CommonBaseResponse> response) {
//                closeDialog();
//
//                if (response.body() != null) {
//                    if (response.body().getStatus().equalsIgnoreCase("true")) {
//                        mApiResponseInterface.isSuccess(response.body(), requestCode);
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<CommonBaseResponse> call, Throwable t) {
//                closeDialog();
//                Log.e("error", t.getMessage());
//                mApiResponseInterface.isError("Network Error", AppConstant.NO_NETWORK_ERROR_CODE);
//                //Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
//                //Constants.showNoInternetDialog(mContext);
//            }
//        });
//
//    }

    public void makeCommonRequest(Map<String, String> params, final int requestCode) {
        showDialog("Please Wait..");

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<BaseReponseBody> call = null;
        if (requestCode == AppConstant.LOGIN) {
            call = apiService.doLogin(params);
        }
        if (requestCode == AppConstant.OTP) {
            call = apiService.doOtp(params);
        }
        if (requestCode == AppConstant.CHECKIN) {
            call = apiService.doCheckIn(params);
        }
        if (requestCode == AppConstant.CHECKOUT) {
            call = apiService.doCheckOut(params);
        }
        if (requestCode == AppConstant.FORGOTPASSWORD) {
            call = apiService.doForgotpassword(params);
        }
        if (requestCode == AppConstant.CHANGEPASSWORD) {
            call = apiService.doChangepassword(params);
        }
        if (requestCode == AppConstant.VIEEPROFILE) {
            call = apiService.viewprofile(params);
        }
        if (requestCode == AppConstant.USERSTATUS) {
            call = apiService.Getstatus(params);
        }
        if (requestCode == AppConstant.ALLEMPLOYEELIST) {
            call = apiService.GetAllEmployeeList(params);
        }
        if (requestCode == AppConstant.CATEGORYLIST) {
            call = apiService.GetCategoryList(params);
        }
        if (requestCode == AppConstant.PRODUCTLIST) {
            call = apiService.GetProductList(params);
        }

        call.enqueue(new Callback<BaseReponseBody>() {
            @Override
            public void onResponse(Call<BaseReponseBody> call, Response<BaseReponseBody> response) {
                try {
                    closeDialog();
                    String responseString = response.body().toString();
                    System.out.println("Response: " + String.valueOf(responseString));
                    if (!TextUtils.isEmpty(responseString) && responseString.length() > 0) {
                        mApiResponseInterface.isSuccess(response.body(), requestCode);
                    } else {
                        mApiResponseInterface.isError("No Data found", AppConstant.ERROR_CODE);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<BaseReponseBody> call, Throwable t) {
                Log.e("error", t.getMessage());
                mApiResponseInterface.isError("Network Error", AppConstant.NO_NETWORK_ERROR_CODE);
                //Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
                //Constants.showNoInternetDialog(mContext);
                closeDialog();
            }
        });

    }


//    public void makeUpdateProfileRequest(Map<String, RequestBody> params, List<MultipartBody.Part> partList, final int requestCode) {
//        showDialog("Please Wait..");
//
//        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//        Call<BaseReponseBody> call = null;
//
//        if (requestCode == AppConstant.UPDATEPROFILE) {
//            call = apiService.doUpdateProfile(params, partList.get(0), partList.get(1), partList.get(2));
//        }
//        if (requestCode == AppConstant.EDITPROFILE) {
//            call = apiService.doEditProfile(params, partList.get(0));
//        }
//        if (requestCode == AppConstant.UPLOADPOST) {
//            call = apiService.UploadImageForReward(params, partList.get(0));
//        }
//
//        call.enqueue(new Callback<BaseReponseBody>() {
//            @Override
//            public void onResponse(Call<BaseReponseBody> call, Response<BaseReponseBody> response) {
//                try {
//                    closeDialog();
//                    String responseString = response.body().toString();
//                    System.out.println("Response: " + responseString);
//                    if (!TextUtils.isEmpty(responseString) && responseString.length() > 0) {
//                        mApiResponseInterface.isSuccess(response.body(), requestCode);
//                    } else {
//                        mApiResponseInterface.isError("No Data found", AppConstant.ERROR_CODE);
//                    }
//                } catch (NullPointerException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<BaseReponseBody> call, Throwable t) {
//                Log.e("error", t.getMessage());
//                mApiResponseInterface.isError("Network Error", AppConstant.NO_NETWORK_ERROR_CODE);
//                //Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
//                //Constants.showNoInternetDialog(mContext);
//                closeDialog();
//            }
//        });
//
//    }


    /**
     * The purpose of this method is to show the dialog
     *
     * @param message
     */
    private void showDialog(String message) {
        try {
            dialog.setMessage(message);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (final Exception e) {
            // Handle or log or ignore
        }
    }


    /**
     * The purpose of this method is to close the dialog
     */
    private void closeDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog.cancel();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (final Exception e) {
            // Handle or log or ignore
        }
    }

//    public void makeFormDataRequest(Map<String, RequestBody> params, int requestCode) {
//        showDialog("Please Wait..");
//
//        ApiInterface apiService =
//                ApiClient.getClient().create(ApiInterface.class);
//        Call<BaseReponseBody> call = null;
//
//
//        if (requestCode == AppConstant.HOME) {
//            call = apiService.dohomelist(params);
//        }
//        if (requestCode == AppConstant.LEADERBOARD) {
//            call = apiService.GetLeaderboardList(params);
//        }
//        if (requestCode == AppConstant.REWARDCOIN) {
//            call = apiService.GetRewardCoin(params);
//        }
//        if (requestCode == AppConstant.REWARDHISTORY) {
//            call = apiService.GetRewardCoinHistory(params);
//        }
//        if (requestCode == AppConstant.PARTICIPANTSLIST) {
//            call = apiService.GetParticipantsList(params);
//        }
//        if (requestCode == AppConstant.ACTIVITYPOST) {
//            call = apiService.GetActivityPost(params);
//        }
//        if (requestCode == AppConstant.ACTIVITCOIN) {
//            call = apiService.GetCoinsList(params);
//        }
//
//        if (requestCode == AppConstant.FUNDRAISER) {
//            call = apiService.GetFundRaiserList(params);
//        }
//        if (requestCode == AppConstant.FAQ) {
//            call = apiService.GetFAQList(params);
//        }
//
//        if (requestCode == AppConstant.TENUREEXTEND) {
//            call = apiService.GetEcxtendTenture(params);
//        }
//        if (requestCode == AppConstant.DOWNLOADCERTIFICATE) {
//            call = apiService.DownloadCertificate(params);
//        }
//
//        if (requestCode == AppConstant.USERCATEGORY) {
//            call = apiService.GetUserCategory(params);
//        }
//        if (requestCode == AppConstant.DONATE) {
//            call = apiService.Donate(params);
//        }
//        if (requestCode == AppConstant.REDEEM) {
//            call = apiService.Redeem(params);
//        }
//        if (requestCode == AppConstant.EXTENDTENURECHECK) {
//            call = apiService.ExtendTenureCheck(params);
//        }
//
//        call.enqueue(new Callback<BaseReponseBody>() {
//            @Override
//            public void onResponse(Call<BaseReponseBody> call, Response<BaseReponseBody> response) {
//                try {
//                    closeDialog();
//                    String responseString = response.body().toString();
//                    System.out.println("Response: " + responseString);
//                    if (!TextUtils.isEmpty(responseString) && responseString.length() > 0) {
//                        mApiResponseInterface.isSuccess(response.body(), requestCode);
//                    } else {
//                        mApiResponseInterface.isError("No Data found", AppConstant.ERROR_CODE);
//                    }
//                } catch (NullPointerException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<BaseReponseBody> call, Throwable t) {
//                Log.e("error", t.getMessage());
//                mApiResponseInterface.isError("Network Error", AppConstant.NO_NETWORK_ERROR_CODE);
//                //Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
//                //Constants.showNoInternetDialog(mContext);
//                closeDialog();
//            }
//        });
//
//
//    }
}
