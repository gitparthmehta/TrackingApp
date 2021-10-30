package com.track.trackingapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.track.trackingapp.Adapters.UserListAdapter;
import com.track.trackingapp.GlobalClass.Constants;
import com.track.trackingapp.GlobalClass.PreferenceHelper;
import com.track.trackingapp.R;
import com.track.trackingapp.models.LocationModel;
import com.track.trackingapp.models.LoginModel;
import com.track.trackingapp.restApi.ApiManager;
import com.track.trackingapp.restApi.ApiResponseInterface;
import com.track.trackingapp.restApi.AppConstant;
import com.track.trackingapp.restApi.Response.BaseReponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    String user_name = "";
    String user_id = "";
    private GoogleMap map;
    private String dealLat, dealLang, address;
    Boolean isprocessing = false;
    Timer timer;
    TimerTask hourlyTask;
    String android_id = "";
    @BindView(R.id.username_txt)
    TextView username_txt;
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.nodataTxt)
    TextView nodataTxt;

    @BindView(R.id.mapLayout)
    RelativeLayout mapLayout;
    private ApiManager mApiManager;
    private ApiResponseInterface mInterFace;
    ArrayList<LocationModel> locationModels = new ArrayList<>();
    private ProgressDialog pDialog;
    SupportMapFragment mapFragment;
    Geocoder geocoder;
    List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_map);
        ButterKnife.bind(this);

        init();
        setupNetwork();
        callUserLocation();
    }

    private void init() {
        geocoder = new Geocoder(this, Locale.getDefault());

        user_id = PreferenceHelper.getString(Constants.clicked_id, "");
        user_name = PreferenceHelper.getString(Constants.clicked_name, "");
        username_txt.setText(user_name);
        pDialog = new ProgressDialog(UserMapActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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
                    Constants.showNoInternetDialog(UserMapActivity.this);
                }
            }

            @Override
            public void isUserDisabled(String errorMsg, int errorCode) {

            }

            @Override
            public void isSuccess(Object response, int ServiceCode) {
                if (ServiceCode == AppConstant.USERLOCATION) {
                    System.out.println("USERLOCATION Response:" + String.valueOf(response.toString()));
                    BaseReponseBody res = (BaseReponseBody) response;
                    Toast.makeText(UserMapActivity.this, res.getMsg().toString(), Toast.LENGTH_LONG).show();

                    if (res.getStatus() == 1) {
                        locationModels = res.getLocationModels();

                        if (locationModels.size() > 0) {
                            nodataTxt.setVisibility(View.GONE);
                            mapLayout.setVisibility(View.VISIBLE);
                            dealLang = locationModels.get(0).getUser_long();
                            dealLat = locationModels.get(0).getUser_lat();
                            try {
                                addresses = geocoder.getFromLocation(Double.parseDouble(dealLat), Double.parseDouble(dealLang), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                                Log.d("address", address);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            nodataTxt.setVisibility(View.VISIBLE);
                            mapLayout.setVisibility(View.GONE);
                        }

                    } else {

                    }
                    mapFragment.getMapAsync(UserMapActivity.this);

                    //invalidTokenshowDialog(UserMapActivity.this);

                }
            }
        };
        mApiManager = new ApiManager(this, mInterFace);

    }

    private void callUserLocation() {
        if (Constants.checkInternet(UserMapActivity.this)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", user_id);
            params.put("device_id", PreferenceHelper.getString(Constants.token, ""));
            mApiManager.makeCommonRequest(params, AppConstant.USERLOCATION);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            if (dealLat.equals("null") || dealLat.isEmpty()) {
                dealLat = "0";
            }
        } catch (NullPointerException e) {
            dealLat = "0";
        }
        try {
            if (dealLang.equals("null") || dealLang.isEmpty()) {
                dealLang = "0";
            }
        } catch (NullPointerException e) {
            dealLang = "0";
        }
        LatLng sydney = new LatLng(Double.parseDouble(dealLat), Double.parseDouble(dealLang));

        Log.i("latelong: ", dealLat.toString() + "long " + dealLang.toString());
        map = googleMap;
        //map.setMapType(M);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        } else {
            // Toast.makeText(UserLocationActivity.this, "You have to accept to enjoy all app's services!", Toast.LENGTH_LONG).show();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                map.setMyLocationEnabled(true);
            }
        }

        //map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
        //map.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 100));

        Marker marker = map.addMarker(new MarkerOptions()
                .title(address)
                .snippet(address)
                .position(sydney)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));


        MapInfoWindowAdapter markerWindowView = new MapInfoWindowAdapter(UserMapActivity.this, "");
        googleMap.setInfoWindowAdapter(markerWindowView);
        marker.showInfoWindow();


        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                // TODO Auto-generated method stub
//                Intent intent = new Intent(UserLocationActivity.this, MapsActivity.class);
//                intent.putExtra("address", dealaddress);
//                intent.putExtra("title", restaurant.getText().toString());
//                intent.putExtra("phone", phone_text);
//                intent.putExtra("dealLat", dealLat);
//                intent.putExtra("dealLang", dealLang);
//                intent.putExtra("mapimage", mapimage);
//                startActivity(intent);
            }
        });

        GoogleMap.OnMarkerClickListener onMarkerClickedListener = new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                } else {
                    marker.showInfoWindow();
                }
                return true;
            }
        };


    }

    class MapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private LayoutInflater inflater;
        private Context context;
        String image_str;
        View v;

        public MapInfoWindowAdapter(UserMapActivity context, String mapimage) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.context = context;
            this.image_str = mapimage;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            try {
                v = inflater.inflate(R.layout.custom_info_contents, null);
            } catch (Exception e) {
                e.printStackTrace();
                // map is already there
            }

            TextView title = (TextView) v.findViewById(R.id.title);
            title.setText(address);

            ImageView iv = (ImageView) v.findViewById(R.id.markerImage);
//            Uri uri = Uri.parse(mapimage);
//            iv.setImageURI(uri);

//            Glide.with(getApplicationContext()).load(Uri.parse(image_str)).into(iv);
            return v;
        }

        @Override
        public View getInfoContents(Marker marker) {


            return v;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserMapActivity.this, UserListActivity.class);
        startActivity(intent);
        finish();
    }
}