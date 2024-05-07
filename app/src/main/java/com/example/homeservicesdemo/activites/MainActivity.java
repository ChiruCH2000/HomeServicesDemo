package com.example.homeservicesdemo.activites;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.adapters.CategoryAdapter;
import com.example.homeservicesdemo.adapters.CategoryStoreAdapter;
import com.example.homeservicesdemo.adapters.MBServicesAdapter;
import com.example.homeservicesdemo.adapters.MBServicesStoreAdapter;
import com.example.homeservicesdemo.models.CategoryBanner;
import com.example.homeservicesdemo.models.CategoryBean;
import com.example.homeservicesdemo.models.MostBookedService;
import com.example.homeservicesdemo.utils.ApiRequest;
import com.example.homeservicesdemo.utils.CheckNetworkConnection;
import com.example.homeservicesdemo.utils.Singleton;
import com.example.homeservicesdemo.utils.VolleyResponseListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public ImageView mimgHomeIcon,mimgStoreIcon,mimgProfileIcon;
    public TextView mtxtHomeService,mtxtStore,mtxtProfile;
    public LinearLayout mllHome,mllStore,mllProfile;
    public BottomNavigationView bnView;
    public TextView mtxtName,mtxtMobileNo,mtxtEmail,mtxtLogout;
    public ImageView mimgEdit,mimgHomeCart,mimgStoreCart;
    public LinearLayout mllHelpCenter,mllManageAddress,mllMyBooking,mllRegisterAsPartner,mllAboutOurCompany,mllShareOurCompany,mllSettings,llHomeSearch,llStoreSearch;
    public ImageSlider mimgHomeSliderBanners,mimgStoreSliderBanners;
    public RecyclerView mrecyclerViewHome,mrecyclerViewStore;
    public RecyclerView mrecyViewMBServicesHome,mrecyViewMBServicesStore;
    private CheckNetworkConnection mCheckNetworkConnection;
    private ApiRequest mApiRequest;
    public List<CategoryBean> categoryBeanList;
    public List<MostBookedService> mostBookedServiceList;
    public View mincludeHome,mincludeStore,mincludeProfile;
    public Context context;
    private String userId;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id","");


        bnView=findViewById(R.id.activity_main_bottom_nav);
        initializeViews();
        if (mCheckNetworkConnection.isNetworkAvailable(context)) {
            CallApiHome();
            mimgHomeIcon.setColorFilter(0xFF664BC1);
            mtxtHomeService.setTextColor(0xFF664BC1);

            mimgStoreIcon.setColorFilter(Color.GRAY);
            mtxtStore.setTextColor(Color.GRAY);

            mimgProfileIcon.setColorFilter(Color.GRAY);
            mtxtProfile.setTextColor(Color.GRAY);
        } else {
            Toast.makeText(MainActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
        mllHome.setOnClickListener(this);
        mllStore.setOnClickListener(this);
        mllProfile.setOnClickListener(this);

        llHomeSearch.setOnClickListener(this);
        llStoreSearch.setOnClickListener(this);

        mimgEdit.setOnClickListener(this);
        mtxtLogout.setOnClickListener(this);

        mllManageAddress.setOnClickListener(this);
        mllMyBooking.setOnClickListener(this);

        mimgStoreCart.setOnClickListener(this);
        mimgHomeCart.setOnClickListener(this);

    }
    private void CallApiHome() {
        JSONObject jsonObjectMap = new JSONObject();
        try {
            jsonObjectMap.put("user_id", userId);
            jsonObjectMap.put("token","Q62qY9851ZM5BXHDzxWScEJfKTZAP8Jp");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        mApiRequest.callPostJson(context, jsonObjectMap, Singleton.getInstance().getUrlHomePage(), new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {
                System.out.println("HomePageActivity: response " + response);
            }
            @Override
            public void onJsonResponse(JSONObject jsonObject) {
                System.out.println("HomePageActivity: jsonresponse " + jsonObject.toString());
                try {
                    String status = jsonObject.getString("status");

                    if ("success".equals(status)) {
                        JSONArray bannersArray = jsonObject.getJSONArray("banners");
                        List<SlideModel> imageUrls = new ArrayList<>();
                        for (int i = 0; i < bannersArray.length(); i++) {
                            JSONObject bannerObj = bannersArray.getJSONObject(i);
                            String imageUrl = bannerObj.getString("banner_img");
                            imageUrls.add(new SlideModel(imageUrl, "", ScaleTypes.FIT));
                        }
                        mimgHomeSliderBanners.setImageList(imageUrls);

                        JSONArray categoryArray = jsonObject.getJSONArray("category");
                        categoryBeanList =new ArrayList<>();
                        for (int i = 0; i < categoryArray.length(); i++) {
                            JSONObject cAJsonObject = categoryArray.getJSONObject(i);
                            CategoryBean categoryBean = new CategoryBean();
                            categoryBean.setId(cAJsonObject.getString("id"));
                            categoryBean.setCategory_name(cAJsonObject.getString("category_name"));
                            categoryBean.setImage(cAJsonObject.getString("image"));
                            if (cAJsonObject.has("category_banners")) {
                                JSONArray cBArry = cAJsonObject.getJSONArray("category_banners");
                                List<CategoryBanner> categoryBannerList = new ArrayList<>();
                                for (int j = 0; j < cBArry.length(); j++) {
                                    CategoryBanner cBBean = new CategoryBanner();
                                    JSONObject cBList = cBArry.getJSONObject(j);
                                    cBBean.setId(cBList.getString("id"));
                                    cBBean.setBanners(cBList.getString("banners"));
                                    categoryBannerList.add(cBBean);
                                }
                                categoryBean.setCategory_banners((ArrayList<CategoryBanner>) categoryBannerList);
                            }
                            categoryBeanList.add(categoryBean);
                        }
                        CategoryAdapter categoryAdapter = new CategoryAdapter(MainActivity.this,categoryBeanList);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,3);
                        mrecyclerViewHome.setLayoutManager(gridLayoutManager);
                        mrecyclerViewHome.setAdapter(categoryAdapter);

                        JSONArray mostBookedServiceArray = jsonObject.getJSONArray("most_booked_service");
                        mostBookedServiceList =new ArrayList<>();
                        for (int i =0 ; i < mostBookedServiceArray.length(); i++){
                            JSONObject mBServicesJsonObject = mostBookedServiceArray.getJSONObject(i);
                            MostBookedService mostBookedService = new MostBookedService();
                            mostBookedService.setService_id(mBServicesJsonObject.getString("service_id"));
                            mostBookedService.setTitle(mBServicesJsonObject.getString("title"));
                            mostBookedService.setPrice(mBServicesJsonObject.getString("price"));
                            mostBookedService.setCat_id(mBServicesJsonObject.getString("cat_id"));
                            mostBookedService.setCategory_id(mBServicesJsonObject.getString("category_id"));
                            mostBookedService.setSubcategory_name(mBServicesJsonObject.getString("subcategory_name"));
                            mostBookedService.setBooking_count(mBServicesJsonObject.getString("booking_count"));
                            mostBookedService.setCustomer_rate(mBServicesJsonObject.getString("customer_rate"));
                            mostBookedService.setSubcat_id(mBServicesJsonObject.getString("subcat_id"));
                            mostBookedService.setMrp(mBServicesJsonObject.getString("mrp"));
                            mostBookedService.setImage(mBServicesJsonObject.getString("image"));

                            mostBookedServiceList.add(mostBookedService);
                        }
                        MBServicesAdapter mbServicesAdapter = new MBServicesAdapter(context,mostBookedServiceList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
                        mrecyViewMBServicesHome.setLayoutManager(linearLayoutManager);
                        mrecyViewMBServicesHome.setAdapter(mbServicesAdapter);
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
    private void CallApiStore() {
        JSONObject jsonObjectMap = new JSONObject();
        try {
            jsonObjectMap.put("user_id", userId);
            jsonObjectMap.put("token", "Q62qY9851ZM5BXHDzxWScEJfKTZAP8Jp");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        mApiRequest.callPostJson(context, jsonObjectMap, Singleton.getInstance().getUrlHomePage(), new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {
                System.out.println("StorePageActivity: response " + response.toString());
            }
            @Override
            public void onJsonResponse(JSONObject jsonObject) {
                System.out.println("StorePageActivity: jsonresponse " + jsonObject.toString());
                try {
                    String status = jsonObject.getString("status");

                    if ("success".equals(status)) {
                        JSONArray bannersArray = jsonObject.getJSONArray("banners");
                        List<SlideModel> imageUrls = new ArrayList<>();
                        for (int i = 0; i < bannersArray.length(); i++) {
                            JSONObject bannerObj = bannersArray.getJSONObject(i);
                            String imageUrl = bannerObj.getString("banner_img");
                            imageUrls.add(new SlideModel(imageUrl, "", ScaleTypes.FIT));
                        }
                        mimgStoreSliderBanners.setImageList(imageUrls);

                        JSONArray categoryArray = jsonObject.getJSONArray("category");
                        categoryBeanList = new ArrayList<>();
                        for (int i = 0; i < categoryArray.length(); i++) {
                            JSONObject cAJsonObject = categoryArray.getJSONObject(i);
                            CategoryBean categoryBean = new CategoryBean();
                            categoryBean.setId(cAJsonObject.getString("id"));
                            categoryBean.setCategory_name(cAJsonObject.getString("category_name"));
                            categoryBean.setImage(cAJsonObject.getString("image"));
                            if (cAJsonObject.has("category_banners")) {
                                JSONArray cBArry = cAJsonObject.getJSONArray("category_banners");
                                List<CategoryBanner> categoryBannerList = new ArrayList<>();
                                for (int j = 0; j < cBArry.length(); j++) {
                                    CategoryBanner cBBean = new CategoryBanner();
                                    JSONObject cBList = cBArry.getJSONObject(j);
                                    cBBean.setId(cBList.getString("id"));
                                    cBBean.setBanners(cBList.getString("banners"));
                                    categoryBannerList.add(cBBean);
                                }
                                categoryBean.setCategory_banners((ArrayList<CategoryBanner>) categoryBannerList);

                            }
                            categoryBeanList.add(categoryBean);
                        }
                        CategoryStoreAdapter categoryAdapter = new CategoryStoreAdapter(MainActivity.this, categoryBeanList);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 3);
                        mrecyclerViewStore.setLayoutManager(gridLayoutManager);
                        mrecyclerViewStore.setAdapter(categoryAdapter);

                        JSONArray mostBookedServiceArray = jsonObject.getJSONArray("most_booked_service");
                        mostBookedServiceList = new ArrayList<>();
                        for (int i = 0; i < mostBookedServiceArray.length(); i++) {
                            JSONObject mBServicesJsonObject = mostBookedServiceArray.getJSONObject(i);
                            MostBookedService mostBookedService = new MostBookedService();
                            mostBookedService.setService_id(mBServicesJsonObject.getString("service_id"));
                            mostBookedService.setTitle(mBServicesJsonObject.getString("title"));
                            mostBookedService.setPrice(mBServicesJsonObject.getString("price"));
                            mostBookedService.setCat_id(mBServicesJsonObject.getString("cat_id"));
                            mostBookedService.setCategory_id(mBServicesJsonObject.getString("category_id"));
                            mostBookedService.setSubcategory_name(mBServicesJsonObject.getString("subcategory_name"));
                            mostBookedService.setBooking_count(mBServicesJsonObject.getString("booking_count"));
                            mostBookedService.setCustomer_rate(mBServicesJsonObject.getString("customer_rate"));
                            mostBookedService.setSubcat_id(mBServicesJsonObject.getString("subcat_id"));
                            mostBookedService.setMrp(mBServicesJsonObject.getString("mrp"));
                            mostBookedService.setImage(mBServicesJsonObject.getString("image"));

                            mostBookedServiceList.add(mostBookedService);
                        }
                        MBServicesStoreAdapter mbServicesAdapter = new MBServicesStoreAdapter(MainActivity.this, mostBookedServiceList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        mrecyViewMBServicesStore.setLayoutManager(linearLayoutManager);
                        mrecyViewMBServicesStore.setAdapter(mbServicesAdapter);
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
    private void CallApiProfile() {
        JSONObject jsonObjectMap = new JSONObject();
        try {
            jsonObjectMap.put("user_id", userId);
            jsonObjectMap.put("token", "Q62qY9851ZM5BXHDzxWScEJfKTZAP8Jp");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        mApiRequest.callPostJson(context, jsonObjectMap, Singleton.getInstance().getUrlProfile(), new VolleyResponseListener() {
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
                            mtxtName.setText(firstObject.getString("user_name"));
                            mtxtEmail.setText(firstObject.getString("email_id"));
                            mtxtMobileNo.setText(firstObject.getString("mobile_number"));
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
    private void initializeViews() {

        mllHome = findViewById(R.id.activity_main_llayout_home_services);
        mllStore = findViewById(R.id.activity_main_llayout_store);
        mllProfile = findViewById(R.id.activity_main_llayout_profile);

        mimgHomeCart =findViewById(R.id.activity_home_imageView_cart);
        mimgStoreCart=findViewById(R.id.activity_store_imageView_cart);

        mimgHomeIcon = findViewById(R.id.home_icon);
        mimgStoreIcon = findViewById(R.id.store_icon);
        mimgProfileIcon = findViewById(R.id.profile_icon);

        mtxtHomeService = findViewById(R.id.home_text);
        mtxtStore = findViewById(R.id.store_text);
        mtxtProfile = findViewById(R.id.profile_text);

        mincludeHome = findViewById(R.id.activity_main_include_home_layout);
        mincludeStore = findViewById(R.id.activity_main_include_store_layout);
        mincludeProfile = findViewById(R.id.activity_main_include_profile_layout);

        mimgHomeSliderBanners = findViewById(R.id.activity_home_imageSlider_banners);
        mrecyclerViewHome = findViewById(R.id.activity_main_recyclerView);
        mrecyViewMBServicesHome = findViewById(R.id.activity_home_recyclerView_mbServices);

        mimgStoreSliderBanners = findViewById(R.id.activity_store_imageSlider_banners);
        mrecyclerViewStore = findViewById(R.id.activity_store_recyclerView);
        mrecyViewMBServicesStore = findViewById(R.id.activity_store_recyclerView_mbServices);

        mtxtName = findViewById(R.id.activity_profile_textView_name);
        mtxtEmail = findViewById(R.id.activity_profile_textView_email_id);
        mtxtMobileNo = findViewById(R.id.activity_profile_textView_mobile_no);
        mtxtLogout = findViewById(R.id.activity_profile_textView_logOut);
        mimgEdit = findViewById(R.id.activity_profile_imgView_edit);

        mllHelpCenter = findViewById(R.id.activity_profile_llayout_helpCenter);
        mllManageAddress = findViewById(R.id.activity_profile_llayout_manageAddress);
        mllMyBooking = findViewById(R.id.activity_profile_llayout_myBookings);
        mllAboutOurCompany = findViewById(R.id.activity_profile_llayout_abourOurCompany);
        mllRegisterAsPartner = findViewById(R.id.activity_profile_llayout_registerAsPartner);
        mllShareOurCompany = findViewById(R.id.activity_profile_llayout_shareOurCompany);
        mllSettings = findViewById(R.id.activity_profile_llayout_settings);

        llHomeSearch = findViewById(R.id.activity_home_search_layout);
        llStoreSearch =findViewById(R.id.activity_store_search_layout);

        mCheckNetworkConnection = new CheckNetworkConnection();
        mApiRequest = new ApiRequest(context);

    }
    @Override
    public void onClick(View v) {
        if (v == mllHome) {
            if (mCheckNetworkConnection.isNetworkAvailable(context)) {

                mincludeHome.setVisibility(View.VISIBLE);
                mincludeStore.setVisibility(View.GONE);
                mincludeProfile.setVisibility(View.GONE);

                CallApiHome();

                mimgHomeIcon.setColorFilter(0xFF664BC1);
                mtxtHomeService.setTextColor(0xFF664BC1);

                mimgStoreIcon.setColorFilter(Color.GRAY);
                mtxtStore.setTextColor(Color.GRAY);

                mimgProfileIcon.setColorFilter(Color.GRAY);
                mtxtProfile.setTextColor(Color.GRAY);

            } else {
                Toast.makeText(context, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }
        } else if (v == mllStore) {
            if (mCheckNetworkConnection.isNetworkAvailable(context)){

                mincludeStore.setVisibility(View.VISIBLE);
                mincludeProfile.setVisibility(View.GONE);
                mincludeHome.setVisibility(View.GONE);

                CallApiStore();

                mimgStoreIcon.setColorFilter(0xFF664BC1);
                mtxtStore.setTextColor(0xFF664BC1);

                mimgProfileIcon.setColorFilter(Color.GRAY);
                mtxtProfile.setTextColor(Color.GRAY);

                mimgHomeIcon.setColorFilter(Color.GRAY);
                mtxtHomeService.setTextColor(Color.GRAY);

            } else {
                Toast.makeText(context, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }
        } else if (v == mllProfile) {
            if (mCheckNetworkConnection.isNetworkAvailable(context)) {

                mincludeProfile.setVisibility(View.VISIBLE);
                mincludeStore.setVisibility(View.GONE);
                mincludeHome.setVisibility(View.GONE);

                CallApiProfile();

                mimgProfileIcon.setColorFilter(0xFF664BC1);
                mtxtProfile.setTextColor(0xFF664BC1);

                mimgStoreIcon.setColorFilter(Color.GRAY);
                mtxtStore.setTextColor(Color.GRAY);

                mimgHomeIcon.setColorFilter(Color.GRAY);
                mtxtHomeService.setTextColor(Color.GRAY);

            } else {
                Toast.makeText(context, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }
        } else if (v == llHomeSearch) {

            Intent intent = new Intent(this,SearchActivity.class);
            startActivity(intent);

        }else if (v == llStoreSearch) {

            Intent intent = new Intent(this,SearchActivity.class);
            startActivity(intent);

        }
        else if (v == mimgEdit) {

            Intent intent = new Intent(this,MyAccountActivity.class);
            startActivity(intent);

        } else if (v == mtxtLogout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to logout?");

            // Add "Yes" button
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sharedPreferences.edit().putBoolean("isLoggedIn", false);
                    sharedPreferences.edit().apply();

                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Finish the current activity to prevent going back to it
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Dismiss the dialog, do nothing
                    dialog.dismiss();
                }
            });

            builder.show();

        } else if (v == mllManageAddress) {

            Intent intent =new Intent(this,ManageAddressActivity.class);
            startActivity(intent);

        } else if (v == mllMyBooking) {

            Intent intent =new Intent(this,MyOrdersActivity.class);
            startActivity(intent);

        }else if (v == mimgHomeCart) {

            Intent intent =new Intent(this, YourCart.class);
            startActivity(intent);

        }else if (v == mimgStoreCart) {

            Intent intent =new Intent(this,YourCart.class);
            startActivity(intent);

        }
    }
}