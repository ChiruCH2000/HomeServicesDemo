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

public class LoginActivity extends AppCompatActivity {
    private EditText medtMobileNo;
    private Button mbtnSendOtp;
    private SharedPreferences sharedPreferences;
    private CheckNetworkConnection mCheckNetworkConnection;
    private ApiRequest mApiRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        initializeViews();
        mbtnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckNetworkConnection.isNetworkAvailable(LoginActivity.this)) {
                    CallApiSendOtp();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void CallApiSendOtp() {
        JSONObject jsonObjectMap = new JSONObject();
        String phoneNo =medtMobileNo.getText().toString().trim();
        try {
            jsonObjectMap.put("phone",phoneNo );
            jsonObjectMap.put("token", "Q62qY9851ZM5BXHDzxWScEJfKTZAP8Jp");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        mApiRequest.callPostJson(this, jsonObjectMap, Singleton.getInstance().getUrlLogin(), new VolleyResponseListener() {
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

                        String userId = jsonObject.getString("user_id");

                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user_id", userId);
                        editor.putString("phone", phoneNo);
                        editor.apply();

                        Intent intent =new Intent(LoginActivity.this,VerifyOtpActivity.class);
                        startActivity(intent);

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

        medtMobileNo = findViewById(R.id.activity_login_edt_phoneNo);
        mbtnSendOtp = findViewById(R.id.activity_login_btn_sendotp);

        mCheckNetworkConnection = new CheckNetworkConnection();
        mApiRequest = new ApiRequest(this);

    }
}