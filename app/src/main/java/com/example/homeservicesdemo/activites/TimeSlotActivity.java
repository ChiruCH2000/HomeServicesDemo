package com.example.homeservicesdemo.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.adapters.DateAdapter;
import com.example.homeservicesdemo.adapters.TimeSlotAdapter;
import com.example.homeservicesdemo.models.DayDateModel;
import com.example.homeservicesdemo.models.TimeSlotBean;
import com.example.homeservicesdemo.utils.ApiRequest;
import com.example.homeservicesdemo.utils.CheckNetworkConnection;
import com.example.homeservicesdemo.utils.Singleton;
import com.example.homeservicesdemo.utils.VolleyResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TimeSlotActivity extends AppCompatActivity implements TimeSlotAdapter.OnItemClickListener, View.OnClickListener {
    private RecyclerView mrecyclerView;
    private LinearLayout mllMorning,mllAfternoon,mllEvening,mllOrderDetails;
    private Button mButton;
    private TextView mtxtViewID;
    private TextView mtxtViewtitle;
    private TextView mtxtViewDate;
    private TextView mtxtViewTime;
    private TextView mtxtViewPrice;
    private TextView mtxtViewStatus;
    private TextView mtxtViewAddress;
    private String mTxtViewID;
    private String mTxtViewtitle;
    private String mTxtViewDate;
    private String mTxtViewTime;
    private String mTxtViewPrice;
    private String mTxtViewStatus;
    private String mTxtViewAddress;
    private RecyclerView recyclerViewMorning, recyclerViewEvening, recyclerViewAfterNoon;
    private CheckNetworkConnection mCheckNetworkConnection;
    private ApiRequest mApiRequest;
    private String userId,msubCatId="";
    private SharedPreferences sharedPreferences;
    private ImageView mimgBackIcon;
    int dayOfWeekInt;
    Calendar calendar;
    int dayOfWeekString;
    List<TimeSlotBean> allTimingItems;
    private TimeSlotAdapter morningAdapter, afternoonAdapter, eveningAdapter;
    List<TimeSlotBean> timingItems = new ArrayList<>();
    List<TimeSlotBean> timingItemsAF = new ArrayList<>();
    List<TimeSlotBean> timingItemsEvening = new ArrayList<>();
    private String selectedTime = "";
    private String selectedDate = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slot);

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id","");

        initializeViews();

        mTxtViewtitle = getIntent().getStringExtra("title");
        mTxtViewPrice = getIntent().getStringExtra("amount");
        mTxtViewDate  = getIntent().getStringExtra("date");
        mTxtViewTime = getIntent().getStringExtra("time");
        mTxtViewID =  getIntent().getStringExtra("id");
        mTxtViewAddress = getIntent().getStringExtra("address");
        mTxtViewStatus = getIntent().getStringExtra("status");

        GetDayOfWeek();

        callApiTimeSlot(dayOfWeekInt);

        setupDateRecyclerView();

        mllEvening.setOnClickListener(this);
        mllAfternoon.setOnClickListener(this);
        mllMorning.setOnClickListener(this);

        mimgBackIcon.setOnClickListener(this);
        mButton.setOnClickListener(this);
    }

    private void GetDayOfWeek() {
        calendar = Calendar.getInstance();
        dayOfWeekString = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeekString) {
            case Calendar.SUNDAY:
                dayOfWeekInt = 7;
                break;
            case Calendar.MONDAY:
                dayOfWeekInt = 1;
                break;
            case Calendar.TUESDAY:
                dayOfWeekInt = 2;
                break;
            case Calendar.WEDNESDAY:
                dayOfWeekInt = 3;
                break;
            case Calendar.THURSDAY:
                dayOfWeekInt = 4;
                break;
            case Calendar.FRIDAY:
                dayOfWeekInt = 5;
                break;
            case Calendar.SATURDAY:
                dayOfWeekInt = 6;
                break;
            default:
                dayOfWeekInt = 0; // Handle any unexpected values
                break;
        }
    }
    private void setupDateRecyclerView() {
        List<DayDateModel> dayDateList = new ArrayList<>();
        SimpleDateFormat sdfDay = new SimpleDateFormat("EEE", Locale.getDefault());
        SimpleDateFormat sdfDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 4; i++) {
            String day = sdfDay.format(calendar.getTime());
            String date = sdfDate.format(calendar.getTime());

            dayDateList.add(new DayDateModel(day, date));
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        DateAdapter dateAdapter = new DateAdapter(TimeSlotActivity.this, dayDateList,TimeSlotActivity.this::onDateItemClick);
        mrecyclerView.setLayoutManager(layoutManager);
        mrecyclerView.setAdapter(dateAdapter);
    }

    private void callApiTimeSlot(int dayOfWeek) {
        JSONObject jsonObjectMap = new JSONObject();
        try {
            jsonObjectMap.put("user_id", userId);
            jsonObjectMap.put("day_no", dayOfWeek);
            jsonObjectMap.put("token","Q62qY9851ZM5BXHDzxWScEJfKTZAP8Jp");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        mApiRequest.callPostJson(this, jsonObjectMap, Singleton.getInstance().getUrlTimeSlot(), new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {
                System.out.println("TimeSlotActivity: response " + response.toString());
            }

            @Override
            public void onJsonResponse(JSONObject jsonObject) {
                System.out.println("TimeSlotActivity: jsonresponse " + jsonObject.toString());

                try {

                    String status = jsonObject.getString("status");

                    if ("success".equals(status)) {
                        timingItems.clear();
                        timingItemsAF.clear();
                        timingItemsEvening.clear();

                        JSONArray morningArray = jsonObject.getJSONArray("morning");
                        // Create a list to hold the timing items


                        // Parse the JSON array and extract timing data
                        for (int i = 0; i < morningArray.length(); i++) {

                            JSONObject timingObject = morningArray.getJSONObject(i);

                            TimeSlotBean timeSlotBean = new TimeSlotBean();

                            timeSlotBean.setTimings(timingObject.getString("timings"));
                            timeSlotBean.setId(timingObject.getString("id"));
                            timeSlotBean.setDays(timingObject.getString("days"));
                            timeSlotBean.setWday(timingObject.getString("wday"));
                            timeSlotBean.setCreated_date(timingObject.getString("created_date"));

                            timingItems.add(timeSlotBean);
                        }

                        JSONArray AfternoonArray = jsonObject.getJSONArray("afternoon");
                        // Create a list to hold the timing items


                        // Parse the JSON array and extract timing data
                        for (int i = 0; i < AfternoonArray.length(); i++) {

                            JSONObject timingObject = AfternoonArray.getJSONObject(i);

                            TimeSlotBean timeSlotBeanAf = new TimeSlotBean();

                            timeSlotBeanAf.setTimings(timingObject.getString("timings"));
                            timeSlotBeanAf.setId(timingObject.getString("id"));
                            timeSlotBeanAf.setDays(timingObject.getString("days"));
                            timeSlotBeanAf.setWday(timingObject.getString("wday"));
                            timeSlotBeanAf.setCreated_date(timingObject.getString("created_date"));

                            timingItemsAF.add(timeSlotBeanAf);

                        }

                        JSONArray EveningArray = jsonObject.getJSONArray("evening");
                        // Create a list to hold the timing items


                        // Parse the JSON array and extract timing data
                        for (int i = 0; i < EveningArray.length(); i++) {
                            JSONObject timingObject = EveningArray.getJSONObject(i);

                            TimeSlotBean timeSlotBeanEvening = new TimeSlotBean();

                            timeSlotBeanEvening.setTimings(timingObject.getString("timings"));
                            timeSlotBeanEvening.setId(timingObject.getString("id"));
                            timeSlotBeanEvening.setDays(timingObject.getString("days"));
                            timeSlotBeanEvening.setWday(timingObject.getString("wday"));
                            timeSlotBeanEvening.setCreated_date(timingObject.getString("created_date"));

                            timingItemsEvening.add(timeSlotBeanEvening);
                        }
                        allTimingItems = new ArrayList<>();
                        allTimingItems.addAll(timingItems);
                        allTimingItems.addAll(timingItemsAF);
                        allTimingItems.addAll(timingItemsEvening);

                        // Create an adapter for the RecyclerView
                        morningAdapter = new TimeSlotAdapter(TimeSlotActivity.this,timingItems,TimeSlotActivity.this::onItemClick, "morning");
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(TimeSlotActivity.this, 3);
                        recyclerViewMorning.setLayoutManager(gridLayoutManager);
                        recyclerViewMorning.setAdapter(morningAdapter);


                        // Create an adapter for the RecyclerView
                        afternoonAdapter = new TimeSlotAdapter(TimeSlotActivity.this,timingItemsAF,TimeSlotActivity.this::onItemClick, "afternoon");
                        gridLayoutManager = new GridLayoutManager(TimeSlotActivity.this, 3);
                        recyclerViewAfterNoon.setLayoutManager(gridLayoutManager);
                        recyclerViewAfterNoon.setAdapter(afternoonAdapter);

                        // Create an adapter for the RecyclerView
                        eveningAdapter = new TimeSlotAdapter(TimeSlotActivity.this,timingItemsEvening,TimeSlotActivity.this::onItemClick, "evening");
                        gridLayoutManager = new GridLayoutManager(TimeSlotActivity.this, 3);
                        recyclerViewEvening.setLayoutManager(gridLayoutManager);
                        recyclerViewEvening.setAdapter(eveningAdapter);

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

        mrecyclerView =findViewById(R.id.activity_time_slot_recycleview);

        mimgBackIcon=findViewById(R.id.activity_time_slot_img_backIcon);

        mllMorning=findViewById(R.id.activity_time_slot_morning_layout);
        mllAfternoon=findViewById(R.id.activity_time_slot_afternoon_layout);
        mllEvening=findViewById(R.id.activity_time_slot_evening_layout);

        recyclerViewMorning =findViewById(R.id.activity_time_slot_morning_sheet);
        recyclerViewAfterNoon =findViewById(R.id.activity_time_slot_afternoon_sheet);
        recyclerViewEvening =findViewById(R.id.activity_time_slot_evening_sheet);


        mllOrderDetails= findViewById(R.id.activity_time_slot_linearLayout_order_details);
        mtxtViewID =findViewById(R.id.activity_my_order_details_txtView_id);
        mtxtViewtitle =findViewById(R.id.activity_my_order_details_txtView_title);
        mtxtViewDate = findViewById(R.id.activity_my_order_details_txtView_date);
        mtxtViewTime =findViewById(R.id.activity_my_order_details_txtView_time);
        mtxtViewPrice =findViewById(R.id.activity_my_order_details_txtView_price);
        mtxtViewStatus =findViewById(R.id.activity_my_order_details_txtView_status);
        mtxtViewAddress  =findViewById(R.id.activity_my_order_details_txtView_address);

        mButton = findViewById(R.id.activity_time_slot_button_confirmTimeSlot);
        mCheckNetworkConnection = new CheckNetworkConnection();
        mApiRequest = new ApiRequest(this);

    }

    @Override
    public void onClick(View v) {
        if ( v == mllMorning ){
            recyclerViewAfterNoon.setVisibility(View.GONE);
            recyclerViewEvening.setVisibility(View.GONE);
            recyclerViewMorning.setVisibility(View.VISIBLE);
        } else if (v==mllAfternoon) {
            recyclerViewEvening.setVisibility(View.GONE);
            recyclerViewMorning.setVisibility(View.GONE);
            recyclerViewAfterNoon.setVisibility(View.VISIBLE);
        } else if (v==mllEvening) {
            recyclerViewEvening.setVisibility(View.VISIBLE);
            recyclerViewMorning.setVisibility(View.GONE);
            recyclerViewAfterNoon.setVisibility(View.GONE);
        } else if (v==mimgBackIcon) {
            onBackPressed();
        } else if (v==mButton) {
            // Pass selected time and date back to OrderSummary activity
            if (!(selectedDate==null) && !(selectedTime==null)) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedTime", selectedTime);
                resultIntent.putExtra("selectedDate", selectedDate);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
            else Toast.makeText(this, "Please select Time and Date", Toast.LENGTH_SHORT).show();
        }
    }
    public void onDateItemClick(DayDateModel dayDateModel) {
        // Handle item click from DateAdapter
        String dayOfWeek = dayDateModel.getDay();
        selectedDate = dayDateModel.getDate();

        int dayOfWeekInt = getDayOfWeekInt(dayOfWeek);// Convert day string to corresponding int value

        callApiTimeSlot(dayOfWeekInt); // Call API with the selected day
    }
   public void onItemClick(int position, String type) {

       // Reset all selected flags to false for all items in all lists
       for (TimeSlotBean model : timingItems) {
           model.setSelected(false);
       }
       for (TimeSlotBean model : timingItemsAF) {
           model.setSelected(false);
       }
       for (TimeSlotBean model : timingItemsEvening) {
           model.setSelected(false);
       }

       // Determine the list and item to set selected
       List<TimeSlotBean> timingListToSetSelected = null;
       TimeSlotBean selectedItem = null;
       switch (type) {
           case "morning":
               timingListToSetSelected = timingItems;
               selectedItem = timingItems.get(position);
               break;
           case "afternoon":
               timingListToSetSelected = timingItemsAF;
               selectedItem = timingItemsAF.get(position);
               break;
           case "evening":
               timingListToSetSelected = timingItemsEvening;
               selectedItem = timingItemsEvening.get(position);
               break;
           default:
               break;
       }

       // Set the selected flag to true for the clicked item
       if (timingListToSetSelected != null && selectedItem != null) {
           selectedItem.setSelected(true);

           selectedTime = selectedItem.getTimings();
           /*selectedDate = selectedItem.getDays();*/
       }

       // Notify all adapters that the data set has changed to update the UI for all lists
       morningAdapter.notifyDataSetChanged();
       afternoonAdapter.notifyDataSetChanged();
       eveningAdapter.notifyDataSetChanged();
   }

    private int getDayOfWeekInt(String dayOfWeek) {
        switch (dayOfWeek) {
            case "Mon":
                return 1;
            case "Tue":
                return 2;
            case "Wed":
                return 3;
            case "Thu":
                return 4;
            case "Fri":
                return 5;
            case "Sat":
                return 6;
            case "Sun":
                return 7;
            default:
                return -1; // Handle invalid day strings
        }
    }
}