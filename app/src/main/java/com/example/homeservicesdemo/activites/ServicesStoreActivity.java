package com.example.homeservicesdemo.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.adapters.ServicesAdapter;
import com.example.homeservicesdemo.adapters.ServicesStoreAdapter;
import com.example.homeservicesdemo.models.HowItWork;
import com.example.homeservicesdemo.models.ServiceExcluded;
import com.example.homeservicesdemo.models.ServiceIncluded;
import com.example.homeservicesdemo.models.ServicesBean;
import com.example.homeservicesdemo.utils.ApiRequest;
import com.example.homeservicesdemo.utils.CheckNetworkConnection;
import com.example.homeservicesdemo.utils.Singleton;
import com.example.homeservicesdemo.utils.VolleyResponseListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServicesStoreActivity extends AppCompatActivity {
    private String userId;
    private SharedPreferences sharedPreferences;
    private ImageView mimgBackIcon;
    private TextView mtxtHeader;
    private RecyclerView mrecyclerViewServicesItems;
    private CheckNetworkConnection mCheckNetworkConnection;
    private ApiRequest mApiRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_store);

        InitializeViews();
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id","");

        mtxtHeader.setText(getIntent().getStringExtra("cat_name"));

        mimgBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (mCheckNetworkConnection.isNetworkAvailable(this)) {
            callApiServices();
        } else {
            Toast.makeText(ServicesStoreActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void InitializeViews() {

        mrecyclerViewServicesItems = findViewById(R.id.activity_services_store_recyclerView);
        mimgBackIcon = findViewById(R.id.activity_services_store_img_backIcon);
        mtxtHeader = findViewById(R.id.activity_services_store_textView_Header);

        mCheckNetworkConnection = new CheckNetworkConnection();
        mApiRequest = new ApiRequest(this);
    }

    private void callApiServices() {
        JSONObject jsonObjectMap = new JSONObject();
        try {
            jsonObjectMap.put("user_id", userId);
            jsonObjectMap.put("category_id", getIntent().getStringExtra("cat_id"));
            jsonObjectMap.put("sub_cat_id", "");
            jsonObjectMap.put("service_id", "");
            jsonObjectMap.put("token","Q62qY9851ZM5BXHDzxWScEJfKTZAP8Jp");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        mApiRequest.callPostJson(this, jsonObjectMap, Singleton.getInstance().getUrlServices(), new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {
                System.out.println("ServicesActivity: response " + response.toString());
            }

            @Override
            public void onJsonResponse(JSONObject jsonObject) {
                System.out.println("ServicesActivity: jsonresponse " + jsonObject.toString());

                try {

                    String status = jsonObject.getString("status");

                    if ("success".equals(status)) {
                        if (jsonObject.has("services")) {
                            ArrayList<ServicesBean> servicesBeansList = new ArrayList<>();
                            JSONArray servicesArray = jsonObject.getJSONArray("services");
                            if (servicesArray.length() > 0) {
                                for (int i = 0; i < servicesArray.length(); i++) {
                                    JSONObject serviceObject = servicesArray.getJSONObject(i);

                                    ServicesBean servicesBean = new ServicesBean();

                                    servicesBean.setService_id(serviceObject.getString("service_id"));
                                    servicesBean.setName(serviceObject.getString("name"));
                                    servicesBean.setDescription(serviceObject.getString("description"));
                                    servicesBean.setMrp(serviceObject.getString("mrp"));
                                    servicesBean.setPrice(serviceObject.getString("price"));
                                    servicesBean.setImage(serviceObject.getString("image"));
                                    servicesBean.setCategory_name(serviceObject.getString("category_name"));
                                    servicesBean.setCategory_id(serviceObject.getString("category_id"));
                                    servicesBean.setSubcat_id(serviceObject.getString("subcat_id"));
                                    servicesBean.setSubcategory_name(serviceObject.getString("subcategory_name"));
                                    servicesBean.setWarranty(serviceObject.getString("warranty"));
                                    servicesBean.setDuration(serviceObject.getString("duration"));
                                    servicesBean.setNote1(serviceObject.optString("note1"));
                                    servicesBean.setNote2(serviceObject.optString("note2"));
                                    servicesBean.setType(serviceObject.getString("type"));
                                    servicesBean.setService_rating(serviceObject.optInt("service_rating"));

                                    servicesBeansList.add(servicesBean);

                                }

                                ServicesStoreAdapter servicesAdapter = new ServicesStoreAdapter(ServicesStoreActivity.this, servicesBeansList);
                                mrecyclerViewServicesItems.setLayoutManager(new LinearLayoutManager(ServicesStoreActivity.this));
                                mrecyclerViewServicesItems.setAdapter(servicesAdapter);

                            }
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


}