package com.example.homeservicesdemo.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.homeservicesdemo.R;

import com.example.homeservicesdemo.adapters.CategoryItemAdapter;
import com.example.homeservicesdemo.adapters.DiscountAdapter;
import com.example.homeservicesdemo.adapters.ServicesAdapter;
import com.example.homeservicesdemo.adapters.VarientAdapter;
import com.example.homeservicesdemo.helperclass.DataBaseHelper;
import com.example.homeservicesdemo.models.DiscountBean;
import com.example.homeservicesdemo.models.HowItWork;
import com.example.homeservicesdemo.models.ServiceExcluded;
import com.example.homeservicesdemo.models.ServiceIncluded;
import com.example.homeservicesdemo.models.ServicesBean;
import com.example.homeservicesdemo.models.SubCategoryBean;
import com.example.homeservicesdemo.models.VarientBean;
import com.example.homeservicesdemo.utils.ApiRequest;
import com.example.homeservicesdemo.utils.CheckNetworkConnection;
import com.example.homeservicesdemo.utils.Singleton;
import com.example.homeservicesdemo.utils.VolleyResponseListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServicesActivity extends AppCompatActivity implements CategoryItemAdapter.ApiCallback, ServicesAdapter.AdapterCallback {
    public CollapsingToolbarLayout collapsingToolbarLayout;
    public NestedScrollView nestedScrollView;
    public ImageView mimgbanner,imgBackIcon;
    public TextView mtxtName,mtxtAmount,mViewCart,mTextViewTitle;
    private CheckNetworkConnection mCheckNetworkConnection;
    private ApiRequest mApiRequest;
    public RecyclerView mrecyclerViewDiscount,mrecyclerViewServicesCategory,mrecyclerViewServicesItems;
    private String userId,msubCatId="";
    private SharedPreferences sharedPreferences;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private List<SubCategoryBean> subCategoryBeanList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_ex);

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id","");

        intializeViews();

        callApiServices();

        AddDiscountItems();

        subCategoryItems();

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    // Collapsed
                    mTextViewTitle.setVisibility(View.VISIBLE);
                    mToolbar.setBackgroundResource(R.color.white);
                } else if (verticalOffset == 0) {
                    // Expanded
                    mTextViewTitle.setVisibility(View.GONE);
                    mToolbar.setBackgroundResource(android.R.color.transparent);
                } else {
                    // Somewhere in between
                    mTextViewTitle.setVisibility(View.GONE);
                    mToolbar.setBackgroundResource(android.R.color.transparent);
                }
            }
        });

       mViewCart.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent =new Intent(ServicesActivity.this,OrderSummery.class);
               intent.putExtra("cat_id",getIntent().getStringExtra("cat_id"));
               intent.putExtra("cat_name", (CharSequence) mtxtName);
               startActivity(intent);
           }
       });

       imgBackIcon.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               onBackPressed();
           }
       });

        DataBaseHelper mDatabaseHelper = new DataBaseHelper(this); // Initialize your database helper

        String categoryId = getIntent().getStringExtra("cat_id");
        double totalPrice = mDatabaseHelper.getTotalCartPrice(categoryId);
        mtxtAmount.setText("₹"+String.valueOf(totalPrice));


    }
    private void callApiServices() {
        JSONObject jsonObjectMap = new JSONObject();
        try {
            jsonObjectMap.put("user_id", userId);
            jsonObjectMap.put("category_id", getIntent().getStringExtra("cat_id"));
            jsonObjectMap.put("sub_cat_id", msubCatId);
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
                        if (jsonObject.has("category_list")) {
                            JSONArray categoryListArray = jsonObject.getJSONArray("category_list");

                            if (categoryListArray.length() > 0) {
                                JSONObject firstObject = categoryListArray.getJSONObject(0);
                                mtxtName.setText(firstObject.getString("category_name"));
                                mTextViewTitle.setText(firstObject.getString("category_name"));

                                JSONArray bannerArray = firstObject.getJSONArray("banners");
                                if (bannerArray.length() > 0) {
                                    JSONObject bannerImgs = bannerArray.getJSONObject(0);
                                    Picasso.get().load(bannerImgs.getString("banner_img")).fit().centerCrop().into(mimgbanner);
                                }
                            }
                        }else {
                            // Handle the case where "category_list" key is missing from the JSON response
                            Log.e("Error", "No category_list key found in JSON response");
                        }
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

                                servicesBean.setVariants(parseVarient(serviceObject.getJSONArray("variants")));

                                servicesBean.setCompany_cover((ArrayList<Object>) serviceObject.get("company_cover"));
                                servicesBean.setPlease_note((ArrayList<Object>) serviceObject.get("please_note"));
                                servicesBean.setCustomer_reviews((ArrayList<Object>) serviceObject.get("customer_reviews"));
                                servicesBean.setAbout_service((ArrayList<Object>) serviceObject.get("about_service"));

                                servicesBean.setService_included(parseServiceIncluded(serviceObject.getJSONArray("service_included")));
                                servicesBean.setService_excluded(parseServiceExcluded(serviceObject.getJSONArray("service_excluded")));
                                servicesBean.setHow_it_works(parseHowItWork(serviceObject.getJSONArray("how_it_works")));
                                servicesBeansList.add(servicesBean);

                            }

                            /*ServicesAdapter servicesAdapter = new ServicesAdapter(ServicesActivity.this, servicesBeansList,new DataBaseHelper(ServicesActivity.this));*/
                            // Instantiate the adapter in the activity
                            ServicesAdapter servicesAdapter = new ServicesAdapter(ServicesActivity.this, servicesBeansList, new DataBaseHelper(ServicesActivity.this), ServicesActivity.this);

                            mrecyclerViewServicesItems.setLayoutManager(new LinearLayoutManager(ServicesActivity.this));
                            mrecyclerViewServicesItems.setAdapter(servicesAdapter);

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
    private ArrayList<VarientBean> parseVarient(JSONArray variants) {
        ArrayList <VarientBean> varientList = new ArrayList<>();
        try {
            for (int i=0;i< variants.length();i++){
                JSONObject jsonObject = variants.getJSONObject(i);

                VarientBean varientBean = new VarientBean();
                varientBean.setVariant_id(jsonObject.getString("variant_id"));
                varientBean.setVariant_name(jsonObject.getString("variant_name"));
                varientBean.setVariant_mrp(jsonObject.getString("variant_mrp"));
                varientBean.setVariant_price(jsonObject.getString("variant_price"));
                varientBean.setVarient_note1(jsonObject.getString("variant_note1"));
                varientBean.setVarient_note2(jsonObject.getString("variant_note2"));
                varientBean.setImage(jsonObject.getString("image"));
                varientBean.setVariant_rating(jsonObject.getString("variant_rating"));

                varientList.add(varientBean);

            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return varientList;
    }

    private ArrayList<HowItWork> parseHowItWork(JSONArray howItWorks) {

        ArrayList<HowItWork> howItWorkList = new ArrayList<>();
        try {
            for (int i = 0; i < howItWorks.length(); i++) {
                JSONObject jsonObject = howItWorks.getJSONObject(i);

                HowItWork howItWork = new HowItWork();
                howItWork.setId(jsonObject.getString("id"));
                howItWork.setTitle(jsonObject.getString("title"));
                howItWork.setImage(jsonObject.getString("image"));
                howItWork.setDescription(jsonObject.getString("description"));

                howItWorkList.add(howItWork);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return howItWorkList;
    }
    private ArrayList<ServiceExcluded> parseServiceExcluded(JSONArray serviceExcluded) {
        ArrayList<ServiceExcluded> serviceExcludedList = new ArrayList<>();
        try {
            for (int i = 0; i < serviceExcluded.length(); i++) {
                JSONObject jsonObject = serviceExcluded.getJSONObject(i);

                ServiceExcluded serviceExcludedBean = new ServiceExcluded();

                serviceExcludedBean.setId(jsonObject.getString("id"));
                serviceExcludedBean.setText(jsonObject.getString("text"));

                serviceExcludedList.add(serviceExcludedBean);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return serviceExcludedList;
    }
    private ArrayList<ServiceIncluded> parseServiceIncluded(JSONArray serviceIncluded) {
        System.out.println("Included:" + serviceIncluded.length());
        ArrayList<ServiceIncluded> serviceIncludedList = new ArrayList<>();
        try {
            for (int i = 0; i < serviceIncluded.length(); i++) {
                JSONObject jsonObject = serviceIncluded.getJSONObject(i);

                ServiceIncluded serviceIncludedBean = new ServiceIncluded();
                serviceIncludedBean.setId(jsonObject.getString("id"));
                serviceIncludedBean.setText(jsonObject.getString("text"));

                serviceIncludedList.add(serviceIncludedBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return serviceIncludedList;
    }
    private void intializeViews() {

        collapsingToolbarLayout = findViewById(R.id.activity_home_service_collapsingToolbarLayout);

        mToolbar =findViewById(R.id.activity_home_service_toolbar);
        mAppBarLayout = findViewById(R.id.activity_home_service_appBarLayout);

        mTextViewTitle=findViewById(R.id.activity_home_service_textView_title);
        mtxtName = findViewById(R.id.activity_home_service_textView_title_2);
        mimgbanner = findViewById(R.id.activity_home_service_imageView_cat_pic);

        mrecyclerViewDiscount = findViewById(R.id.activity_home_service_recyclerView_offers);
        mrecyclerViewServicesItems = findViewById(R.id.activity_home_service_recyclerView_service);
        mrecyclerViewServicesCategory = findViewById(R.id.activity_home_service_recyclerView_sub_category);

        mViewCart = findViewById(R.id.activity_home_service_textView_view_cart);
        mtxtAmount = findViewById(R.id.activity_home_service_textView_total_amount);

        nestedScrollView = findViewById(R.id.activity_home_service_nestedScrollView);

        imgBackIcon=findViewById(R.id.activity_home_service_imageView_back);

        mCheckNetworkConnection = new CheckNetworkConnection();
        mApiRequest = new ApiRequest(this);
    }
    private void subCategoryItems() {
        JSONObject jsonObjectMap = new JSONObject();
        try {
            jsonObjectMap.put("user_id", userId);
            jsonObjectMap.put("cat_id", getIntent().getStringExtra("cat_id"));
            jsonObjectMap.put("token","Q62qY9851ZM5BXHDzxWScEJfKTZAP8Jp");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        mApiRequest.callPostJson(this, jsonObjectMap, Singleton.getInstance().getUrlSubCategory(), new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {
                System.out.println("SubCategoryItems: response " + response.toString());

            }

            @Override
            public void onJsonResponse(JSONObject jsonObject) {
                System.out.println("SubCategoryItems: jsonresponse " + jsonObject.toString());


                try {

                    String status = jsonObject.getString("status");

                    if ("success".equals(status)) {

                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataArrayJSONObject = dataArray.getJSONObject(i);

                            SubCategoryBean subCategoryBean = new SubCategoryBean();

                            subCategoryBean.setId(dataArrayJSONObject.getString("id"));
                            subCategoryBean.setBanner(dataArrayJSONObject.getString("banner"));
                            subCategoryBean.setImage(dataArrayJSONObject.getString("image"));
                            subCategoryBean.setSubcategory_name(dataArrayJSONObject.getString("subcategory_name"));
                            subCategoryBeanList.add(subCategoryBean);
                        }
                        CategoryItemAdapter categoryItemAdapter = new CategoryItemAdapter(ServicesActivity.this, subCategoryBeanList,ServicesActivity.this);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(ServicesActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        mrecyclerViewServicesCategory.setLayoutManager(layoutManager);
                        mrecyclerViewServicesCategory.setAdapter(categoryItemAdapter);
                    }

                } catch(JSONException e){
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
    public void onSubCategorySelected(String subCatId) {
        msubCatId = subCatId;
        callApiServices();
    }

    private void AddDiscountItems() {
        ArrayList<DiscountBean> staticDiscountItems = new ArrayList<>();

        DiscountBean discountBean =new DiscountBean();
        discountBean.setText1("Save 15% on Your booking");
        discountBean.setText2("Get Plus now");
        staticDiscountItems.add(discountBean);

        DiscountBean discountBean2 = new DiscountBean();
        discountBean2.setText1("20% off on Kotak Silk catton");
        discountBean2.setText2(" 20% off up to IMR 350");
        staticDiscountItems.add(discountBean2);

        DiscountBean discountBean3 = new DiscountBean();
        discountBean3.setText1("Assured cashback on C.....");
        discountBean3.setText2("Get CashBack of IMR 100");
        staticDiscountItems.add(discountBean3);

        DiscountBean discountBean5 = new DiscountBean();
        discountBean5.setText1("Another Discount Offer");
        discountBean5.setText2("Exclusive Deal");
        staticDiscountItems.add(discountBean5);

        DiscountBean discountBean4 =new DiscountBean();
        discountBean4.setText1("15% off on Kotak Debit Card");
        discountBean4.setText2("15% off up to INR 250");
        staticDiscountItems.add(discountBean4);


        DiscountAdapter discountAdapter = new DiscountAdapter(this, staticDiscountItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mrecyclerViewDiscount.setLayoutManager(layoutManager);
        mrecyclerViewDiscount.setAdapter(discountAdapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Update the activity content when it resumes
        updateActivityContent();
        intializeViews();

        callApiServices();

        AddDiscountItems();

        subCategoryItems();
    }

    private void updateActivityContent() {
        // Update the total price
        DataBaseHelper mDatabaseHelper = new DataBaseHelper(this);
        String categoryId = getIntent().getStringExtra("cat_id");
        double totalPrice = mDatabaseHelper.getTotalCartPrice(categoryId);
        mtxtAmount.setText(String.valueOf(totalPrice));
    }

    @Override
    public void onAdapterItemChanged() {
        // Implement your logic to refresh the activity content
        updateActivityContent();
    }

    public void showDialogAddVarient(ServicesBean serviceItem) {
        final Dialog dialog = new Dialog(ServicesActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_add_varient);

        LinearLayout mllClose;

        TextView mtxtTitle,mtxtRating,mtxtPrice,mtxtTotalPrize,mtxtdone;

        RecyclerView mVarientRecyclerView;
        ArrayList<VarientBean> mVarient = serviceItem.getVariants();

        mllClose = dialog.findViewById(R.id.bottom_sheet_add_varient_linearLayoutclose);

        mtxtTitle = dialog.findViewById(R.id.bottom_sheet_add_varient_textView_name);
        mtxtPrice = dialog.findViewById(R.id.bottom_sheet_add_varient_textview_price);
        mtxtRating = dialog.findViewById(R.id.bottom_sheet_add_varient_textview_rating);
        mtxtTotalPrize = dialog.findViewById(R.id.bottom_sheet_add_varient_textView_total_amount);
        mtxtdone = dialog.findViewById(R.id.bottom_sheet_add_varient_textView_done);

        mVarientRecyclerView = dialog.findViewById(R.id.bottom_sheet_add_varient_recyclerViewVarient);

        VarientAdapter varientAdapter = new VarientAdapter(ServicesActivity.this, mVarient, new DataBaseHelper(ServicesActivity.this), ServicesActivity.this);
        mVarientRecyclerView.setLayoutManager(new LinearLayoutManager(ServicesActivity.this));
        mVarientRecyclerView.setAdapter(varientAdapter);

        mtxtTitle.setText(serviceItem.getCategory_name());
        mtxtPrice.setText("₹"+serviceItem.getPrice());
        mtxtRating.setText(serviceItem.getService_rating());

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