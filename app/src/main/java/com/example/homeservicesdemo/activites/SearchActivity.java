package com.example.homeservicesdemo.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.adapters.SearchAdapter;
import com.example.homeservicesdemo.models.SearchedSearvicesItems;
import com.example.homeservicesdemo.utils.ApiRequest;
import com.example.homeservicesdemo.utils.CheckNetworkConnection;
import com.example.homeservicesdemo.utils.Singleton;
import com.example.homeservicesdemo.utils.VolleyResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private CheckNetworkConnection mCheckNetworkConnection;
    private ApiRequest mApiRequest;
    private ImageView mimgBackIcon;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private List<SearchedSearvicesItems> searchItemList;
    private String userId;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id","");

        initializeViews();
        mimgBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setupRecyclerView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text changes if needed
                if (mCheckNetworkConnection.isNetworkAvailable(SearchActivity.this)) {
                    callSearchApi(newText);
                } else {
                    Toast.makeText(SearchActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

    }



    private void setupRecyclerView() {
        searchItemList = new ArrayList<>(); // Initialize with an empty list

        recyclerView = findViewById(R.id.activity_search_recyclerView_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter = new SearchAdapter(searchItemList);
        recyclerView.setAdapter(searchAdapter);
    }

    private void updateRecyclerView(List<SearchedSearvicesItems> newSearchItemList) {
        searchItemList.clear();
        searchItemList.addAll((Collection<? extends SearchedSearvicesItems>) newSearchItemList);
        searchAdapter.notifyDataSetChanged();
    }
    private void callSearchApi(String selectedItem) {
        JSONObject jsonObjectMap = new JSONObject();
        try {
            jsonObjectMap.put("user_id", userId);
            jsonObjectMap.put("token", "Q62qY9851ZM5BXHDzxWScEJfKTZAP8Jp");
            jsonObjectMap.put("term", selectedItem);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApiRequest.callPostJson(this, jsonObjectMap, Singleton.getInstance().getUrlSearch(), new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {
                System.out.println("HomePageActivity: response " + response.toString());

            }

            @Override
            public void onJsonResponse(JSONObject jsonObject) {
                System.out.println("HomePageActivity: jsonresponse " + jsonObject.toString());

                try {

                    String status = jsonObject.getString("status");
                    Toast.makeText(SearchActivity.this, status, Toast.LENGTH_SHORT).show();

                    if ("success".equals(status)) {
                        JSONArray searchDataArray = jsonObject.getJSONArray("search_data");
                        if (searchDataArray.length() > 0) {
                            List<SearchedSearvicesItems> newSearchItemList = parseSearchData(searchDataArray);
                            updateRecyclerView(newSearchItemList);
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
    private List<SearchedSearvicesItems> parseSearchData(JSONArray searchDataArray) {
        List<SearchedSearvicesItems> searchItemList = new ArrayList<>();

        for (int i = 0; i < searchDataArray.length(); i++) {

            try {
                JSONObject searchData = searchDataArray.getJSONObject(i);
                SearchedSearvicesItems item = new SearchedSearvicesItems();

                item.setCategory_id(searchData.getString("category_id"));
                item.setSubcategory_id(searchData.getString("subcategory_id"));
                item.setService_id(searchData.getString("service_id"));
                item.setCategory_name(searchData.getString("category_name"));
                item.setSubcategory_name(searchData.getString("subcategory_name"));
                item.setImage(searchData.getString("image"));
                item.setPrice(searchData.getString("price"));
                item.setService_name(searchData.getString("service_name"));

                searchItemList.add(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return searchItemList;
    }
    private void initializeViews() {
        searchView = findViewById(R.id.activity_search_searchView);
        mimgBackIcon = findViewById(R.id.activity_search_img_backIcon);

        mCheckNetworkConnection = new CheckNetworkConnection();
        mApiRequest = new ApiRequest(this);
    }
}