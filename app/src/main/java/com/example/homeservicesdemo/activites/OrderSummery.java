package com.example.homeservicesdemo.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.adapters.OrdersAdapter;
import com.example.homeservicesdemo.adapters.SelectedServiceAdapter;
import com.example.homeservicesdemo.helperclass.DataBaseHelper;
import com.example.homeservicesdemo.helperclass.SelectedServiceDetails;
import com.example.homeservicesdemo.models.Orderbean;
import com.example.homeservicesdemo.models.VarientBean;
import com.example.homeservicesdemo.utils.ApiRequest;
import com.example.homeservicesdemo.utils.CheckNetworkConnection;
import com.example.homeservicesdemo.utils.Singleton;
import com.example.homeservicesdemo.utils.VolleyResponseListener;
import com.google.android.material.color.utilities.Variant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderSummery extends AppCompatActivity implements SelectedServiceAdapter.AdapterCallback{
    private ImageView mimgBackIcon;
    private TextView mtxtPrice,mtxtAddress,mtxtTimeSlot,mtxtPay;
    private LinearLayout mllayoutAddAddress,MllayoutAddAddressdetails,MllayoutAddTimeSlot,mllayoutAddTimeslot;
    private RecyclerView mrecyclerView;
    private SelectedServiceAdapter mAdapter;
    private String categoryId;
    private DataBaseHelper mDatabaseHelper;
    private List<SelectedServiceDetails> mSelectedServicesList;
    private static final int REQUEST_TIME_SLOT = 123; // Any unique integer value
    private static final int REQUEST_ADDRESS = 101; // Or any unique integer value
    private String userId,address,label,longitude,latitude,house,landmark,pincode,selectedDate,selectedTime,session;
    private SharedPreferences sharedPreferences;
    private ApiRequest mApiRequest;
    private CheckNetworkConnection mCheckNetworkConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summery);

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id","");

        initializeViews();

        mDatabaseHelper = new DataBaseHelper(this);

        // Retrieve selected services for the current category
        categoryId = (getIntent().getStringExtra("cat_id")); // Assuming you pass the category ID through Intent
        mSelectedServicesList = mDatabaseHelper.getSelectedCategoryServices(Integer.parseInt(categoryId));

        // Get the total price for the specified categoryId
        double totalPrice =  mDatabaseHelper.getTotalCartPrice((categoryId));
        // Set the total price to your TextView
        mtxtPrice.setText(String.valueOf(totalPrice));

        // Set up RecyclerView
        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SelectedServiceAdapter(this, mSelectedServicesList,OrderSummery.this);
        mrecyclerView.setAdapter(mAdapter);

        mllayoutAddTimeslot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(OrderSummery.this,TimeSlotActivity.class);
                startActivityForResult(intent, REQUEST_TIME_SLOT);
            }
        });
        mllayoutAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderSummery.this, ManageAddressActivity.class);
                intent.putExtra("calling_activity", "OrderSummary"); // Add extra to indicate the calling activity
                startActivityForResult(intent, REQUEST_ADDRESS);
            }
        });
        mimgBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mtxtPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallEassBussPaymentIntegration();
            }
        });
    }

    private void CallEassBussPaymentIntegration() {
        JSONObject jsonObjectMap = new JSONObject();
        try {
            jsonObjectMap.put("user_id", userId);
            jsonObjectMap.put("total_amount", mtxtPrice);
            jsonObjectMap.put("pay_amount", mtxtPrice);
            jsonObjectMap.put("payment_type", "easebuzz");
            jsonObjectMap.put("token", "Q62qY9851ZM5BXHDzxWScEJfKTZAP8Jp");
            jsonObjectMap.put("cat_id", categoryId);
            jsonObjectMap.put("address", address);
            jsonObjectMap.put("label", label);
            jsonObjectMap.put("house", house);
            jsonObjectMap.put("landmark", landmark);
            jsonObjectMap.put("latitude", latitude);
            jsonObjectMap.put("longitude", longitude);
            jsonObjectMap.put("pincode", pincode);
            jsonObjectMap.put("date", selectedDate);
            jsonObjectMap.put("slot_id", "14");
            jsonObjectMap.put("session", session);
            jsonObjectMap.put("time", selectedTime);

            JSONArray serviceArray = new JSONArray();

            for (SelectedServiceDetails service : mSelectedServicesList) {
                JSONObject serviceObject = new JSONObject();
                serviceObject.put("cat_id", categoryId);
                serviceObject.put("cat_name", getIntent().getStringExtra("cat_name"));
                serviceObject.put("sub_cat_id", service.getSubServiceId());
                serviceObject.put("sub_cat_name", service.getSubServiceName());
                serviceObject.put("service_id", service.getServiceId());
                serviceObject.put("service_name", service.getServiceName());
                serviceObject.put("type", service.getType());
                serviceObject.put("mrp", service.getPrice());
                serviceObject.put("price", service.getPrice());
                serviceObject.put("qty", service.getQuantity());

                JSONArray variantArray = new JSONArray();
                List<VarientBean> variants = service.getVariantId();
                if (variants != null) {
                    for (VarientBean variant : variants) {
                        JSONObject variantObject = new JSONObject();
                        variantObject.put("variant_id", variant.getVariant_id());
                        variantObject.put("variant_name", variant.getVariant_name());
                        variantObject.put("variant_mrp", variant.getVariant_mrp());
                        variantObject.put("variant_price", variant.getVariant_price());
                        variantObject.put("variant_qty", variant.getVariant_qty());
                        variantArray.put(variantObject);
                    }
                }

                serviceObject.put("variant", variantArray);
                serviceArray.put(serviceObject);
            }

            jsonObjectMap.put("service", serviceArray);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        mApiRequest.callPostJson(this, jsonObjectMap, Singleton.getInstance().getUrlConfirmOrder(), new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {
                System.out.println("MyOrdersPageActivity: response " + response.toString());
            }

            @Override
            public void onJsonResponse(JSONObject jsonObject) {
                System.out.println("MyOrdersPageActivity: jsonresponse " + jsonObject.toString());
                try {

                    JSONObject jsonResponse = new JSONObject(String.valueOf(jsonObject));
                    String status = jsonResponse.optString("status");

                    if ("success".equals(status)) {
                        JSONObject data = jsonResponse.getJSONObject("data");
                        String paidAmount = data.optString("paid_amount");
                        String payMode = data.optString("pay_mode");
                        String paymentType = data.optString("payment_type");
                        String orderId = data.optString("order_id");
                        String easebuzzMerchantKey = data.optString("easebuzz_merchantkey");
                        String easebuzzSaltKey = data.optString("easebuzz_saltkey");
                        String currencyType = data.optString("currency_type");
                        String easebuzzCallback = data.optString("easebuzz_callback");
                        String responseData = data.optString("data");

                        Toast.makeText(OrderSummery.this, status, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TIME_SLOT && resultCode == RESULT_OK) {
             selectedDate = data.getStringExtra("selectedDate");
             selectedTime = data.getStringExtra("selectedTime");
             session = data.getStringExtra("session");
            // Get selected time and date from TimeSlotActivity
            MllayoutAddTimeSlot.setVisibility(View.VISIBLE);
             mtxtTimeSlot.setText(data.getStringExtra("selectedDate")+" "+data.getStringExtra("selectedTime"));

            // Use the selected time and date as needed
        }
        if (requestCode == REQUEST_ADDRESS && resultCode == Activity.RESULT_OK && data != null) {
            String fulladdress = data.getStringExtra("selected_address");
            if (fulladdress != null) {
                 address = data.getStringExtra("address");
                 label = data.getStringExtra("label");
                 longitude = data.getStringExtra("longitude");
                 latitude = data.getStringExtra("latitude");
                 house = data.getStringExtra("house");
                 landmark = data.getStringExtra("landmark");
                 pincode = data.getStringExtra("pincode");
                // Set the address to the TextView
                MllayoutAddAddressdetails.setVisibility(View.VISIBLE);
                mtxtAddress.setText(address);
            }
        }
    }
    @Override
    public void onAdapterItemChanged() {
        // Implement your logic to refresh the activity content
        updateActivityContent();
    }

    private void updateActivityContent() {
        double totalPrice =  mDatabaseHelper.getTotalCartPrice((categoryId));
        // Set the total price to your TextView
        mtxtPrice.setText(String.valueOf(totalPrice));
    }

    private void initializeViews() {

        mimgBackIcon =findViewById(R.id.activity_order_summery_img_backIcon);

        mtxtPay = findViewById(R.id.activity_order_summery_textView_pay);
        mtxtPrice=findViewById(R.id.activity_order_summery_textView_price);
        mtxtAddress=findViewById(R.id.activity_order_summery_textView_address);
        mtxtTimeSlot=findViewById(R.id.activity_order_summery_txtView_timeslot);

        mllayoutAddAddress=findViewById(R.id.activity_order_summery_add_address);
        mllayoutAddTimeslot=findViewById(R.id.activity_order_summery_linearlayout_add_timeslot);

        MllayoutAddTimeSlot=findViewById(R.id.activity_order_summery_add_timeslots_details);
        MllayoutAddAddressdetails=findViewById(R.id.activity_order_summery_linearlayout_add_address_details);

        mrecyclerView=findViewById(R.id.activity_order_summery_recyclerView);

        mCheckNetworkConnection = new CheckNetworkConnection();
        mApiRequest = new ApiRequest(this);
    }
}