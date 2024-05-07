package com.example.homeservicesdemo.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.adapters.CategoryAdapter;
import com.example.homeservicesdemo.adapters.MBServicesAdapter;
import com.example.homeservicesdemo.models.CategoryBanner;
import com.example.homeservicesdemo.models.CategoryBean;
import com.example.homeservicesdemo.models.MostBookedService;
import com.example.homeservicesdemo.utils.ApiRequest;
import com.example.homeservicesdemo.utils.CheckNetworkConnection;
import com.example.homeservicesdemo.utils.Singleton;
import com.example.homeservicesdemo.utils.VolleyResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyAccountActivity extends AppCompatActivity {
    private TextView mtxtUpdate;
    private EditText medtxtName,medtxtPhone,medtxtEmail;
    private ImageView mimgBackIcon;
    private CheckNetworkConnection mCheckNetworkConnection;
    private ApiRequest mApiRequest;
    private String userId;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id","");

        intializeViews();
        mimgBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        CallApiProfile();

        mtxtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckNetworkConnection.isNetworkAvailable(MyAccountActivity.this)) {
                    CallApiUpdate();
                } else {
                    Toast.makeText(MyAccountActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void CallApiProfile() {
        JSONObject jsonObjectMap = new JSONObject();
        try {
            jsonObjectMap.put("user_id", userId);
            jsonObjectMap.put("token", "Q62qY9851ZM5BXHDzxWScEJfKTZAP8Jp");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        mApiRequest.callPostJson(this, jsonObjectMap, Singleton.getInstance().getUrlProfile(), new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {
                System.out.println("ProfilePageActivity: response " + response.toString());
            }

            @Override
            public void onJsonResponse(JSONObject jsonObject) {
                System.out.println("ProfilePageActivity: jsonresponse " + jsonObject.toString());
                try {

                    String status = jsonObject.getString("status");

                    if ("success".equals(status)) {
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        if (dataArray.length() > 0) {
                            JSONObject firstObject = dataArray.getJSONObject(0);
                            medtxtName.setText(firstObject.getString("user_name"));
                            medtxtEmail.setText(firstObject.getString("email_id"));
                            medtxtPhone.setText(firstObject.getString("mobile_number"));
                        }
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
    private void CallApiUpdate() {
        JSONObject jsonObjectMap = new JSONObject();
        try {
            jsonObjectMap.put("user_id", userId);
            jsonObjectMap.put("token", "Q62qY9851ZM5BXHDzxWScEJfKTZAP8Jp");
            jsonObjectMap.put("name",medtxtName.getText().toString().trim());
            jsonObjectMap.put("email",medtxtEmail.getText().toString().trim());

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        mApiRequest.callPostJson(this, jsonObjectMap, Singleton.getInstance().getUrlUpdate(), new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {
                System.out.println("MyAccountActivity: response " + response);
            }
            @Override
            public void onJsonResponse(JSONObject jsonObject) {
                System.out.println("MyAccountActivity: jsonresponse " + jsonObject.toString());
                try {
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("msg");

                    if ("success".equals(status)) {

                        Toast.makeText(MyAccountActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish();

                    }
                }
                catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onError(VolleyError error) {
                System.out.println("error"+error);
            }
        });
    }

    private void intializeViews() {

        medtxtName = findViewById(R.id.activity_myAccount_edt_name);
        medtxtEmail =findViewById(R.id.activity_myAccount_edt_email);
        medtxtPhone = findViewById(R.id.activity_myAccount_edt_phone);
        mimgBackIcon = findViewById(R.id.activity_myAccount_img_backIcon);

        mtxtUpdate=findViewById(R.id.activity_myAccount_btn_update);

        mCheckNetworkConnection = new CheckNetworkConnection();
        mApiRequest = new ApiRequest(this);
    }
}