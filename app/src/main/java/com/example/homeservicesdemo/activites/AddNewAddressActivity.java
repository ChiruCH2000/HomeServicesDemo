package com.example.homeservicesdemo.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.adapters.TimeSlotAdapter;
import com.example.homeservicesdemo.models.TimeSlotBean;
import com.example.homeservicesdemo.utils.ApiRequest;
import com.example.homeservicesdemo.utils.CheckNetworkConnection;
import com.example.homeservicesdemo.utils.Singleton;
import com.example.homeservicesdemo.utils.VolleyResponseListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddNewAddressActivity extends AppCompatActivity implements OnMapReadyCallback{

    private EditText medtxtLandMark, medtxtHouseNo,meditOthers;
    private Button mbtnSaveAddress;
    private TextView mtxtaddress;
    private RadioButton mrbHome, mrbOthers;
    private ImageView mimgMarker,mimgBackIcon;
    private MapView mapView;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String userId,mlatitute,mlogitute,mpincode,label;
    private SharedPreferences sharedPreferences;
    private RadioGroup mlabel;
    private RadioButton selectedRadioButton;
    private int selectedId;
    private CheckNetworkConnection mCheckNetworkConnection;
    private ApiRequest mApiRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id","");
        initializeViews();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        mimgBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mlabel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.activity_add_new_address_radio_others) {
                    meditOthers.setVisibility(View.VISIBLE);
                } else {
                    meditOthers.setVisibility(View.GONE);
                }
            }
        });
        if (mrbOthers.isChecked()) {
            label = meditOthers.getText().toString().trim();
            if (label.isEmpty()) {
                label = "Others";
            }
        } else {
            label = mrbHome.isChecked() ? "Home" : "Other";
        }
        mbtnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallApiSaveAddress();
                selectedId = mlabel.getCheckedRadioButtonId();
                selectedRadioButton = findViewById(selectedId);
                System.out.println("onclicksaveaddress"+selectedRadioButton.getText().toString()+medtxtHouseNo.getText().toString().trim()+medtxtLandMark.getText().toString());

            }
        });
    }
    private void CallApiSaveAddress() {
        JSONObject jsonObjectMap = new JSONObject();
        try {
            jsonObjectMap.put("user_id", userId);
            jsonObjectMap.put("address", mtxtaddress.getText().toString());
            jsonObjectMap.put("token","Q62qY9851ZM5BXHDzxWScEJfKTZAP8Jp");
            jsonObjectMap.put("house", medtxtHouseNo.getText().toString().trim());
            jsonObjectMap.put("landmark", medtxtLandMark.getText().toString());
            jsonObjectMap.put("latitude", mlatitute);
            jsonObjectMap.put("longitude", mlogitute);
            jsonObjectMap.put("pincode", mpincode);
            selectedId = mlabel.getCheckedRadioButtonId();
            selectedRadioButton = findViewById(selectedId);
            jsonObjectMap.put("label", selectedRadioButton.getText().toString());

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        mApiRequest.callPostJson(this, jsonObjectMap, Singleton.getInstance().getUrlAddAddress(), new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {
                System.out.println("AddAddressActivity: response " + response.toString());
            }

            @Override
            public void onJsonResponse(JSONObject jsonObject) {
                System.out.println("AddAddressActivity: jsonresponse " + jsonObject.toString());

                try {

                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("msg");

                    if (status.equals("success")) {

                        Toast.makeText(AddNewAddressActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish();

                    } else{
                        Toast.makeText(AddNewAddressActivity.this, "Failed to Update" + message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onError(VolleyError error) {
                System.out.println("error" + error);
            }
        });
    }
    private void getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            mlatitute= String.valueOf(latLng.latitude);
            mlogitute= String.valueOf(latLng.latitude);
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0);
                mtxtaddress.setText(address);
                Address firstAddress = addresses.get(0);
                String pincode = firstAddress.getPostalCode();
                if (pincode != null) {
                    mpincode = pincode;
                } else {
                    mpincode = "Pincode not available";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    private void initializeViews() {
        mapView = findViewById(R.id.activity_myAccount_mapView);
        medtxtHouseNo = findViewById(R.id.activity_add_new_address_edtxt_houseNo);
        medtxtLandMark = findViewById(R.id.activity_add_new_address_edtxt_landMark);
        mbtnSaveAddress = findViewById(R.id.activity_add_new_address_btnSaveAddress);
        mrbHome = findViewById(R.id.activity_add_new_address_radio_home);
        mrbOthers = findViewById(R.id.activity_add_new_address_radio_others);
        mlabel = findViewById(R.id.activity_add_new_address_radio_group_label);
        mimgMarker =findViewById(R.id.activity_myAccount_img_marker);
        mimgBackIcon = findViewById(R.id.activity_myAccount_img_backIcon);
        mtxtaddress = findViewById(R.id.activity_add_new_address_txtAddress);
        meditOthers = findViewById(R.id.activity_add_new_address_edittext_others);

        mCheckNetworkConnection = new CheckNetworkConnection();
        mApiRequest = new ApiRequest(this);
    }
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng latLng = googleMap.getCameraPosition().target;
                getAddressFromLatLng(latLng);
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
            LatLng latLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }
    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            // Update map with current location
            if (googleMap != null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                locationManager.removeUpdates(locationListener);
            }
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
    }
}
