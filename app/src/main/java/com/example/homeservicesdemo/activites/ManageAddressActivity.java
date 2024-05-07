package com.example.homeservicesdemo.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.adapters.AddAddressListAdapter;
import com.example.homeservicesdemo.models.AddressesListBean;
import com.example.homeservicesdemo.utils.ApiRequest;
import com.example.homeservicesdemo.utils.CheckNetworkConnection;
import com.example.homeservicesdemo.utils.Singleton;
import com.example.homeservicesdemo.utils.VolleyResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ManageAddressActivity extends AppCompatActivity implements AddAddressListAdapter.OnSetAddressClickListener {
    private Button mbtnAddAddress;
    private RecyclerView mrecyclerView;
    private CheckNetworkConnection mCheckNetworkConnection;
    public List<AddressesListBean> addressesList ;
    private ApiRequest mApiRequest;
    private String userId;
    private ImageView mimgBackIcon;
    private SharedPreferences sharedPreferences;
    private boolean isSetAddressButtonVisible = false;

    private AddAddressListAdapter addAddressListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_address);

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id","");

        initializeViews();
        if (mCheckNetworkConnection.isNetworkAvailable(this)) {
            CallApiManageAddress();
        }
        else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
        Intent intent = getIntent();
        if (intent != null) {
            String callingActivity = intent.getStringExtra("calling_activity");
            if ("OrderSummary".equals(callingActivity)) {
                isSetAddressButtonVisible = true;
            } else {
                isSetAddressButtonVisible = false;
            }
        }

        mimgBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mbtnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ManageAddressActivity.this,AddNewAddressActivity.class);
                startActivity(intent);
            }
        });
    }

    public void CallApiDelete(String addressId) {
        JSONObject jsonObjectMap = new JSONObject();
        try {
            jsonObjectMap.put("user_id", userId);
            jsonObjectMap.put("address_id", addressId);
            jsonObjectMap.put("token", "Q62qY9851ZM5BXHDzxWScEJfKTZAP8Jp");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        mApiRequest.callPostJson(ManageAddressActivity.this, jsonObjectMap, Singleton.getInstance().getUrlDeleteAddress(), new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {
                System.out.println("DeleteAddress: response " + response);
            }

            @Override
            public void onJsonResponse(JSONObject jsonObject) {
                System.out.println("DeleteAddress: jsonresponse " + jsonObject);
                try {
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");

                    if ("success".equals(status)) {
                        Toast.makeText(ManageAddressActivity.this, msg, Toast.LENGTH_SHORT).show();
                        // Refresh the RecyclerView or update the dataset as needed
                        addressesList.removeIf(address -> address.getAddress_id().equals(addressId));
                        if(addAddressListAdapter != null)
                            addAddressListAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ManageAddressActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    private void CallApiManageAddress() {
        JSONObject jsonObjectMap = new JSONObject();
        try {
            jsonObjectMap.put("user_id", userId);
            jsonObjectMap.put("token", "Q62qY9851ZM5BXHDzxWScEJfKTZAP8Jp");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        mApiRequest.callPostJson(this, jsonObjectMap, Singleton.getInstance().getUrlAddresslist(), new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {
                System.out.println("AddressPageActivity: response " + response.toString());
            }

            @Override
            public void onJsonResponse(JSONObject jsonObject) {
                System.out.println("AddressPageActivity: jsonresponse " + jsonObject.toString());
                try {

                    String status = jsonObject.getString("status");

                    if ("success".equals(status)) {
                        JSONArray addressListArray = jsonObject.getJSONArray("address_list");
                        addressesList =new ArrayList<>();
                        for (int i =0 ; i < addressListArray.length(); i++){
                            JSONObject address = addressListArray.getJSONObject(i);

                            AddressesListBean addressesListBean = new AddressesListBean();
                            addressesListBean.setAddress_id(address.getString("address_id"));
                            addressesListBean.setAddress(address.getString("address"));
                            addressesListBean.setHouse(address.getString("house"));
                            addressesListBean.setLandmark(address.getString("landmark"));
                            addressesListBean.setLatitude(address.getString("latitude"));
                            addressesListBean.setLongitude(address.getString("longitude"));
                            addressesListBean.setLabel(address.getString("label"));
                            addressesListBean.setPincode(address.getString("pincode"));

                            addressesList.add(addressesListBean);
                        }
                         addAddressListAdapter = new AddAddressListAdapter(ManageAddressActivity.this, addressesList, isSetAddressButtonVisible, ManageAddressActivity.this::onSetAddressClicked);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ManageAddressActivity.this);
                        mrecyclerView.setLayoutManager(linearLayoutManager);
                        mrecyclerView.setAdapter(addAddressListAdapter);

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
        mrecyclerView = findViewById(R.id.activity_manage_address_recyclerView);
        mbtnAddAddress = findViewById(R.id.activity_manage_address_btn_addAddress);
        mimgBackIcon = findViewById(R.id.activity_manage_address_img_backIcon);

        mCheckNetworkConnection = new CheckNetworkConnection();
        mApiRequest = new ApiRequest(this);
    }

    @Override
    public void onSetAddressClicked(AddressesListBean addressesListBean) {
        boolean isOpenedByOrderSummary = false; // Assume it's not opened by OrderSummary by default
        Activity activity = (Activity) this;
        Intent intent = activity.getIntent();
        if (intent != null) {
            String callingActivity = intent.getStringExtra("calling_activity");
            if ("OrderSummary".equals(callingActivity)) {
                isOpenedByOrderSummary = true;
            }
        }

        if (isOpenedByOrderSummary) {
            // Send the address details back to OrderSummary activity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_address", (CharSequence) addressesListBean.getLabel()+"-"+addressesListBean.getAddress());
            activity.setResult(Activity.RESULT_OK, resultIntent);
            activity.finish(); // Finish the current activity
        } else {
            activity.onBackPressed(); // Go back to the previous activity
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Update the activity content when it resumes
        CallApiManageAddress();
    }
}