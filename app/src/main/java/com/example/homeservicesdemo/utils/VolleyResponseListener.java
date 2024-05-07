package com.example.homeservicesdemo.utils;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface VolleyResponseListener {
    void onResponse(String response);

    void onJsonResponse(JSONObject jsonObject);

    void onError(VolleyError error);
}
