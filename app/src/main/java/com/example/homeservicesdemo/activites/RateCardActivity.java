package com.example.homeservicesdemo.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.adapters.RateCardAdapter;
import com.example.homeservicesdemo.adapters.ServicesStoreAdapter;
import com.example.homeservicesdemo.models.RateCardBean;
import com.example.homeservicesdemo.models.ServicesBean;
import com.example.homeservicesdemo.utils.ApiRequest;
import com.example.homeservicesdemo.utils.CheckNetworkConnection;
import com.example.homeservicesdemo.utils.Singleton;
import com.example.homeservicesdemo.utils.VolleyResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RateCardActivity extends AppCompatActivity {
    private RecyclerView mrecyclerView;
    private TextView mtxtTitle;
    private ImageView mimgBackIcon;
    private CheckNetworkConnection mCheckNetworkConnection;
    private ApiRequest mApiRequest;
    private String userId,mCatId,mSubCatId;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_card);
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id","");
        mCatId=getIntent().getStringExtra("cat_id");
        mSubCatId=getIntent().getStringExtra("subCatId");



        initializViews();

        mtxtTitle.setText(getIntent().getStringExtra("categoryName"));
        mimgBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (mCheckNetworkConnection.isNetworkAvailable(this)) {
            callApiRAteCard();
        } else {
            Toast.makeText(RateCardActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void initializViews() {
        mrecyclerView = findViewById(R.id.activity_rate_card_recyclewrView);
        mtxtTitle = findViewById(R.id.activity_rate_card_textView_title);
        mimgBackIcon = findViewById(R.id.activity_rate_card_img_backIcon);

        mCheckNetworkConnection = new CheckNetworkConnection();
        mApiRequest = new ApiRequest(this);
    }
    private void callApiRAteCard() {
        JSONObject jsonObjectMap = new JSONObject();
        try {
            jsonObjectMap.put("user_id", userId);
            jsonObjectMap.put("cat_id", mCatId);
            jsonObjectMap.put("subcat_id", mSubCatId);
            jsonObjectMap.put("token","Q62qY9851ZM5BXHDzxWScEJfKTZAP8Jp");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        mApiRequest.callPostJson(this, jsonObjectMap, Singleton.getInstance().getUrlRateCard(), new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {
                System.out.println("RateCardActivity: response " + response.toString());
            }

            @Override
            public void onJsonResponse(JSONObject jsonObject) {
                System.out.println("RateCardActivity: jsonresponse " + jsonObject.toString());

                try {

                    String status = jsonObject.getString("status");
                    Toast.makeText(RateCardActivity.this, status, Toast.LENGTH_SHORT).show();

                    if ("success".equals(status)) {
                        if (jsonObject.has("data")) {
                            ArrayList<RateCardBean> rateCardBeanslist = new ArrayList<>();
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            if (dataArray.length() > 0) {
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataobject = dataArray.getJSONObject(i);

                                    RateCardBean rateCardBean = new RateCardBean();

                                    rateCardBean.setId(dataobject.getString("id"));
                                    rateCardBean.setPrice(dataobject.getString("price"));
                                    rateCardBean.setDescription(dataobject.getString("description"));

                                    rateCardBeanslist.add(rateCardBean);

                                }

                                RateCardAdapter rateCardAdapter = new RateCardAdapter(RateCardActivity.this, rateCardBeanslist);
                                mrecyclerView.setLayoutManager(new LinearLayoutManager(RateCardActivity.this));
                                mrecyclerView.setAdapter(rateCardAdapter);

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