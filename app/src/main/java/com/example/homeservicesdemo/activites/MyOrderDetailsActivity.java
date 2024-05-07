package com.example.homeservicesdemo.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.VolleyError;
import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.adapters.ExpandableAdapter;
import com.example.homeservicesdemo.models.ServiceDetailsBean;
import com.example.homeservicesdemo.models.VarientBean;
import com.example.homeservicesdemo.utils.ApiRequest;
import com.example.homeservicesdemo.utils.CheckNetworkConnection;
import com.example.homeservicesdemo.utils.Singleton;
import com.example.homeservicesdemo.utils.VolleyResponseListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MyOrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mtxtViewID,mtxtViewtitle,mtxtViewDate,mtxtViewTime,mtxtViewPrice,mtxtViewStatus,mtxtViewAddress,mtxtViewReshedule,mtxtViewCancelBooking,mtxttechnicianName,mtxtRating,mtxtRateOurService,mtxtViewProfile;
    private LinearLayout mllCancelationPolicy,mllServiceDetails,mllTechnicianLayout,mllCancelationlayout;
    private ImageView mimgTechnicianImg,mimgBackIcon;
    private LinkedHashMap<String,ServiceDetailsBean> serviceDetailsBeanLinkedHashMap=new LinkedHashMap<String,ServiceDetailsBean>();
    private ArrayList<ServiceDetailsBean> serviceDB= new ArrayList<>();
    private ExpandableAdapter expandableAdapter;
    private ApiRequest mApiRequest;
    private CheckNetworkConnection mCheckNetworkConnection;
    private String userId,oId,technicianId;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_details);

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id","");
        oId = getIntent().getStringExtra("id");
        initializeView();

        if (mCheckNetworkConnection.isNetworkAvailable(this)) {
            CallApi();
        }
        else Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        mimgBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mtxtViewReshedule.setOnClickListener(this);
        mtxtViewCancelBooking.setOnClickListener(this);
        mllServiceDetails.setOnClickListener(this);
        mllCancelationPolicy.setOnClickListener(this);
        mtxtRateOurService.setOnClickListener(this);
    }
    private void CallApi() {
        JSONObject jsonObjectMap = new JSONObject();
        try {
            jsonObjectMap.put("user_id", userId);
            jsonObjectMap.put("token", "Q62qY9851ZM5BXHDzxWScEJfKTZAP8Jp");
            jsonObjectMap.put("order_id",oId);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        mApiRequest.callPostJson(this, jsonObjectMap, Singleton.getInstance().getUrlBookingDetails(), new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {
                System.out.println("MyOrdersDetailsPageActivity: response " + response.toString());
            }
            @Override
            public void onJsonResponse(JSONObject jsonObject) {
                System.out.println("MyOrdersDetailsPageActivity: jsonresponse " + jsonObject.toString());
                try {

                    String status = jsonObject.getString("status");

                    if ("success".equals(status)) {
                        JSONArray dataArray = jsonObject.getJSONArray("data");

                        JSONObject orderDetails = dataArray.getJSONObject(0);

                        mtxtViewID.setText(orderDetails.getString("order_id"));
                        mtxtViewDate.setText(orderDetails.getString("order_date"));
                        mtxtViewTime.setText(orderDetails.getString("order_time"));
                        mtxtViewPrice.setText(orderDetails.getString("pay_amount"));
                        String Orderstatus =orderDetails.getString("status");
                        mtxtViewStatus.setText(orderDetails.getString("status"));
                        mtxtViewAddress.setText(orderDetails.getString("order_address"));

                        Boolean reshedhule = Boolean.valueOf(orderDetails.getString("reschedule_status"));

                        if (reshedhule){
                            mllCancelationlayout.setVisibility(View.VISIBLE);
                        }
                        else mllTechnicianLayout.setVisibility(View.GONE);


                        JSONArray serviceDetailsArray = orderDetails.getJSONArray("order_details");
                        for (int i = 0; i < serviceDetailsArray.length(); i++) {

                            JSONObject serviceDetails = serviceDetailsArray.getJSONObject(i);

                            ServiceDetailsBean serviceDetailsBean = new  ServiceDetailsBean();

                            serviceDetailsBean.setService_id(serviceDetails.getString("service_id"));
                            serviceDetailsBean.setId(serviceDetails.getString("id"));
                            serviceDetailsBean.setCategory_id(serviceDetails.getString("category_id"));
                            mtxtViewtitle.setText(serviceDetails.getString("category_name"));
                            serviceDetailsBean.setSubcat_id(serviceDetails.getString("subcat_id"));
                            serviceDetailsBean.setSubcategory_name(serviceDetails.getString("subcategory_name"));
                            serviceDetailsBean.setType(serviceDetails.getString("type"));
                            serviceDetailsBean.setService_name(serviceDetails.getString("service_name"));
                            serviceDetailsBean.setService_mrp(serviceDetails.getString("service_mrp"));
                            serviceDetailsBean.setService_price(serviceDetails.getString("service_price"));
                            serviceDetailsBean.setService_qty(serviceDetails.getString("service_qty"));
                            serviceDetailsBean.setSubcat_id(serviceDetails.getString("subcat_id"));
                            serviceDetailsBean.setSubcategory_name(serviceDetails.getString("subcategory_name"));

                            List<VarientBean> varientBeanArrayList = new ArrayList<>();
                            JSONArray varientArray = serviceDetails.getJSONArray("variant");
                            for (int j =0;j< varientArray.length();j++){
                                JSONObject variantObject = varientArray.getJSONObject(i);
                                VarientBean varientBean = new VarientBean();

                                varientBean.setVariant_id(variantObject.getString("variant_id"));
                                varientBean.setVariant_name(variantObject.getString("variant_name"));
                                varientBean.setVariant_qty(variantObject.getString("variant_qty"));
                                varientBean.setVariant_mrp(variantObject.getString("variant_mrp"));
                                varientBean.setVariant_price(variantObject.getString("variant_price"));
                                varientBeanArrayList.add(varientBean);

                            }
                            serviceDetailsBean.setVariant((ArrayList<VarientBean>) varientBeanArrayList);
                            serviceDB.add(serviceDetailsBean);
                        }
                        JSONArray additionalServiceArray = orderDetails.getJSONArray("additional_service");
                        for (int i =0;i< additionalServiceArray.length();i++){
                            JSONObject additionalService = additionalServiceArray.getJSONObject(i);
                        }
                        JSONArray CustomerRatingArray = orderDetails.getJSONArray("customer_rating");
                        for (int i =0; i< CustomerRatingArray.length();i++){
                            JSONObject customerRating = CustomerRatingArray.getJSONObject(i);
                        }
                        JSONArray technicianArray = orderDetails.getJSONArray("technician");

                        for (int i =0; i< technicianArray.length();i++){
                            JSONObject technicianJSONObject = technicianArray.getJSONObject(i);

                            mtxttechnicianName.setText(technicianJSONObject.getString("name"));
                            technicianId = technicianJSONObject.getString("technician_id");

                            if (!technicianJSONObject.getString("pic").isEmpty()) {
                                Picasso.get().load(technicianJSONObject.getString("pic")).fit().centerCrop().into(mimgTechnicianImg);
                            }
                            mtxtRating.setText(technicianJSONObject.getString("star_rate"));
                            mllCancelationlayout.setVisibility(View.GONE);
                            mllTechnicianLayout.setVisibility(View.VISIBLE);
                            mtxtRateOurService.setVisibility(View.VISIBLE);
                            if (Orderstatus == "Pending") {
                                mllCancelationlayout.setVisibility(View.GONE);
                                mllTechnicianLayout.setVisibility(View.GONE);
                            }
                            else if (Orderstatus == "Assigned") {
                                mtxtRateOurService.setVisibility(View.VISIBLE);
                            }
                        }
                        if (Orderstatus == "Completed"){
                            mllCancelationlayout.setVisibility(View.GONE);
                            mllTechnicianLayout.setVisibility(View.VISIBLE);
                            mtxtRateOurService.setVisibility(View.VISIBLE);
                        } else if (Orderstatus == "No status") {
                            mllCancelationlayout.setVisibility(View.GONE);
                            mllTechnicianLayout.setVisibility(View.GONE);
                        } else if (Orderstatus == "New") {
                            mllCancelationlayout.setVisibility(View.VISIBLE);
                            mllTechnicianLayout.setVisibility(View.GONE);
                        } else if (Orderstatus == "Assigned") {
                            mllCancelationlayout.setVisibility(View.GONE);
                            mllTechnicianLayout.setVisibility(View.VISIBLE);
                            mtxtRateOurService.setVisibility(View.VISIBLE);
                        }else if (Orderstatus == "Pending") {
                            mllCancelationlayout.setVisibility(View.GONE);
                            mllTechnicianLayout.setVisibility(View.GONE);
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
    private void initializeView() {

        mtxtViewID =findViewById(R.id.activity_my_order_details_txtView_id);
        mtxtViewtitle =findViewById(R.id.activity_my_order_details_txtView_title);
        mtxtViewDate = findViewById(R.id.activity_my_order_details_txtView_date);
        mtxtViewTime =findViewById(R.id.activity_my_order_details_txtView_time);
        mtxtViewPrice =findViewById(R.id.activity_my_order_details_txtView_price);
        mtxtViewStatus =findViewById(R.id.activity_my_order_details_txtView_status);
        mtxtViewAddress  =findViewById(R.id.activity_my_order_details_txtView_address);
        mtxtViewReshedule =findViewById(R.id.activity_my_order_details_txtView_reshedule);
        mtxtViewCancelBooking =findViewById(R.id.activity_my_order_details_txtView_cancelBooking);

        mtxttechnicianName=findViewById(R.id.activity_my_order_details_txtView_technicianName);
        mtxtRating = findViewById(R.id.activity_my_order_details_txtView_starRating);
        mtxtRateOurService =findViewById(R.id.activity_my_order_details_txtView_rateOurService);
        mtxtViewProfile = findViewById(R.id.activity_my_order_details_txtView_viewProfile);

        mllTechnicianLayout = findViewById(R.id.activity_my_order_details_linearlayout_technician);
        mllCancelationPolicy =findViewById(R.id.activity_my_order_details_linearlayout_cancellationPolicy);
        mllServiceDetails =findViewById(R.id.activity_my_order_details_serviceDetails);
        mllCancelationlayout = findViewById(R.id.activity_my_order_details_linearlayout_cancellationlayout);

        mimgBackIcon = findViewById(R.id.activity_my_order_details_img_backIcon);

        mimgTechnicianImg = findViewById(R.id.activity_my_order_details_img_technicianImg);

        mCheckNetworkConnection = new CheckNetworkConnection();
        mApiRequest = new ApiRequest(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mtxtViewReshedule){
            Intent intent =new Intent(this,TimeSlotActivity.class);
            intent.putExtra("id", (CharSequence) mtxtViewID);
            intent.putExtra("date",(CharSequence)mtxtViewDate);
            intent.putExtra("time",(CharSequence)mtxtViewTime);
            intent.putExtra("amount",(CharSequence)mtxtViewPrice);
            intent.putExtra("status",(CharSequence)mtxtViewStatus);
            intent.putExtra("address",(CharSequence)mtxtViewAddress);
            intent.putExtra("title",(CharSequence)mtxtViewtitle);
            startActivity(intent);
        } else if (v ==mllServiceDetails) {
            showDialogViewDetails();
        } else if (v == mllCancelationPolicy) {
            
        } else if (v == mtxtRateOurService) {
            showDialogRateOurServices();
        } else if (v == mtxtViewCancelBooking) {

        }
    }
    private void CallApiRateOurService() {
        JSONObject jsonObjectMap = new JSONObject();
        try {
            jsonObjectMap.put("user_id", userId);
            jsonObjectMap.put("token", "Q62qY9851ZM5BXHDzxWScEJfKTZAP8Jp");
            jsonObjectMap.put("order_id",oId);
            jsonObjectMap.put("technician_id",technicianId);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        mApiRequest.callPostJson(this, jsonObjectMap, Singleton.getInstance().getUrlBookingDetails(), new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {
                System.out.println("MyOrdersDetailsPageActivity RateOurService: response " + response.toString());
            }
            @Override
            public void onJsonResponse(JSONObject jsonObject) {
                System.out.println("MyOrdersDetailsPageActivity RateOurService: jsonresponse " + jsonObject.toString());
                try {

                    String status = jsonObject.getString("status");
                    String Message = jsonObject.getString("msg");

                    if ("success".equals(status)) {
                        Toast.makeText(MyOrderDetailsActivity.this, Message, Toast.LENGTH_SHORT).show();
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
    private void showDialogRateOurServices() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_rateourservices);

        LinearLayout mllClose;
        TextView mtxtViewComment;
        Button mbtnSubmit;
        RatingBar mratingBar;

        mllClose = dialog.findViewById(R.id.bottom_sheet_rateourservices_linearLayoutclose);
        mtxtViewComment = dialog.findViewById(R.id.bottom_sheet_rateourservices_editText_comment);
        mratingBar = dialog.findViewById(R.id.bottom_sheet_rateourservices_ratingbar);
        mbtnSubmit = dialog.findViewById(R.id.bottom_sheet_rateourservices_btnSubmit);

        mbtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallApiRateOurService();
            }
        });

        mllClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }
    private void showDialogViewDetails() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_servicesdetails);

        LinearLayout mllClose;
        ExpandableListView expandableListView=dialog.findViewById(R.id.bottom_sheet_servicesDetails_expandableList);

        expandableAdapter = new ExpandableAdapter(this,serviceDB);
        expandableListView.setAdapter(expandableAdapter);


        mllClose = dialog.findViewById(R.id.bottom_sheet_servicesDetails_linearLayoutclose);

        mllClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }
}