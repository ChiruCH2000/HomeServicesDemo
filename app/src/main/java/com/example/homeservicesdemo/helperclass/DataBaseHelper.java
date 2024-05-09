package com.example.homeservicesdemo.helperclass;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "HomeServiices.db";
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
    private static final String KEY_TOTAL_PRICE = "total_price";

    // selected_services table column names
    private static final String KEY_SERVICE_ID = "service_id";
    private static final String KEY_SUB_SERVICE_ID = "sub_service_id";
    private static final String KEY_SERVICE_NAME = "service_name";
    private static final String KEY_SUB_SERVICE_NAME="sub_service_name";
    private static final String KEY_PRICE = "price";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_TYPE = "type";
    private static final String KEY_VARIENT_NAME = "varient_name";

    private static final String KEY_VARIENT_PRICE = "varient_name";
    private static final String KEY_VARIENT_ID = "varient_id";
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CART_ITEMS_TABLE = "CREATE TABLE " + TABLE_CART_ITEMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_CATEGORY_ID + " INTEGER,"
                + KEY_CATEGORY_NAME + " TEXT,"
                + KEY_SERVICES_LIST + " TEXT,"
                + KEY_TOTAL_PRICE + " REAL"
                + ")";
        db.execSQL(CREATE_CART_ITEMS_TABLE);
        String CREATE_SELECTED_SERVICES_TABLE = "CREATE TABLE " + TABLE_SELECTED_SERVICES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_CATEGORY_ID + " INTEGER,"
                + KEY_SERVICE_ID + " INTEGER,"
                + KEY_SUB_SERVICE_ID + " INTEGER,"
                + KEY_CATEGORY_NAME + " TEXT,"
                + KEY_SERVICE_NAME + " TEXT,"
                + KEY_SUB_SERVICE_NAME + " TEXT,"
                + KEY_PRICE + " REAL,"
                + KEY_QUANTITY + " INTEGER,"
                + KEY_TYPE + "TEXT,"
                + KEY_VARIENT_ID + " INTEGER,"
                + KEY_VARIENT_NAME + " TEXT,"
                + KEY_VARIENT_PRICE + " REAL"
                + ")";
        db.execSQL(CREATE_SELECTED_SERVICES_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SELECTED_SERVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART_ITEMS);
        // Create tables again
        onCreate(db);
    }
    @SuppressLint("Range")
    // Method to add selected service to the selected_services table
    public void addToCart(String serviceId, String categoryId,String categoryName, String serviceName,String subserviceId,String subServiceName, double servicePrice, int quantity,String type) {

        SQLiteDatabase db = this.getWritableDatabase();

        // Check if cart entry already exists for the category
        Cursor cartCursor = db.rawQuery("SELECT * FROM " + TABLE_CART_ITEMS + " WHERE " + KEY_CATEGORY_ID + " = ?", new String[]{categoryId});
        double totalPrice = servicePrice * quantity; // Calculate total price for this service
        if (cartCursor.getCount() == 0) {
            // If cart entry doesn't exist, create a new one
            ContentValues cartValues = new ContentValues();
            cartValues.put(KEY_CATEGORY_ID, categoryId);
            cartValues.put(KEY_CATEGORY_NAME, categoryName);
            cartValues.put(KEY_TOTAL_PRICE, totalPrice); // Set initial total price to service price * quantity
            db.insert(TABLE_CART_ITEMS, null, cartValues);
        } else {
            // If cart entry already exists, update the total price
            cartCursor.moveToFirst();
            double currentTotalPrice = cartCursor.getDouble(cartCursor.getColumnIndex(KEY_TOTAL_PRICE));
            totalPrice += currentTotalPrice; // Add current total price to the new service price
            ContentValues cartUpdateValues = new ContentValues();
            cartUpdateValues.put(KEY_TOTAL_PRICE, totalPrice);
            db.update(TABLE_CART_ITEMS, cartUpdateValues, KEY_CATEGORY_ID + " = ?", new String[]{categoryId});
        }
        
        cartCursor.close();
        ContentValues values = new ContentValues();
        values.put(KEY_SERVICE_ID, serviceId);
        values.put(KEY_CATEGORY_ID, categoryId);
        values.put(KEY_CATEGORY_NAME,categoryName);
        values.put(KEY_SERVICE_NAME, serviceName);
        values.put(KEY_SUB_SERVICE_ID, subserviceId);
        values.put(KEY_SUB_SERVICE_NAME,subServiceName);
        values.put(KEY_PRICE, servicePrice);
        values.put(KEY_QUANTITY, quantity);
        values.put(KEY_TYPE,type);
        long rowId = db.insert(TABLE_SELECTED_SERVICES, null, values);
        db.close();
    }

    public boolean isServiceInCart(String serviceId) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean isInCart = false;
        // Define the query to check if the service is in the cart
        String query = "SELECT COUNT(*) FROM " + TABLE_SELECTED_SERVICES +
                " WHERE " + KEY_SERVICE_ID + " = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{serviceId});
        // Check if the cursor has any rows
        if (cursor != null && cursor.moveToFirst()) {
            // Get the count of rows returned by the query
            int count = cursor.getInt(0);
            // If count > 0, the service is already in the cart
            isInCart = count > 0;
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return isInCart;
    }

    public void removeServiceFromCart(String serviceId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Define the WHERE clause to delete the service based on its ID
        String selection = KEY_SERVICE_ID + " = ?";
        String[] selectionArgs = { serviceId };

        // Execute the delete operation
        db.delete(TABLE_SELECTED_SERVICES, selection, selectionArgs);

        // Close the database connection
        db.close();
    }
    public void removeCategoryFromCart(String categoryId){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = KEY_CATEGORY_ID + " = ?";
        String[] selectionArgs = { categoryId };
        db.delete(TABLE_CART_ITEMS,selection,selectionArgs);
        db.close();
    }
    @SuppressLint("Range")
    public List<CartItems> getAllCartItems() {
        List<CartItems> cartItemsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to select all rows from the cart_items table
        String query = "SELECT * FROM " + TABLE_CART_ITEMS;

        // Execute the query
        Cursor cursor = db.rawQuery(query, null);

        // Iterate through the cursor to retrieve data and populate the list
        if (cursor.moveToFirst()) {
            do {
                CartItems cartItem = new CartItems();
                cartItem.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex(KEY_ID))));
                cartItem.setCategoryId(String.valueOf(cursor.getInt(cursor.getColumnIndex(KEY_CATEGORY_ID))));
                cartItem.setCategoryName(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_NAME)));
                // Fetch services list based on category ID
                int categoryId = cursor.getInt(cursor.getColumnIndex(KEY_CATEGORY_ID));
                List<SelectedServiceDetails> selectedServices = getSelectedCategoryServices(categoryId);

                if (selectedServices.isEmpty()) {
                    removeCategoryFromCart(String.valueOf(categoryId));
                    continue; // Move to the next iteration
                }

                List<String> serviceNamesList = new ArrayList<>(); // Create a list to store service names
                StringBuilder servicesListBuilder = new StringBuilder();
                for (SelectedServiceDetails service : selectedServices) {
                    serviceNamesList.add(service.getServiceName()); // Add service name to the list
                    servicesListBuilder.append(service.getServiceName()).append(", ");
                }
                String servicesList = servicesListBuilder.toString();
                // Remove the last comma and space
                if (servicesList.length() > 2) {
                    servicesList = servicesList.substring(0, servicesList.length() - 2);
                }
                cartItem.setServiceNames(serviceNamesList); // Set the list of service names

                double totalPrice = getTotalCartPrice(String.valueOf(categoryId));
                cartItem.setTotalPrice(String.valueOf(totalPrice));

                cartItemsList.add(cartItem);
            } while (cursor.moveToNext());
        }

        // Close the cursor and database connection
        cursor.close();
        db.close();

        // Return the list of cart items
        return cartItemsList;
    }
    //This is for cart items price of perticular categoryid
    @SuppressLint("Range")
    public double getTotalCartPrice(String categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalPrice = 0.0;

        // Query to get the total price for the specified categoryId
        String query = "SELECT SUM(" + KEY_PRICE + " * " + KEY_QUANTITY + ") AS total FROM " + TABLE_SELECTED_SERVICES +
                " WHERE " + KEY_CATEGORY_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{categoryId});

        // Extract the total price from the cursor
        if (cursor.moveToFirst()) {
            totalPrice = cursor.getDouble(cursor.getColumnIndex("total"));
        }

        cursor.close();
        db.close();

        return totalPrice;
    }
    @SuppressLint("Range")
    public List<SelectedServiceDetails> getSelectedCategoryServices(int categoryId) {
        List<SelectedServiceDetails> selectedcategoryServices = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT *, (" + KEY_PRICE + " * " + KEY_QUANTITY + ") AS total_price FROM " + TABLE_SELECTED_SERVICES +
                " WHERE " + KEY_CATEGORY_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(categoryId)});

        if (cursor.moveToFirst()) {
            do {
                SelectedServiceDetails selectedServiceDetails = new SelectedServiceDetails();

                selectedServiceDetails.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                selectedServiceDetails.setServiceId(cursor.getInt(cursor.getColumnIndex(KEY_SERVICE_ID)));
                selectedServiceDetails.setCategoryId(cursor.getInt(cursor.getColumnIndex(KEY_CATEGORY_ID)));
                selectedServiceDetails.setServiceName(cursor.getString(cursor.getColumnIndex(KEY_SERVICE_NAME)));
                selectedServiceDetails.setSubServiceId(cursor.getInt(cursor.getColumnIndex(KEY_SUB_SERVICE_ID)));
                selectedServiceDetails.setSubServiceName(cursor.getString(cursor.getColumnIndex(KEY_SUB_SERVICE_NAME)));
                selectedServiceDetails.setQuantity(cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY))); // Retrieve quantity
                selectedServiceDetails.setPrice(cursor.getDouble(cursor.getColumnIndex(KEY_PRICE))); // Retrieve price

                selectedcategoryServices.add(selectedServiceDetails);
                
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return selectedcategoryServices;
    }
    // This is for Quantity update according to the services increment or decrement
    public void updateQuantity(int serviceId, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_QUANTITY, newQuantity);

        db.update(TABLE_SELECTED_SERVICES, values, KEY_SERVICE_ID + " = ?",
                new String[]{String.valueOf(serviceId)});
        db.close();
    }
    // Method to update the total price for a category when a service quantity is updated
    public void updateCategoryTotalPrice(String categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Calculate the new total price for the category
            double newTotalPrice = getTotalCartPrice(categoryId);

            // Update the total price in the cart_items table
            ContentValues values = new ContentValues();
            values.put(KEY_TOTAL_PRICE, newTotalPrice);
            db.update(TABLE_CART_ITEMS, values, KEY_CATEGORY_ID + " = ?", new String[]{categoryId});
            db.close();
        } finally {
            // Close the database connection
            db.close();
        }
    }
    @SuppressLint("Range")
    public int getServiceQuantity(String serviceId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int quantity = 0;
        Cursor cursor = db.rawQuery("SELECT " + KEY_QUANTITY + " FROM " + TABLE_SELECTED_SERVICES + " WHERE " + KEY_SERVICE_ID + " = ?", new String[]{serviceId});
        if (cursor.moveToFirst()) {
            quantity = cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY));
        }
        cursor.close();
        db.close();
        return quantity;
    }
    /*public void addToCartWithVarient(String serviceId, String categoryId,String categoryName, String serviceName,String subserviceId,String subServiceName, double servicePrice, int quantity,String type,String varientId,String varientName,double varientPrice) {

        SQLiteDatabase db = this.getWritableDatabase();

        // Check if cart entry already exists for the category
        Cursor cartCursor = db.rawQuery("SELECT * FROM " + TABLE_CART_ITEMS + " WHERE " + KEY_CATEGORY_ID + " = ?", new String[]{categoryId});
        double totalPrice = varientPrice * quantity; // Calculate total price for this service
        if (cartCursor.getCount() == 0) {
            // If cart entry doesn't exist, create a new one
            ContentValues cartValues = new ContentValues();
            cartValues.put(KEY_CATEGORY_ID, categoryId);
            cartValues.put(KEY_CATEGORY_NAME, categoryName);
            cartValues.put(KEY_TOTAL_PRICE, totalPrice); // Set initial total price to service price * quantity
            db.insert(TABLE_CART_ITEMS, null, cartValues);
        } else {
            // If cart entry already exists, update the total price
            cartCursor.moveToFirst();
            double currentTotalPrice = cartCursor.getDouble(cartCursor.getColumnIndex(KEY_TOTAL_PRICE));
            totalPrice += currentTotalPrice; // Add current total price to the new service price
            ContentValues cartUpdateValues = new ContentValues();
            cartUpdateValues.put(KEY_TOTAL_PRICE, totalPrice);
            db.update(TABLE_CART_ITEMS, cartUpdateValues, KEY_CATEGORY_ID + " = ?", new String[]{categoryId});
        }

        cartCursor.close();
        ContentValues values = new ContentValues();
        values.put(KEY_SERVICE_ID, serviceId);
        values.put(KEY_CATEGORY_ID, categoryId);
        values.put(KEY_CATEGORY_NAME,categoryName);
        values.put(KEY_SERVICE_NAME, serviceName);
        values.put(KEY_SUB_SERVICE_ID, subserviceId);
        values.put(KEY_SUB_SERVICE_NAME,subServiceName);
        values.put(KEY_PRICE, servicePrice);
        values.put(KEY_QUANTITY, quantity);
        values.put(KEY_TYPE,type);
        values.put(KEY_VARIENT_ID,varientId);
        values.put(KEY_VARIENT_NAME,varientName);
        values.put(KEY_VARIENT_PRICE,varientPrice);
        long rowId = db.insert(TABLE_SELECTED_SERVICES, null, values);
        db.close();
    }
    public int getVarientQuantity(String variantId) {
        int quantity =0;
        return quantity;
    }
    public boolean isVarientInCart(String variantId){

        SQLiteDatabase db =this.getReadableDatabase();
        boolean isInCart = false;
        String query = " SELECT COUNT(*) FROM " + TABLE_SELECTED_SERVICES +
                " WHERE " + KEY_VARIENT_ID + " = ?";

        db.close();

        return isInCart;
    }*/
    private void updateOrInsertCartEntry(String categoryId, String categoryName, double totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Check if cart entry already exists for the category
            Cursor cartCursor = db.rawQuery("SELECT * FROM " + TABLE_CART_ITEMS + " WHERE " + KEY_CATEGORY_ID + " = ?", new String[]{categoryId});

            if (cartCursor.getCount() == 0) {
                // If cart entry doesn't exist, create a new one
                ContentValues cartValues = new ContentValues();
                cartValues.put(KEY_CATEGORY_ID, categoryId);
                cartValues.put(KEY_CATEGORY_NAME, categoryName);
                cartValues.put(KEY_TOTAL_PRICE, totalPrice);
                db.insert(TABLE_CART_ITEMS, null, cartValues);
            } else {
                // If cart entry already exists, update the total price
                cartCursor.moveToFirst();
                @SuppressLint("Range")
                double currentTotalPrice = cartCursor.getDouble(cartCursor.getColumnIndex(KEY_TOTAL_PRICE));
                totalPrice += currentTotalPrice; // Add current total price to the new service price
                ContentValues cartUpdateValues = new ContentValues();
                cartUpdateValues.put(KEY_TOTAL_PRICE, totalPrice);
                db.update(TABLE_CART_ITEMS, cartUpdateValues, KEY_CATEGORY_ID + " = ?", new String[]{categoryId});
            }
            cartCursor.close();
        } finally {
            db.close();
        }
    }

    public void addToCartWithVariant(String serviceId, String categoryId, String categoryName, String serviceName, String subserviceId, String subServiceName, double servicePrice, int quantity, String type, String varientId, String varientName, double varientPrice) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // Calculate total price for this service
            double totalPrice = varientPrice * quantity;

            // Update or insert the cart entry for the category
            updateOrInsertCartEntry(categoryId, categoryName, totalPrice);

            // Insert selected service details with variant
            insertSelectedServiceWithVariant(db, serviceId, categoryId, categoryName, serviceName, subserviceId, subServiceName, servicePrice, quantity, type, varientId, varientName, varientPrice);
        } finally {
            db.close();
        }
    }

    private void insertSelectedServiceWithVariant(SQLiteDatabase db, String serviceId, String categoryId, String categoryName, String serviceName, String subserviceId, String subServiceName, double servicePrice, int quantity, String type, String varientId, String varientName, double varientPrice) {
        ContentValues values = new ContentValues();
        values.put(KEY_SERVICE_ID, serviceId);
        values.put(KEY_CATEGORY_ID, categoryId);
        values.put(KEY_CATEGORY_NAME, categoryName);
        values.put(KEY_SERVICE_NAME, serviceName);
        values.put(KEY_SUB_SERVICE_ID, subserviceId);
        values.put(KEY_SUB_SERVICE_NAME, subServiceName);
        values.put(KEY_PRICE, servicePrice);
        values.put(KEY_QUANTITY, quantity);
        values.put(KEY_TYPE, type);
        values.put(KEY_VARIENT_ID, varientId);
        values.put(KEY_VARIENT_NAME, varientName);
        values.put(KEY_VARIENT_PRICE, varientPrice);
        db.insert(TABLE_SELECTED_SERVICES, null, values);
    }

    @SuppressLint("Range")
    public int getVarientQuantity(String variantId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int quantity = 0;
        Cursor cursor = db.rawQuery("SELECT " + KEY_QUANTITY + " FROM " + TABLE_SELECTED_SERVICES + " WHERE " + KEY_VARIENT_ID + " = ?", new String[]{variantId});
        if (cursor.moveToFirst()) {
            quantity = cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY));
        }
        cursor.close();
        db.close();
        return quantity;
    }

    public boolean isVarientInCart(String variantId) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean isInCart = false;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SELECTED_SERVICES + " WHERE " + KEY_VARIENT_ID + " = ?", new String[]{variantId});
        isInCart = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isInCart;
    }
    
}