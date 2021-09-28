 package com.track.trackingapp.restApi;


 import com.google.gson.Gson;
 import com.google.gson.GsonBuilder;
 import com.track.trackingapp.BuildConfig;

 import java.util.concurrent.TimeUnit;

 import okhttp3.OkHttpClient;
 import okhttp3.logging.HttpLoggingInterceptor;
 import retrofit2.Retrofit;
 import retrofit2.converter.gson.GsonConverterFactory;

 public class ApiClient {
//    public static final String BASE_URL = "http://petacodes.com/payroll_dev/webservice/";
    public static final String BASE_URL = "https://www.dikshtech.com/trackingsystem/api/";

     private static Retrofit retrofit = null;

     public static Retrofit getClient() {
         HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
         interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
 //        OkHttpClient client = new OkHttpClient.Builderhttp://192.168.30.10:8090().connectTimeout(1, TimeUnit.MINUTES)
 //                .readTimeout(120, TimeUnit.SECONDS)
 //                .writeTimeout(120, TimeUnit.SECONDS)
 //
 //                .addInterceptor(interceptor)
 //                .build();

         OkHttpClient.Builder builder = new OkHttpClient.Builder();
         builder.connectTimeout(1, TimeUnit.MINUTES);
         builder.writeTimeout(120, TimeUnit.SECONDS);
         builder.readTimeout(120, TimeUnit.SECONDS);

         if (BuildConfig.DEBUG) {
             builder.addInterceptor(interceptor);
         }
         builder.cache(null);
         OkHttpClient okHttpClient = builder.build();

         Gson gson = new GsonBuilder()
                 .setLenient()
                 .disableHtmlEscaping()
                 .create();

         retrofit = new Retrofit.Builder()
                 .baseUrl(BASE_URL)
                 .addConverterFactory(GsonConverterFactory.create(gson))
                 .client(okHttpClient)
                 .build();
         return retrofit;

     }
 }
