package com.track.trackingapp.GlobalClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.track.trackingapp.Activities.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constants {
    public static final String developerkey = "AIzaSyBlgYB7isC9pQnCg7P_Rqd5sYUPeiBfAt4";
    public static final String user_id = "user_id";
    public static final String doneProfile = "";
    public static final String user_name = "user_name";
    public static final String end_date = "end_date";
    public static final String mobile_number = "mobile_number";
    public static final String token = "token";
    public static final String clicked_id = "clicked_id";
    public static final String clicked_name = "clicked_name";
    public static final String category_id = "category_id";
    public static final String designation_id = "designation_id";
    public static final String is_verified = "is_verified";
    public static final String profileimage = "profileimage";
    public static final String participant_category = "participant_category";
    public static final String participant_category_title = "participant_category_title";
    public static String chooseDateTenureCompletion = "";

    public static AlertDialog dialog;

    public static String passwordPattern = "^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{8,}" +               //at least 8 characters
            "$";
    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public static String getYoutubeVideoId(String youtubeUrl) {
        String videoId = "";
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0 && youtubeUrl.startsWith("http")) {

            String expression = "^.*((youtu.be" + "/)" + "|(v/)|(/u/w/)|(embed/)|(watch\\?))\\??v?=?([^#&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(youtubeUrl);
            if (matcher.matches()) {
                String groupIndex1 = matcher.group(7);
                if (groupIndex1 != null && groupIndex1.length() == 11)
                    videoId = groupIndex1;

            }

        }
        return videoId;
    }

    public static void invalidTokenshowDialog(Activity activity) {
        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(activity).create();
        alertDialog.setTitle("Sorry !!");
        alertDialog.setMessage("Please Login again.");
        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PreferenceHelper.putString(Constants.token, "");
                        PreferenceHelper.putString(Constants.user_id, "");
                        Intent intent = new Intent(activity, LoginActivity.class);
                        activity.startActivity(intent);
                        activity.finish();

                    }
                });
        alertDialog.show();
    }

    public static void errorMsgDialog(Activity activity, String msg) {
        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(activity).create();
        alertDialog.setTitle("Error !!");
        alertDialog.setMessage(msg);
        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        alertDialog.show();
    }

    public static boolean checkInternet(Activity activity) {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            System.out.println("Network available.");
            if (getInetAddressByName("www.google.com")) {
                System.out.println("google find successful.");
                if (getInetAddressByName("portal.specificstep.com")) {
                    connected = true;
                    System.out.println("specific find successful.");
                } else {
                    connected = false;
                    System.out.println("Please check your mobile data or wifi connection.");
                    showErrorInternetDialog(activity, "Please check your mobile data or wifi connection and try again later.");
                }
            } else {
                connected = false;
                System.out.println("Please check your mobile data connection.");
                showErrorInternetDialog(activity, "Please check your mobile data connection and try again later.");
            }
        } else {
            System.out.println("Network not available.");
            connected = false;
            showNoInternetDialog(activity);
        }
        return connected;
    }

    public static boolean getInetAddressByName(String name) {
        AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    InetAddress address = InetAddress.getByName(params[0]);
                    return !address.equals("");
                } catch (UnknownHostException e) {
                    return false;
                }
            }
        };
        try {
            return task.execute(name).get();
        } catch (InterruptedException e) {
            return false;
        } catch (ExecutionException e) {
            return false;
        }

    }

    public static void showErrorInternetDialog(final Activity context, String msg) {
        try {
            if (dialog == null || !dialog.isShowing()) {
                dialog = new AlertDialog.Builder(context)
                        .setTitle("Error!")
                        .setMessage(msg)
                        .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
                                ((Activity) context).startActivityForResult(settingsIntent, 9003);
                            }
                        })
                        .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void showNoInternetDialog(final Context context) {
        new AlertDialog.Builder(context)
                .setTitle("No Internet")
                .setMessage("Please check your internet connection.")
                .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
                        ((Activity) context).startActivityForResult(settingsIntent, 9003);
                    }
                })
                .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    public static File getBitmapToFile(Bitmap bitmap, File file) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos); // YOU can also save it in JPEG
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return file; // it will return null
        }

    }

    public static String parseDateToddMMyyyy(String time, String inputPattern, String outputPattern) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String formatDate(String dateToFormat, String inputFormat, String outputFormat) {
        try {
            Log.e("DATE", "Input Date Date is " + dateToFormat);

            String convertedDate = new SimpleDateFormat(outputFormat)
                    .format(new SimpleDateFormat(inputFormat)
                            .parse(dateToFormat));

            Log.e("DATE", "Output Date is " + convertedDate);

            //Update Date
            return convertedDate;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }


}
