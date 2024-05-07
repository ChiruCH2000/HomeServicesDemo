package com.example.homeservicesdemo.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.homeservicesdemo.R;

import com.example.homeservicesdemo.adapters.OrdersAdapter;
import com.example.homeservicesdemo.models.Orderbean;
import com.example.homeservicesdemo.utils.ApiRequest;
import com.example.homeservicesdemo.utils.CheckNetworkConnection;
import com.example.homeservicesdemo.utils.Singleton;
import com.example.homeservicesdemo.utils.VolleyResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersActivity extends AppCompatActivity {
    private ImageView mimgBackIcon;
    private RecyclerView mrecyclerView;
    private CheckNetworkConnection mCheckNetworkConnection;
    public List<Orderbean> ordersList;
    private ApiRequest mApiRequest;
    private String userId;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id","");
        initializeViews();

        callApi();
        mimgBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void callApi() {
        JSONObject jsonObjectMap = new JSONObject();
        try {
            jsonObjectMap.put("user_id", userId);
            jsonObjectMap.put("token", "Q62qY9851ZM5BXHDzxWScEJfKTZAP8Jp");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        mApiRequest.callPostJson(this, jsonObjectMap, Singleton.getInstance().getUrlMyOrders(), new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {
                System.out.println("MyOrdersPageActivity: response " + response.toString());
            }

            @Override
            public void onJsonResponse(JSONObject jsonObject) {
                System.out.println("MyOrdersPageActivity: jsonresponse " + jsonObject.toString());
                try {

                    String status = jsonObject.getString("status");

                    if ("success".equals(status)) {
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        ordersList =new ArrayList<>();
                        for (int i =0 ; i < dataArray.length(); i++){
                            JSONObject address = dataArray.getJSONObject(i);

                            Orderbean orderbeanList = new Orderbean();
                            orderbeanList.setOrder_id(address.getString("order_id"));
                            orderbeanList.setOid(address.getString("oid"));
                            orderbeanList.setPay_amount(address.getString("pay_amount"));
                            orderbeanList.setOrder_date(address.getString("order_date"));
                            orderbeanList.setOrder_time(address.getString("order_time"));
                            orderbeanList.setStatus(address.getString("status"));
                            orderbeanList.setCategory_name(address.getString("category_name"));

                            ordersList.add(orderbeanList);
                        }
                        OrdersAdapter ordersAdapter = new OrdersAdapter(MyOrdersActivity.this,ordersList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyOrdersActivity.this);
                        mrecyclerView.setLayoutManager(linearLayoutManager);
                        mrecyclerView.setAdapter(ordersAdapter);

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

    private void initializeViews() {
        mimgBackIcon = findViewById(R.id.activity_my_orders_img_backIcon);
        mrecyclerView =findViewById(R.id.activiy_my_orders_recyclerView);

        mCheckNetworkConnection = new CheckNetworkConnection();
        mApiRequest = new ApiRequest(this);
    }
}