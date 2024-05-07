package com.example.homeservicesdemo.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.utils.ApiRequest;
import com.example.homeservicesdemo.utils.CheckNetworkConnection;
import com.example.homeservicesdemo.utils.Singleton;
import com.example.homeservicesdemo.utils.VolleyResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

public class VerifyOtpActivity extends AppCompatActivity {
    private EditText medtOtp;
    private Button mbtnVerifyOtp;
    private SharedPreferences sharedPreferences;
    private CheckNetworkConnection mCheckNetworkConnection;
    private ApiRequest mApiRequest;
    private String userId,phoneNumberExtra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        initializeViews();

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id","");
        phoneNumberExtra = sharedPreferences.getString("phone","");

        Toast.makeText(this, userId+phoneNumberExtra, Toast.LENGTH_SHORT).show();

        mbtnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckNetworkConnection.isNetworkAvailable(VerifyOtpActivity.this)) {
                    CallApiVerifyOtp();
                } else {
                    Toast.makeText(VerifyOtpActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    private void CallApiVerifyOtp() {
        JSONObject jsonObjectMap = new JSONObject();
        try {
            /*String phoneNumberExtra = getIntent().getStringExtra("phone");*/
            if (phoneNumberExtra != null) {
                jsonObjectMap.put("phone", phoneNumberExtra.trim());
                Toast.makeText(this, phoneNumberExtra, Toast.LENGTH_SHORT).show();
            } else {

                return;
            }
            jsonObjectMap.put("otp", medtOtp.getText().toString().trim() );
            jsonObjectMap.put("user_id",userId);
            jsonObjectMap.put("fcm_id","");
            jsonObjectMap.put("token", "Q62qY9851ZM5BXHDzxWScEJfKTZAP8Jp");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        mApiRequest.callPostJson(this, jsonObjectMap, Singleton.getInstance().getUrlVerifyOtp(), new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {

                System.out.println("LoginActivity: response " + response);

            }
            @Override
            public void onJsonResponse(JSONObject jsonObject) {

                System.out.println("LoginActivity: jsonresponse " + jsonObject.toString());

                try {

                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("msg");

                    if ("success".equals(status)) {

                        Toast.makeText(VerifyOtpActivity.this, message, Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();

                        Intent intent =new Intent(VerifyOtpActivity.this,MainActivity.class);
                        startActivity(intent);
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

    private void initializeViews() {

        medtOtp = findViewById(R.id.activity_verify_otp_edt_phoneNo);
        mbtnVerifyOtp = findViewById(R.id.activity_verify_otp_btn_verifyOtp);

        mCheckNetworkConnection = new CheckNetworkConnection();
        mApiRequest = new ApiRequest(this);

    }
}