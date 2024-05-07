package com.example.homeservicesdemo.helperclass;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseSingle extends SQLiteOpenHelper {
    private Context mcontext;
    private static final String DATABASE_NAME = "HomeServices.db";
    private static final int DATABASE_VERSION=1;
    // Table names
    private static final String TABLE_CART_ITEMS = "cart_items";
    private static final String TABLE_SELECTED_SERVICES = "selected_services";

    // Common column names
    private static final String KEY_ID = "id";

    // cart_items table column names
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_CATEGORY_NAME = "category_name";
    private static final String KEY_SERVICES_LIST = "services_list";
    private static final String KEY_TOTAL_SERVICE_PRICE = "total_price";

    // selected_services table column names
    private static final String KEY_SERVICE_ID = "service_id";
    private static final String KEY_SUB_SERVICE_ID = "sub_service_id";
    private static final String KEY_SERVICE_NAME = "service_name";
    private static final String KEY_SUB_SERVICE_NAME="sub_service_name";
    private static final String KEY_SERVICE_PRICE = "price";
    private static final String KEY_QUANTITY = "quantity";

    public DataBaseSingle(@Nullable Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
